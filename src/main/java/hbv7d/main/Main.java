package hbv7d.main;

import hbv7d.api.Api;
import hbv7d.controller.CompanyController;
import hbv7d.controller.UserController;
import hbv7d.model.Company;
import hbv7d.repository.BookingRepository;
import hbv7d.repository.CompanyRepository;
import hbv7d.repository.TourRepository;
import hbv7d.repository.UserRepository;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        start();
    }

    private static void start(){
        //Býr til töflunnar ef þær eru ekki núþegar til
        CompanyRepository companyRepository = new CompanyRepository();
        TourRepository tourRepository = new TourRepository();
        UserRepository userRepository = new UserRepository();
        BookingRepository bookingRepository = new BookingRepository();
        CompanyController companyController = new CompanyController(companyRepository,tourRepository);
        UserController userController = new UserController();

        bookingRepository.createBookingTable();
        companyRepository.createCompanyTable();
        tourRepository.createTourTable();
        userRepository.createUserTable();

    }
}
