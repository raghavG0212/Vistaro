Create database IF NOT EXISTS vistaro_db;
use vistaro_db;

-- =======================
-- TABLE 1: City
-- =======================
CREATE TABLE City (
    city_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- =======================
-- TABLE 2: User
-- =======================
CREATE TABLE User (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20) UNIQUE,
    password VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    role ENUM('USER','ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =======================
-- TABLE 3: Event
-- =======================
CREATE TABLE Event (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    category ENUM('MOVIE','SPORT','EVENT') NOT NULL,
    sub_category VARCHAR(50),                  
    banner_url TEXT,
    thumbnail_url TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =======================
-- TABLE 4: MovieDetails
-- =======================
CREATE TABLE MovieDetails (
    movie_details_id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL UNIQUE,
    cast_json JSON,
    director VARCHAR(50),
    genre VARCHAR(150),
    language VARCHAR(150),
    rating DECIMAL(3,2) DEFAULT 0,
    total_reviews INT DEFAULT 0 CHECK (total_reviews >= 0),
    trailer_url TEXT,
    release_date DATE,
    
    CONSTRAINT fk_movie_event FOREIGN KEY (event_id)
        REFERENCES Event(event_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- =======================
-- TABLE 5: SportDetails
-- =======================
CREATE TABLE SportDetails (
    sport_details_id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL UNIQUE,
    sport_type VARCHAR(100) NOT NULL,
    team1 VARCHAR(150) NOT NULL,
    team2 VARCHAR(150) NOT NULL,
    match_format VARCHAR(50) ,
    venue_info VARCHAR(255),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
   
    CONSTRAINT fk_sport_event FOREIGN KEY (event_id)
        REFERENCES Event(event_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- =======================
-- TABLE 6: GeneralEventDetails
-- =======================
CREATE TABLE GeneralEventDetails (
    general_details_id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL UNIQUE,
    artist VARCHAR(150) NOT NULL,
    host VARCHAR(150),
    genre VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    additional_info VARCHAR(255),
    
    CONSTRAINT fk_general_event FOREIGN KEY (event_id)
        REFERENCES Event(event_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- =======================
-- TABLE 7: Venue
-- =======================
CREATE TABLE Venue (
    venue_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(300) NOT NULL,
    city VARCHAR(100) NOT NULL,
    venue_type ENUM('CINEMA','STADIUM','AUDITORIUM','GROUND','CLUB') NOT NULL,
    screen_name VARCHAR(50) NOT NULL DEFAULT 'Screen 1',
    seat_layout_json JSON
);

-- =======================
-- TABLE 8: EventSlot (showtimes)
-- =======================
CREATE TABLE EventSlot (
    slot_id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL,
    venue_id INT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    format ENUM('NA','_2D','_3D','_4DX') DEFAULT 'NA',
    language VARCHAR(50) ,
    base_price DECIMAL(10,2) NOT NULL CHECK (base_price > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_slot_event FOREIGN KEY (event_id)
        REFERENCES Event(event_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_slot_venue FOREIGN KEY (venue_id)
        REFERENCES Venue(venue_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- =======================
-- TABLE 9: Seat
-- =======================
CREATE TABLE Seat (
    seat_id INT PRIMARY KEY AUTO_INCREMENT,
    slot_id INT NOT NULL,
    row_label VARCHAR(5) NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    seat_type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    is_booked BOOLEAN DEFAULT FALSE,
    is_locked BOOLEAN DEFAULT FALSE,
    locked_until TIMESTAMP NULL,

    UNIQUE(slot_id, row_label, seat_number),

    CONSTRAINT fk_seat_slot FOREIGN KEY (slot_id)
        REFERENCES EventSlot(slot_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- =======================
-- TABLE 10: Booking
-- =======================
CREATE TABLE Booking (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    slot_id INT NOT NULL,
    seats JSON ,
    ticket_total DECIMAL(10,2) DEFAULT 0 CHECK(ticket_total >= 0),
    food_total DECIMAL(10,2) DEFAULT 0 CHECK(food_total >= 0),
    final_amount DECIMAL(10,2) NOT NULL CHECK(final_amount >= 0),
    offer_applied VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_user FOREIGN KEY (user_id)
        REFERENCES User(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_booking_slot FOREIGN KEY (slot_id)
        REFERENCES EventSlot(slot_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- =======================
-- TABLE 11: Payment
-- =======================
CREATE TABLE Payment (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL UNIQUE,
    payment_mode ENUM('CARD','UPI','WALLET','NETBANKING') NOT NULL,
    transaction_id VARCHAR(100) NOT NULL UNIQUE,
    payment_status ENUM('SUCCESS','FAILED','PENDING','REFUNDED') NOT NULL,
    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id)
        REFERENCES Booking(booking_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);


-- =======================
-- TABLE 12: Food (Simplified)
-- =======================
CREATE TABLE Food (
    food_id INT PRIMARY KEY AUTO_INCREMENT,
    slot_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    CONSTRAINT fk_food_slot FOREIGN KEY (slot_id)
        REFERENCES EventSlot(slot_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);


-- =======================
-- TABLE 13: Booking Food
-- =======================
CREATE TABLE booking_food (
    booking_food_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL,
    food_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1 CHECK(quantity > 0),
    CONSTRAINT fk_booking_food_booking
        FOREIGN KEY (booking_id)
        REFERENCES Booking(booking_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_booking_food_food
        FOREIGN KEY (food_id)
        REFERENCES Food(food_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);


-- =======================
-- TABLE 14: Rating
-- =======================
CREATE TABLE Rating (
    rating_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (user_id, event_id),

    CONSTRAINT fk_rating_user FOREIGN KEY (user_id)
        REFERENCES User(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_rating_event FOREIGN KEY (event_id)
        REFERENCES Event(event_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);



-- =======================
-- TABLE 15: Offers
-- =======================
CREATE TABLE Offer (
    offer_id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE, -- e.g. BMS50
    description VARCHAR(200),
    discount_percent INT CHECK (discount_percent >= 0 AND discount_percent <= 100),
    max_discount DECIMAL(10,2) CHECK (max_discount >= 0),
    valid_from DATE NOT NULL,
    valid_till DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);


-- =======================
-- TABLE 16: Giftcard
-- =======================
CREATE TABLE GiftCard (
    giftcard_id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL CHECK(amount > 0),
    is_redeemed BOOLEAN DEFAULT FALSE,
    expiry_date DATE NOT NULL
);



-- =======================
-- TABLE 17: User Event draft
-- =======================
CREATE TABLE UserEventDraft (
    draft_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    sub_category VARCHAR(100),
    banner_url VARCHAR(500),
    thumbnail_url VARCHAR(500),
    artist VARCHAR(255),
    host VARCHAR(255),
    genre VARCHAR(255),
    event_start DATETIME NOT NULL,
    event_end DATETIME NOT NULL,
    venue_id INT NOT NULL,
    slot_start DATETIME,
    slot_end DATETIME,
    base_price DECIMAL(10,2),
    event_id INT,
    slot_id INT,
    approval_status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
    admin_comment VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_draft_user FOREIGN KEY (user_id)
        REFERENCES User(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_draft_event FOREIGN KEY (event_id)
        REFERENCES Event(event_id)
        ON UPDATE CASCADE,
    CONSTRAINT fk_draft_slot FOREIGN KEY (slot_id)
        REFERENCES EventSlot(slot_id)
        ON UPDATE CASCADE
);




--Not Implemented Tables

-- =======================
-- TABLE 18: Wishlist
-- =======================
CREATE TABLE Wishlist (
    wishlist_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(user_id, event_id), -- user can wishlist an event only once

    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id)
        REFERENCES User(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_wishlist_event FOREIGN KEY (event_id)
        REFERENCES Event(event_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
-- =======================
-- TABLE 19: Review
-- =======================
CREATE TABLE Review (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    review_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_review_user FOREIGN KEY (user_id)
        REFERENCES User(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_review_event FOREIGN KEY (event_id)
        REFERENCES Event(event_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);