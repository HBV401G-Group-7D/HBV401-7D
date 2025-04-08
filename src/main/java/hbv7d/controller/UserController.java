package hbv7d.controller;

import hbv7d.repository.BookingRepository;
import hbv7d.repository.TourRepository;
import hbv7d.repository.UserRepository;

import java.util.List;

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
        // Sækir user og tour
        User user = userRepository.findById(userId);
        if (user == null) {
            return false;
        }
        
        Tour tour = tourRepository.findById(tourId); 
        if (tour == null) {
            return false;
        }
        
        // Býr til nýja bókun með PENDING status
        Booking booking = new Booking(userId, tourId);
        
        // Reynir að bóka sæti á Tour
        boolean seatReserved = tour.reserveSeat(booking);
        
        if (seatReserved) {
            // Setur Booking í gagnagrunn
            int bookingId = bookingRepository.createBooking(booking);
            if (bookingId > 0) {
                booking.setBookingID(bookingId);
                // Staðfestir bókun
                boolean confirmed = bookingRepository.confirmBooking(bookingId);
                if (confirmed) {
                    return true;
                }
            }
        } else {
            // Ef það nær ekki að bóka þá cancellar það bókun
            int bookingId = bookingRepository.createBooking(booking);
            if (bookingId > 0) {
                booking.setBookingID(bookingId);
                bookingRepository.bookingFailed(bookingId);
            }
        }
        return false;
    }

    public boolean cancelBooking(int bookingId){
        return bookingRepository.cancelBooking(bookingId);
    }

    public List<Booking> viewBookings(int userId) {
        return bookingRepository.findBookingsByUserId(userId);
    }

}

