package com.project.vistaro.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.dto.*;
import com.project.vistaro.model.*;
import com.project.vistaro.repository.*;

class BookingServiceImplTest {

    @Mock private BookingDao bookingDao;
    @Mock private PaymentDao paymentDao;
    @Mock private SeatDao seatDao;
    @Mock private BookingFoodDao bookingFoodDao;
    @Mock private OfferDao offerDao;
    @Mock private GiftCardDao giftCardDao;
    @Mock private FoodDao foodDao;

    @InjectMocks private BookingServiceImpl bookingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper factories -------------------------------------------------
    private Seat makeSeat(int seatId, int slotId, BigDecimal price, boolean booked) {
        Seat s = new Seat();
        s.setSeatId(seatId);
        s.setSlotId(slotId);
        s.setPrice(price);
        s.setIsBooked(booked);
        return s;
    }

    private Food makeFood(int id, BigDecimal price) {
        Food f = new Food();
        f.setFoodId(id);
        f.setPrice(price);
        f.setName("Food-" + id);
        return f;
    }

    private BookingFoodRequestDto bfReq(int foodId, int qty) {
        BookingFoodRequestDto b = new BookingFoodRequestDto();
        b.setFoodId(foodId);
        b.setQuantity(qty);
        return b;
    }

    private Offer makeOffer(String code, int percent, BigDecimal maxDiscount, LocalDate from, LocalDate till, boolean active) {
        Offer o = new Offer();
        o.setCode(code);
        o.setDiscountPercent(percent);
        o.setMaxDiscount(maxDiscount);
        o.setValidFrom(from);
        o.setValidTill(till);
        o.setIsActive(active);
        return o;
    }

    private GiftCard makeGiftCard(String code, BigDecimal amount, LocalDate expiry, boolean redeemed, int id) {
        GiftCard g = new GiftCard();
        g.setGiftcardId(id);
        g.setCode(code);
        g.setAmount(amount);
        g.setExpiryDate(expiry);
        g.setIsRedeemed(redeemed);
        return g;
    }

    // ------------------------------------------------------------------
    // TEST: createBooking - basic flow with seats + food
    // ------------------------------------------------------------------
    @Test
    void createBooking_success_withFood() throws Exception {
        // input DTO
        BookingCreateDto dto = new BookingCreateDto();
        dto.setSlotId(10);
        dto.setSeatIds(List.of(1,2));
        dto.setUserId(99);
        dto.setPaymentMode(PaymentMode.UPI);
        dto.setFoodItems(List.of(bfReq(101, 2), bfReq(102, 1)));

        // seats returned by seatDao
        Seat s1 = makeSeat(1, 10, BigDecimal.valueOf(100), false);
        Seat s2 = makeSeat(2, 10, BigDecimal.valueOf(120), false);
        when(seatDao.findByIds(List.of(1,2))).thenReturn(List.of(s1, s2));

        // foods
        Food food101 = makeFood(101, BigDecimal.valueOf(50));
        Food food102 = makeFood(102, BigDecimal.valueOf(30));
        when(foodDao.findById(101)).thenReturn(food101);
        when(foodDao.findById(102)).thenReturn(food102);

        // bookingDao.save should set id — simulate via answer
        when(bookingDao.save(any(Booking.class))).thenAnswer(inv -> {
            Booking b = inv.getArgument(0);
            b.setBookingId(555);
            return b;
        });

        // bookingFoodDao.save we just verify it's called (return value not important)
        when(bookingFoodDao.save(any(BookingFood.class))).thenAnswer(inv -> inv.getArgument(0));

        // payment save returns payment
        when(paymentDao.save(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            p.setPaymentId(777);
            return p;
        });

        // call
        BookingResponseDto res = bookingService.createBooking(dto);

        // assertions: totals computed correctly
        // ticket total = 100 + 120 = 220
        assertEquals(BigDecimal.valueOf(220), res.getTicketTotal());
        // food total = (50*2) + (30*1) = 130
        assertEquals(BigDecimal.valueOf(130), res.getFoodTotal());
        // final amount = 350
        assertEquals(BigDecimal.valueOf(350), res.getFinalAmount());

        // verify interactions
        verify(bookingDao).save(any(Booking.class));
        verify(bookingFoodDao, times(2)).save(any(BookingFood.class));
        verify(seatDao).bookSeats(List.of(1,2));
        verify(paymentDao).save(any(Payment.class));
    }

    // ------------------------------------------------------------------
    // TEST: createBooking - offer applied with max discount
    // ------------------------------------------------------------------
    @Test
    void createBooking_offerApplied_withMaxDiscount() {
        BookingCreateDto dto = new BookingCreateDto();
        dto.setSlotId(1);
        dto.setSeatIds(List.of(11));
        dto.setUserId(1);
        dto.setPaymentMode(PaymentMode.UPI);
        dto.setOfferCode("OFFER10");

        Seat s = makeSeat(11, 1, BigDecimal.valueOf(200), false);
        when(seatDao.findByIds(List.of(11))).thenReturn(List.of(s));

        // Offer 50% but max discount 50
        Offer offer = makeOffer("OFFER10", 50, BigDecimal.valueOf(50), LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), true);
        when(offerDao.findByCode("OFFER10")).thenReturn(Optional.of(offer));

        when(bookingDao.save(any(Booking.class))).thenAnswer(inv -> {
            Booking b = inv.getArgument(0);
            b.setBookingId(10);
            return b;
        });

        when(paymentDao.save(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            p.setPaymentId(99);
            return p;
        });

        BookingResponseDto res = bookingService.createBooking(dto);

        // Ticket total = 200, discount = min(200*0.5=100, max=50) => 50, finalAmount=150
        assertEquals(BigDecimal.valueOf(200), res.getTicketTotal());
        assertEquals(BigDecimal.valueOf(150).setScale(0), res.getFinalAmount().setScale(0));
        assertEquals("OFFER10", res.getOfferApplied());

        verify(offerDao).findByCode("OFFER10");
    }

    // ------------------------------------------------------------------
    // TEST: createBooking - gift card applied and marked redeemed
    // ------------------------------------------------------------------
    @Test
    void createBooking_giftCardApplied_marksRedeemed() {
        BookingCreateDto dto = new BookingCreateDto();
        dto.setSlotId(2);
        dto.setSeatIds(List.of(21));
        dto.setUserId(1);
        dto.setPaymentMode(PaymentMode.UPI);
        dto.setGiftCardCode("GC100");

        Seat s = makeSeat(21, 2, BigDecimal.valueOf(120), false);
        when(seatDao.findByIds(List.of(21))).thenReturn(List.of(s));

        GiftCard gc = makeGiftCard("GC100", BigDecimal.valueOf(50), LocalDate.now().plusDays(5), false, 333);
        when(giftCardDao.findByCode("GC100")).thenReturn(Optional.of(gc));

        when(bookingDao.save(any(Booking.class))).thenAnswer(inv -> {
            Booking b = inv.getArgument(0);
            b.setBookingId(200);
            return b;
        });
        when(paymentDao.save(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            p.setPaymentId(500);
            return p;
        });

        BookingResponseDto res = bookingService.createBooking(dto);

        // Ticket total 120, gift card 50 applied => final = 70
        assertEquals(BigDecimal.valueOf(120), res.getTicketTotal());
        assertEquals(BigDecimal.valueOf(70), res.getFinalAmount());

        verify(giftCardDao).findByCode("GC100");
        verify(giftCardDao).markRedeemed(333);
    }

    // ------------------------------------------------------------------
    // TEST: createBooking - seat already booked -> throw
    // ------------------------------------------------------------------
    @Test
    void createBooking_seatAlreadyBooked_throws() {
        BookingCreateDto dto = new BookingCreateDto();
        dto.setSlotId(3);
        dto.setSeatIds(List.of(31));
        Seat s = makeSeat(31, 3, BigDecimal.ONE, true); // already booked
        when(seatDao.findByIds(List.of(31))).thenReturn(List.of(s));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.createBooking(dto));
        assertTrue(ex.getMessage().contains("already booked"));
    }

    // ------------------------------------------------------------------
    // TEST: getBooking - combined dto built
    // ------------------------------------------------------------------
    @Test
    void getBooking_buildsCombinedDto() {
        int bookingId = 900;
        Booking b = new Booking();
        b.setBookingId(bookingId);
        b.setUserId(12);
        b.setSlotId(55);
        b.setTicketTotal(BigDecimal.valueOf(200));
        b.setFoodTotal(BigDecimal.valueOf(50));
        b.setFinalAmount(BigDecimal.valueOf(250));
        b.setOfferApplied("OFFX");
        b.setCreatedAt(LocalDateTime.of(2025, 1, 1, 10, 0));

        Map<String,Object> details = new HashMap<>();
        details.put("user_name", "Alice");
        details.put("event_title", "ShowTime");
        details.put("venue_name", "BigHall");
        details.put("show_start", Timestamp.valueOf(LocalDateTime.of(2025, 12, 1, 12, 0)));
        details.put("show_end", Timestamp.valueOf(LocalDateTime.of(2025, 12, 1, 14, 0)));

        when(bookingDao.findById(bookingId)).thenReturn(Optional.of(b));
        when(bookingDao.getBookingFullDetails(bookingId)).thenReturn(details);

        List<Map<String,Object>> seats = List.of(Map.of("seat_id", 1, "row_label","A"));
        List<Map<String,Object>> foods = List.of(Map.of("food_id", 10, "food_name","Popcorn"));

        when(seatDao.findSeatsByBooking(bookingId)).thenReturn(seats);
        when(bookingFoodDao.findByBookingIdDetailed(bookingId)).thenReturn(foods);

        Payment p = new Payment();
        p.setPaymentId(77);
        p.setBookingId(bookingId);
        p.setPaymentMode(PaymentMode.UPI);
        p.setPaymentStatus(PaymentStatus.SUCCESS);
        p.setTransactionId("TX-77");
        p.setPaidAt(LocalDateTime.now());
        when(paymentDao.findByBookingId(bookingId)).thenReturn(Optional.of(p));

        CombinedBookingDetailsDto dto = bookingService.getBooking(bookingId);

        assertEquals(bookingId, dto.getBookingId());
        assertEquals("Alice", dto.getUserName());
        assertNotNull(dto.getSeats());
        assertNotNull(dto.getFoodItems());
        assertNotNull(dto.getPayment());
        assertEquals("TX-77", dto.getPayment().getTransactionId());
    }

    // ------------------------------------------------------------------
    // TEST: deleteBooking - refund >=24 hours -> 70%
    // ------------------------------------------------------------------
    @Test
    void deleteBooking_refund_70percent() {
        int bookingId = 400;
        Booking b = new Booking();
        b.setBookingId(bookingId);
        b.setFinalAmount(BigDecimal.valueOf(1000));

        when(bookingDao.findById(bookingId)).thenReturn(Optional.of(b));

        // show start far in future to make hours >=24
        LocalDateTime future = LocalDateTime.now().plusHours(48);
        Map<String,Object> details = Map.of("show_start", Timestamp.valueOf(future));
        when(bookingDao.getBookingFullDetails(bookingId)).thenReturn(details);

        // payment present
        Payment payment = new Payment();
        payment.setPaymentId(10);
        payment.setBookingId(bookingId);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId("TX100");
        when(paymentDao.findByBookingId(bookingId)).thenReturn(Optional.of(payment));

        List<Map<String,Object>> seatList = List.of(Map.of("seat_id", 1));
        List<Map<String,Object>> foodList = List.of(Map.of("food_id", 2));
        when(seatDao.findSeatsByBooking(bookingId)).thenReturn(seatList);
        when(bookingFoodDao.findByBookingIdDetailed(bookingId)).thenReturn(foodList);

        // call
        RefundResponseDto res = bookingService.deleteBooking(bookingId);

        // 70% of 1000 = 700
        assertEquals(BigDecimal.valueOf(700).setScale(2), res.getRefundAmount().setScale(2));
        assertEquals("70%", res.getRefundPercentage());
        assertEquals("REFUNDED", res.getPaymentStatus());

        // verify update and delete calls
        verify(paymentDao).update(argThat(p -> p.getPaymentStatus() == PaymentStatus.REFUNDED));
        verify(seatDao).unlockSeats(anyList());
        verify(bookingDao).deleteById(bookingId);
    }

    // ------------------------------------------------------------------
    // TEST: deleteBooking - refund between 3 and 24 hours -> 50%
    // ------------------------------------------------------------------
    @Test
    void deleteBooking_refund_50percent() {
        int bookingId = 401;
        Booking b = new Booking();
        b.setBookingId(bookingId);
        b.setFinalAmount(BigDecimal.valueOf(200));

        when(bookingDao.findById(bookingId)).thenReturn(Optional.of(b));

        LocalDateTime future = LocalDateTime.now().plusHours(5); // between 3 and 24
        when(bookingDao.getBookingFullDetails(bookingId)).thenReturn(Map.of("show_start", Timestamp.valueOf(future)));

        Payment payment = new Payment(); payment.setPaymentId(11); payment.setBookingId(bookingId); payment.setPaymentStatus(PaymentStatus.SUCCESS); payment.setTransactionId("T11");
        when(paymentDao.findByBookingId(bookingId)).thenReturn(Optional.of(payment));

        when(seatDao.findSeatsByBooking(bookingId)).thenReturn(List.of(Map.of("seat_id", 1)));
        when(bookingFoodDao.findByBookingIdDetailed(bookingId)).thenReturn(List.of(Map.of("food_id", 3)));

        RefundResponseDto res = bookingService.deleteBooking(bookingId);

        // 50% of 200 = 100
        assertEquals(BigDecimal.valueOf(100).setScale(2), res.getRefundAmount().setScale(2));
        assertEquals("50%", res.getRefundPercentage());
    }

    // ------------------------------------------------------------------
    // TEST: deleteBooking - refund less than 3 hours -> 0%
    // ------------------------------------------------------------------
    @Test
    void deleteBooking_refund_0percent() {
        int bookingId = 402;
        Booking b = new Booking();
        b.setBookingId(bookingId);
        b.setFinalAmount(BigDecimal.valueOf(500));

        when(bookingDao.findById(bookingId)).thenReturn(Optional.of(b));

        LocalDateTime future = LocalDateTime.now().plusMinutes(120); // 2 hours -> <3
        when(bookingDao.getBookingFullDetails(bookingId)).thenReturn(Map.of("show_start", Timestamp.valueOf(future)));

        Payment payment = new Payment(); payment.setPaymentId(12); payment.setBookingId(bookingId); payment.setPaymentStatus(PaymentStatus.SUCCESS); payment.setTransactionId("T12");
        when(paymentDao.findByBookingId(bookingId)).thenReturn(Optional.of(payment));

        when(seatDao.findSeatsByBooking(bookingId)).thenReturn(List.of(Map.of("seat_id", 1)));
        when(bookingFoodDao.findByBookingIdDetailed(bookingId)).thenReturn(List.of(Map.of("food_id", 4)));

        RefundResponseDto res = bookingService.deleteBooking(bookingId);

        assertEquals(BigDecimal.ZERO.setScale(2), res.getRefundAmount().setScale(2));
        assertEquals("0%", res.getRefundPercentage());
    }
}
