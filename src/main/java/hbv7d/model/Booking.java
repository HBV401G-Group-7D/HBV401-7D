package hbv7d.model;


public class Booking {
    private int bookingID;
    private int tourID;
    private int userID;
    private BookingStatus status;


    
    public Booking(int userID, int tourID) {
        this.userID = userID;
        this.tourID = tourID;
        this.status = BookingStatus.PENDING;
    }

    public boolean confirmBooking(){
        return true;
    }

    public boolean cancelBooking(){
        return true;
    }

    public boolean bookingFaild(){
        return true;
    }

    private enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED
    }

    public BookingStatus getStatus() {
        return status;
    }

    public int getTour() {
        return tourID;
    }

    public void setTour(int tourID) {
        this.tourID = tourID;
    }

    public int getUserId() {
        return userID;
    }

    public void setUserId(int userID) {
        this.userID = userID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }
}