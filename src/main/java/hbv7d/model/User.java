package hbv7d.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId = -1; // default value
    private String name = "default name"; // default value
    private String email = "default email"; // default value
    private List<Booking> bookings = new ArrayList<>(); // default value

    // Constructor no args
    public User(){

    }

    // Constructor
    public User(int userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    // GETTERS & SETTERS
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Booking> getBookings() {
        return bookings;
    } //viewBooking

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
    
    // METHODS
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void cancelBooking(Booking booking) {
        bookings.remove(booking);
    }

    @Override
    public String toString() {
        return name;
    }
}