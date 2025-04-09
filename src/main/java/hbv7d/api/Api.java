package hbv7d.api;

import hbv7d.controller.CompanyController;
import hbv7d.controller.UserController;
import hbv7d.model.Booking;
import hbv7d.model.Company;
import hbv7d.model.Tour;
import hbv7d.model.User;
import hbv7d.repository.BookingRepository;
import hbv7d.repository.CompanyRepository;
import hbv7d.repository.TourRepository;
import hbv7d.repository.UserRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

//TODO think about if we should split this up in companyapi and user api or just use them for the ui
public class Api {
    public CompanyController companyController;
    public UserController userController;

    public static CompanyRepository companyRepository;
    public static TourRepository tourRepository;
    public static UserRepository userRepository;
    public static BookingRepository bookingRepository;

    public Api(){

        start();

    }

//    public Api(CompanyRepository companyRepositoryIn, TourRepository tourRepositoryIn, UserRepository userRepositoryIn, BookingRepository bookingRepositoryIn){
//
//    }

    private void start(){
        companyController = new CompanyController(companyRepository,tourRepository);
        userController = new UserController(userRepository, tourRepository, bookingRepository);
    }

    /**
     * Creates a new company if one with the same ID does not already exist.
     *
     * @param company the Company object to be saved
     * @return true if the company was successfully created, false otherwise
     */
    public boolean createCompany(Company company){
        return companyController.createCompany(company);
    }

    /**
     * Retrieves a Company by its ID.
     *
     * @param companyId the ID of the company to retrieve
     * @return the Company object if found; otherwise, null
     */
    public Company getCompany(int companyId) {
        return companyController.getCompany(companyId);
    }

    /**
     * Deletes a Company identified by the given ID.
     *
     * @param companyId the ID of the company to delete
     * @return true if the company was found and deleted; false otherwise
     */
    public boolean deleteCompany(int companyId){
        return companyController.deleteCompany(companyId);
    }

    /**
     * Adds a Tour for a specified Company.
     * The company is retrieved and set as the host of the tour before saving.
     *
     * @param companyId the ID of the company under which the tour is to be added
     * @param tour the Tour object to add
     * @return true if the company exists and the tour is successfully added; false otherwise
     */
    public boolean addTour(int companyId, Tour tour){
        return companyController.addTour(companyId,tour);
    }

    /**
     * Retrieves a list of Tours associated with the specified Company.
     *
     * @param companyId the ID of the company whose tours are to be viewed
     * @return a List of Tour objects; if no tours exist, the list may be empty
     */
    public List<Tour> viewCompanyTours(int companyId){
        return companyController.viewCompanyTours(companyId);
    }

    /**
     * Retrieves a list of all tours available in the database.
     *
     * @return a List of Tour objects representing all available tours.
     */
    public List<Tour> viewAllATours(){
        return companyController.viewAllATours();
    }



    /**
     * Creates a new user.
     *
     * @param user the User object to be saved in the database
     * @return true if the user is successfully saved
     */
    public boolean createUser(User user) {
        userController.createUser(user);
        return true;
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param userId the ID of the user
     * @return the User object if found, otherwise null
     */
    public User getUser(int userId) {
        return userController.getUser(userId);
    }

    /**
     * Deletes the user specified by the given ID.
     *
     * @param userId the ID of the user to delete
     * @return true if the user existed and was deleted, otherwise false
     */
    public boolean deleteUser(int userId) {
        return userController.deleteUser(userId);
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
        return userController.makeBooking(userId,tourId);
    }

    /**
     * Cancels a booking identified by the booking ID.
     *
     * @param bookingId the ID of the booking to cancel
     * @return true if the cancellation was successful, otherwise false
     */
    public boolean cancelBooking(int bookingId) {
        return userController.cancelBooking(bookingId);
    }

    /**
     * Retrieves all bookings associated with the specified user.
     *
     * @param userId the ID of the user whose bookings are to be retrieved
     * @return a List of Booking objects owned by the user
     */
    public List<Booking> viewBookings(int userId) {
        return userController.viewBookings(userId);
    }
}
