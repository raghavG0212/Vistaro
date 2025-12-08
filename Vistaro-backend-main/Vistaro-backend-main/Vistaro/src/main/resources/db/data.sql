INSERT INTO Offer 
(code, description, discount_percent, max_discount, valid_from, valid_till, is_active)
VALUES
('FLAT10', 'Get 10% off on all movie bookings', 10, 75.00, '2025-01-01', '2025-12-31', TRUE),
('WEEKEND20', 'Enjoy 20% off on weekend events', 20, 120.00, '2025-01-01', '2025-12-31', TRUE),
('FESTIVE30', 'Flat 30% festive discount on selected shows', 30, 200.00, '2025-10-01', '2025-12-31', TRUE);

--user
{
  "name" : "raghav",
  "email" :"raghav1@gmail.com",
  "phone" : "8168314273",
  "password" : "12345678",
  "city": "Bengaluru"
}


--venue
{
  "name": "Cinepolis",
  "address": "Sector 32",
  "city": "Chandigarh",
  "venueType": "CINEMA",
  "screenName": "Audi 1",
  "seatLayoutJson": {
    "rows": [
      { "label": "A", "count": 10, "type": "PREMIUM", "price": 350 },
      { "label": "B", "count": 12, "type": "GOLD", "price": 300 },
      { "label": "C", "count": 12, "type": "SILVER", "price": 250 }
    ]
  }
}



--event
{
  "title": "Dune 2",
  "category": "MOVIE",
  "subCategory": "Sci-fi",
  "startTime": "2025-03-10T00:00:00",
  "endTime": "2025-03-30T23:59:59"
}


--eventslot
{
  "eventId": 5,
  "venueId": 3,
  "startTime": "2025-03-15T18:00:00",
  "endTime": "2025-03-15T21:00:00",
  "format": "IMAX",
  "language": "English",
  "basePrice": 250
}


--food
{
 "slotId":3,
 "name" :"Pop Corn - Small",
 "price":200

}

--booking
{
  "userId": 101,
  "slotId": 5,
  "seatIds": [12, 13, 14],
  "foodItems": [
    { "foodId": 3, "quantity": 2 },
    { "foodId": 5, "quantity": 1 }
  ],
  "offerCode": "BMS50",
  "giftCardCode": "GIFT100"
}




{
  "userId": 4,
  "title": "Local Music Night",
  "description": "An amazing evening of indie music vibes.",
  "subCategory": "Music",
  "bannerUrl": "https://example.com/event-banner.png",
  "thumbnailUrl": "https://example.com/thumb.png",

  "artist": "DJ Nova",
  "host": "IndieRecords",
  "genre": "Indie Rock",

  "eventStart": "2025-03-15T18:00:00",
  "eventEnd": "2025-03-15T21:00:00",

  "venueId": 3,
  "slotStart": "2025-03-15T18:00:00",
  "slotEnd": "2025-03-15T21:00:00",

  "basePrice": 399.0
}





