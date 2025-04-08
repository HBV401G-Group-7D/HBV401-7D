package hbv7d.controller;

import hbv7d.repository.BookingRepository;
import hbv7d.repository.TourRepository;
import hbv7d.repository.UserRepository;
import hbv7d.model.Booking;
import hbv7d.model.Tour;
import hbv7d.model.User;
import java.util.List;

/**
 * The UserController class manages operations related to users including
 * creating users, making and canceling bookings, and viewing user bookings.
 * It works with the UserRepository, TourRepository, and BookingRepository.
 */
public class UserController {
    private UserRepository userRepository;
    private TourRepository tourRepository;
    private BookingRepository bookingRepository;

    /**
     * Default constructor.
     * 
     * Note: This constructor does not initialize the repositories.
     * To use its methods, be sure to set the repositories appropriately.
     * 
     */
    public UserController(){}

    /**
     * Constructs a UserController with the specified repositories.
     *
     * @param userRepository    the repository for user operations
     * @param tourRepository    the repository for tour operations
     * @param bookingRepository the repository for booking operations
     */
    public UserController(UserRepository userRepository, TourRepository tourRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
        this.bookingRepository = bookingRepository;  
    }
    
    /**
     * Creates a new user.
     *
     * @param user the User object to be saved in the database
     * @return true if the user is successfully saved
     */
    public boolean createUser(User user) {
        userRepository.save(user);
        return true;
    }
    
    /**
     * Retrieves a user by its ID.
     *
     * @param userId the ID of the user
     * @return the User object if found, otherwise null
     */
    public User getUser(int userId) {
        return userRepository.findById(userId);
    }
    
    /**
     * Deletes the user specified by the given ID.
     *
     * @param userId the ID of the user to delete
     * @return true if the user existed and was deleted, otherwise false
     */
    public boolean deleteUser(int userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            userRepository.delete(userId);
            return true;
        }
        return false;
    }
    
    /**
     * Attempts to create a booking for the specified user and tour.
     * 
     * This method retrieves the user and tour using their IDs, creates a new booking
     * with an initial PENDING status, then attempts to reserve a seat on the tour.
     * If a seat is reserved, the booking is stored in the database and its status is updated to CONFIRMED.
     * If a seat cannot be reserved, the booking is marked as failed (cancelled).
     * 
     *
     * @param userId the ID of the user making the booking
     * @param tourId the ID of the tour to be booked
     * @return true if the booking was successfully confirmed, otherwise false
     */
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
        
        // Create a new booking with a PENDING status.
        Booking booking = new Booking(userId, tourId);
        
        // Try to reserve a seat on the tour.
        boolean seatReserved = tour.reserveSeat(booking);
        
        if (seatReserved) {
            // Store the booking in the database.
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
            // If seat reservation fails, store the booking and mark it as failed.
            int bookingId = bookingRepository.createBooking(booking);
            if (bookingId > 0) {
                booking.setBookingID(bookingId);
                bookingRepository.bookingFailed(bookingId);
            }
        }
        return false;
    }

    /**
     * Cancels a booking identified by the booking ID.
     *
     * @param bookingId the ID of the booking to cancel
     * @return true if the cancellation was successful, otherwise false
     */
    public boolean cancelBooking(int bookingId) {
        return bookingRepository.cancelBooking(bookingId);
    }

    /**
     * Retrieves all bookings associated with the specified user.
     *
     * @param userId the ID of the user whose bookings are to be retrieved
     * @return a List of Booking objects owned by the user
     */
    public List<Booking> viewBookings(int userId) {
        return bookingRepository.findBookingsByUserId(userId);
    }
}
