
-- ======================== TRIGGERS ========================

-- Trigger 1: auto calculate final amount from ticket_total + food_total if not provided

DELIMITER $$
CREATE TRIGGER 
trg_booking_before_insert
BEFORE INSERT ON booking 
FOR EACH ROW 
BEGIN 
	IF NEW.final_amount IS NULL THEN 
	SET NEW.final_amount = IFNULL(NEW.ticket_total, 0) + IFNULL(NEW.food_total, 0);
	END IF;
END $$
DELIMITER;

-- Trigger 2: Ensure price is always +ve in Food table

DELIMITER $$
CREATE TRIGGER trg_food_before_insert 
BEFORE INSERT ON food
FOR EACH ROW 
BEGIN 
	IF NEW.price <= 0 THEN
	SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Price must be positive';
	END IF;
END $$;
DELIMITER;

-- Trigger 3: end_time > start_time

DELIMITER $$
CREATE TRIGGER trg_event_time_validation 
BEFORE INSERT ON generaleventdetails 
FOR EACH ROW
BEGIN
	IF NEW.end_time <= NEW.start_time 
	THEN 
	SIGNAL SQLSTATE '45000'
	SET MESSAGE_TEXT = 'End time must be greater than start time';
	END IF;
	
END $$;
DELIMITER;

-- Trigger 4: When new food is added for a booking -> update its food_total automatically

DELIMITER $$
CREATE TRIGGER trg_food_after_insert 
AFTER INSERT ON food
FOR EACH ROW
BEGIN
	IF NEW.booking_id IS NOT NULL THEN
	UPDATE booking
	SET 
	food_total = (
	SELECT COALESCE(SUM(price),0) 
	FROM food WHERE booking_id = NEW.booking_id
	),
	final_amount = (
	COALESCE((SELECT ticket_total FROM booking WHERE booking_id = NEW.booking_id),0) + 
	COALESCE((SELECT SUM(price) FROM NEW.booking_id), 0)
	)
	WHERE booking_id = NEW.booking_id;
	END IF;
END $$
DELIMITER;

-- Trigger 5: Prevent duplicate venue within same city

DELIMITER $$
CREATE TRIGGER trg_venue_prevent_duplicate
BEFORE INSERT ON venue
FOR EACH ROW
BEGIN
	IF EXISTS(SELECT 1 FROM venue WHERE name = NEW.name AND city = NEW.city)
	THEN
	SIGNAL SQLSTATE '45000'
	SET  MESSAGE_TEXT = 'This venue already exists in the same city';
	END IF;
END $$
DELIMITER;

-- Trigger 6: Prevent duplicate events with same title & start time

DELIMITER $$
CREATE TRIGGER trg_event_prevent_duplicate
BEFORE INSERT ON event
FOR EACH ROW
BEGIN
	IF EXISTS(SELECT 1 FROM event WHERE title = NEW.title AND start_time = NEW.start_time)
	THEN 
	SIGNAL SQLSTATE '45000'
	SET MESSAGE_TEXT = 'An event with the same title and start time already exists';
	END IF;
END $$
DELIMITER ;

-- Trigger 7: Validate base_price > 0

DELIMITER $$
CREATE TRIGGER trg_slot_price_validation
BEFORE INSERT ON eventslot
FOR EACH ROW
BEGIN
	IF NEW.base_price <= 0 THEN
	SIGNAL SQLSTATE '45000'
	SET MESSAGE_TEXT = 'Base price must be positive';
	END IF;
END $$
DELIMITER ;

-- Trigger 8: Prevent overlapping slots for the same venue

DELIMITER $$
CREATE TRIGGER trg_slot_prevent_overlap
BEFORE INSERT ON eventslot 
FOR EACH ROW
BEGIN
	IF EXISTS(
	SELECT 1
	FROM eventslot
	WHERE venue_id = NEW.venue_id
	AND(
	NEW.start_time BETWEEN start_time AND end_time
	OR NEW.end_time BETWEEN start_time AND end_time
	OR start_time BETWEEN NEW.start_time AND NEW.end_time
	)
	)
	THEN
	SIGNAL SQLSTATE '45000'
	SET MESSAGE_TEXT = 'Slot timing overlaps with an existing slot for this venue';
	END IF;
END $$
DELIMITER ;



DELIMITER $$

CREATE TRIGGER trg_slot_delete_seats
AFTER DELETE ON EventSlot
FOR EACH ROW
BEGIN
    DELETE FROM Seat WHERE slot_id = OLD.slot_id;
END$$

DELIMITER ;



DELIMITER $$

CREATE TRIGGER trg_set_booking_timestamp
BEFORE INSERT ON Booking
FOR EACH ROW
BEGIN
    IF NEW.created_at IS NULL THEN
        SET NEW.created_at = NOW();
    END IF;
END$$

DELIMITER ;
