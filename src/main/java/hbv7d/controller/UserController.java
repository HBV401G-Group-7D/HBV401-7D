package hbv7d.controller;

import hbv7d.repository.BookingRepository;
import hbv7d.repository.TourRepository;
import hbv7d.repository.UserRepository;
import hbv7d.model.Booking;
import hbv7d.model.Tour;
import hbv7d.model.User;

public class UserController {
    private UserRepository userRepository;
    private TourRepository tourRepository;
    private BookingRepository bookingRepository;

    public UserController(){

    }

    public UserController(UserRepository userRepository, TourRepository tourRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
        this.bookingRepository = bookingRepository;
        
    }
    
    public boolean createUser(User user) {
        userRepository.save(user);
        return true;
    }
    

    public User getUser(int userId) {
        return userRepository.findById(userId);
    }
    
    public boolean deleteUser(int userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            userRepository.delete(userId);
            return true;
        }
        return false;
    }


public boolean makeBooking(int userId, int tourId) {
        // Retrieve the user and tour.
        User user = userRepository.findById(userId);
        if (user == null) {
            return false;
        }
        
        Tour tour = tourRepository.findById(tourId); 
        if (tour == null) {
            return false;
        }
        
        // Create a new booking with initial status PENDING.
        Booking booking = new Booking(userId, tourId);
        
        // Attempt to reserve a seat on the tour.
        boolean seatReserved = tour.reserveSeat(booking);
        
        if (seatReserved) {
            // Insert booking in the database.
            int bookingId = bookingRepository.createBooking(booking);
            if (bookingId > 0) {
                booking.setBookingID(bookingId);
                // Confirm the booking.
                boolean confirmed = bookingRepository.confirmBooking(bookingId);
                if (confirmed) {
                    return true;
                }
            }
        } else {
            // If reserving the seat fails, record the booking as failed.
            int bookingId = bookingRepository.createBooking(booking);
            if (bookingId > 0) {
                booking.setBookingID(bookingId);
                bookingRepository.bookingFailed(bookingId);
            }
        }
        return false;
    }
}

