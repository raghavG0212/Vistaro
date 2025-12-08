package com.project.vistaro.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.BookingCreateDto;
import com.project.vistaro.dto.BookingFoodRequestDto;
import com.project.vistaro.dto.BookingResponseDto;
import com.project.vistaro.dto.CombinedBookingDetailsDto;
import com.project.vistaro.dto.PaymentResponseDto;
import com.project.vistaro.dto.RefundResponseDto;
import com.project.vistaro.exception.BadRequestException;
import com.project.vistaro.exception.BusinessRuleException;
import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.Booking;
import com.project.vistaro.model.BookingFood;
import com.project.vistaro.model.GiftCard;
import com.project.vistaro.model.Offer;
import com.project.vistaro.model.Payment;
import com.project.vistaro.model.PaymentStatus;
import com.project.vistaro.model.Seat;
import com.project.vistaro.repository.BookingDao;
import com.project.vistaro.repository.BookingFoodDao;
import com.project.vistaro.repository.FoodDao;
import com.project.vistaro.repository.GiftCardDao;
import com.project.vistaro.repository.OfferDao;
import com.project.vistaro.repository.PaymentDao;
import com.project.vistaro.repository.SeatDao;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;
    private final PaymentDao paymentDao;
    private final SeatDao seatDao;
    private final BookingFoodDao bookingFoodDao;
    private final OfferDao offerDao;
    private final GiftCardDao giftCardDao;
    private final FoodDao foodDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BookingServiceImpl(
            BookingDao bookingDao,
            PaymentDao paymentDao,
            SeatDao seatDao,
            BookingFoodDao bookingFoodDao,
            OfferDao offerDao,
            GiftCardDao giftCardDao,
            FoodDao foodDao
    ) {
        this.bookingDao = bookingDao;
        this.paymentDao = paymentDao;
        this.seatDao = seatDao;
        this.bookingFoodDao = bookingFoodDao;
        this.offerDao = offerDao;
        this.giftCardDao = giftCardDao;
        this.foodDao = foodDao;
    }

    // ---------------------------------------------------
    // CREATE BOOKING (main logic)
    // ---------------------------------------------------
    @Override
    public BookingResponseDto createBooking(BookingCreateDto dto) {
        if (dto.getSeatIds() == null || dto.getSeatIds().isEmpty()) {
        	throw new BadRequestException("At least one seat must be selected");
        }

        // TEMP: dummy user if not provided (you'll replace with real auth later)
        Integer userId = (dto.getUserId() != null) ? dto.getUserId() : 1;

        // 1. Fetch & validate seats
        List<Integer> seatIds = dto.getSeatIds();
        List<Seat> seats = seatDao.findByIds(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new ResourceNotFoundException("Some selected seats do not exist");
        }

        Integer slotIdFromSeats = seats.get(0).getSlotId();
        if (!slotIdFromSeats.equals(dto.getSlotId())) {
            throw new BadRequestException("Selected seats do not belong to this slot");
        }

        for (Seat s : seats) {
            if (Boolean.TRUE.equals(s.getIsBooked())) {
                throw new BusinessRuleException("One or more seats are already booked");
            }
        }

        // 2. Calculate ticket total
        BigDecimal ticketTotal = seats.stream()
                .map(Seat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Calculate food total
        BigDecimal foodTotal = BigDecimal.ZERO;

        if (dto.getFoodItems() != null && !dto.getFoodItems().isEmpty()) {
            for (BookingFoodRequestDto fi : dto.getFoodItems()) {
                var food = foodDao.findById(fi.getFoodId());
                BigDecimal line = food.getPrice()
                        .multiply(BigDecimal.valueOf(fi.getQuantity()));
                foodTotal = foodTotal.add(line);
            }
        }

        BigDecimal total = ticketTotal.add(foodTotal);

        // 4. Apply offer (if present)
        String appliedOfferCode = null;
        if (dto.getOfferCode() != null && !dto.getOfferCode().isBlank()) {
            Offer offer = offerDao.findByCode(dto.getOfferCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid offer code"));

            LocalDate today = LocalDate.now();
            boolean activeDate =
                    !today.isBefore(offer.getValidFrom()) &&
                    !today.isAfter(offer.getValidTill());

            if (!activeDate || !Boolean.TRUE.equals(offer.getIsActive())) {
                throw new BusinessRuleException("Offer is not active or has expired");
            }

            BigDecimal percent = BigDecimal
                    .valueOf(offer.getDiscountPercent())
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            BigDecimal discount = total.multiply(percent);

            if (offer.getMaxDiscount() != null && discount.compareTo(offer.getMaxDiscount()) > 0) {
                discount = offer.getMaxDiscount();
            }

            total = total.subtract(discount);
            if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;

            appliedOfferCode = offer.getCode();
        }

        // 5. Apply gift card (if present)
        if (dto.getGiftCardCode() != null && !dto.getGiftCardCode().isBlank()) {
            GiftCard gc = giftCardDao.findByCode(dto.getGiftCardCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid gift card code"));

            LocalDate today = LocalDate.now();
            if (Boolean.TRUE.equals(gc.getIsRedeemed()) || today.isAfter(gc.getExpiryDate())) {
                throw new BusinessRuleException("Gift card already used or expired");
            }

            BigDecimal applyAmount = gc.getAmount();
            if (applyAmount.compareTo(total) > 0) {
                applyAmount = total;
            }

            total = total.subtract(applyAmount);
            if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;

            // mark redeemed
            giftCardDao.markRedeemed(gc.getGiftcardId());
        }

        BigDecimal finalAmount = total;

        // 6. Store seats as JSON of seat_ids
        String seatsJson;
        try {
            seatsJson = objectMapper.writeValueAsString(seatIds);
        } catch (Exception e) {
            throw new BadRequestException("Could not serialize seat list");
        }

        // 7. Build Booking model
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setSlotId(dto.getSlotId());
        booking.setSeats(seatsJson);
        booking.setTicketTotal(ticketTotal);
        booking.setFoodTotal(foodTotal);
        booking.setFinalAmount(finalAmount);
        booking.setOfferApplied(appliedOfferCode);
        booking.setCreatedAt(LocalDateTime.now());

        // 8. Save booking
        Booking saved = bookingDao.save(booking);

        // 9. Save booking_food rows
        if (dto.getFoodItems() != null && !dto.getFoodItems().isEmpty()) {
            for (BookingFoodRequestDto fi : dto.getFoodItems()) {
                BookingFood bf = new BookingFood();
                bf.setBookingId(saved.getBookingId());
                bf.setFoodId(fi.getFoodId());
                bf.setQuantity(fi.getQuantity());
                bookingFoodDao.save(bf);
            }
        }
        // 10. Mark seats as booked
        seatDao.bookSeats(seatIds);
        // 11. Create Payment automatically
        Payment payment = new Payment();
        payment.setBookingId(saved.getBookingId());
        payment.setPaymentMode(dto.getPaymentMode());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId("VIST-" + System.currentTimeMillis());
        payment.setPaidAt(LocalDateTime.now());
        paymentDao.save(payment);
        // 12. Build response DTO
        BookingResponseDto res = new BookingResponseDto();
        res.setBookingId(saved.getBookingId());
        res.setUserId(saved.getUserId());
        res.setSlotId(saved.getSlotId());
        res.setTicketTotal(saved.getTicketTotal());
        res.setFoodTotal(saved.getFoodTotal());
        res.setFinalAmount(saved.getFinalAmount());
        res.setOfferApplied(saved.getOfferApplied());
        res.setCreatedAt(saved.getCreatedAt());
        return res;
    }


    // ---------------------------------------------------
    // GET BOOKING (full details)
    // ---------------------------------------------------
    @Override
    public CombinedBookingDetailsDto getBooking(int bookingId) {

        Booking b = bookingDao.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Map<String, Object> details = bookingDao.getBookingFullDetails(bookingId);

        List<Map<String, Object>> seatList = seatDao.findSeatsByBooking(bookingId);
        List<Map<String, Object>> foodList = bookingFoodDao.findByBookingIdDetailed(bookingId);

        Payment payment = paymentDao.findByBookingId(bookingId).orElse(null);

        CombinedBookingDetailsDto dto = new CombinedBookingDetailsDto();

        // basic data
        dto.setBookingId(b.getBookingId());
        dto.setUserId(b.getUserId());
        dto.setSlotId(b.getSlotId());
        dto.setTicketTotal(b.getTicketTotal());
        dto.setFoodTotal(b.getFoodTotal());
        dto.setFinalAmount(b.getFinalAmount());
        dto.setOfferApplied(b.getOfferApplied());
        dto.setCreatedAt(b.getCreatedAt());

        // joined metadata
        dto.setUserName((String) details.get("user_name"));
        dto.setEventTitle((String) details.get("event_title"));
        dto.setVenueName((String) details.get("venue_name"));

        Timestamp startTs = (Timestamp) details.get("show_start");
        Timestamp endTs = (Timestamp) details.get("show_end");

        dto.setShowStart(startTs != null ? startTs.toLocalDateTime().toString() : null);
        dto.setShowEnd(endTs != null ? endTs.toLocalDateTime().toString() : null);

        dto.setSeats(seatList);
        dto.setFoodItems(foodList);

        // Convert Payment → PaymentResponseDto
        if (payment != null) {
            PaymentResponseDto pd = new PaymentResponseDto();
            pd.setPaymentId(payment.getPaymentId());
            pd.setBookingId(payment.getBookingId());
            pd.setPaymentMode(payment.getPaymentMode());
            pd.setPaymentStatus(payment.getPaymentStatus());
            pd.setPaidAt(payment.getPaidAt());
            pd.setTransactionId(payment.getTransactionId());
            dto.setPayment(pd);
        }

        return dto;
    }

    // ---------------------------------------------------
    // DELETE BOOKING WITH REFUND
    // ---------------------------------------------------
    @Override
    public RefundResponseDto deleteBooking(int bookingId) {

        Booking b = bookingDao.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Map<String, Object> details = bookingDao.getBookingFullDetails(bookingId);

        Timestamp startTs = (Timestamp) details.get("show_start");
        if (startTs == null) {
            throw new ResourceNotFoundException("Show start time not found");
        }

        LocalDateTime showStart = startTs.toLocalDateTime();

        Payment payment = paymentDao.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking"));

        // capture before deletion
        List<Map<String, Object>> seatList = seatDao.findSeatsByBooking(bookingId);
        List<Map<String, Object>> foodList = bookingFoodDao.findByBookingIdDetailed(bookingId);

        LocalDateTime now = LocalDateTime.now();
        long hours = Duration.between(now, showStart).toHours();

        BigDecimal refundPercent;

        if (hours >= 24) refundPercent = new BigDecimal("0.70");
        else if (hours >= 3) refundPercent = new BigDecimal("0.50");
        else refundPercent = BigDecimal.ZERO;

        BigDecimal refundAmount = b.getFinalAmount()
                .multiply(refundPercent)
                .setScale(2, RoundingMode.HALF_UP);

        // update payment status
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        paymentDao.update(payment);

        // unlock seats
        seatDao.releaseSeats(
                seatList.stream()
                        .map(s -> (Integer) s.get("seat_id"))
                        .toList()
        );

        // delete booking: cascade removes booking_food + payment
        bookingDao.deleteById(bookingId);

        RefundResponseDto dto = new RefundResponseDto();
        dto.setBookingId(bookingId);
        dto.setFinalAmount(b.getFinalAmount());
        dto.setRefundAmount(refundAmount);
        dto.setRefundPercentage(
                refundPercent.multiply(new BigDecimal("100"))
                        .setScale(0, RoundingMode.HALF_UP) + "%"
        );
        dto.setRefundedAt(LocalDateTime.now());
        dto.setSeats(seatList);
        dto.setFoodItems(foodList);
        dto.setPaymentStatus("REFUNDED");
        dto.setTransactionId(payment.getTransactionId());

        return dto;
    }

    // ---------------------------------------------------
    // LIST METHODS
    // ---------------------------------------------------
    @Override
    public List<CombinedBookingDetailsDto> getBookingsByUser(int userId) {
        return bookingDao.findByUserId(userId)
                .stream()
                .map(b -> getBooking(b.getBookingId()))
                .toList();
    }

    @Override
    public List<CombinedBookingDetailsDto> getBookingsBySlot(int slotId) {
        return bookingDao.findBySlotId(slotId)
                .stream()
                .map(b -> getBooking(b.getBookingId()))
                .toList();
    }

}
