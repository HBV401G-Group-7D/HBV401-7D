package hbv7d.main;

import hbv7d.api.Api;
import hbv7d.controller.CompanyController;
import hbv7d.controller.UserController;
import hbv7d.model.Company;
import hbv7d.model.Tour;
import hbv7d.repository.BookingRepository;
import hbv7d.repository.CompanyRepository;
import hbv7d.repository.TourRepository;
import hbv7d.repository.UserRepository;
import java.util.Date;

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

        bookingRepository.createBookingTable();
        companyRepository.createCompanyTable();
        tourRepository.createTourTable();
        userRepository.createUserTable();

        CompanyController companyController = new CompanyController(companyRepository,tourRepository);
        UserController userController = new UserController();
        Company company1 = new Company(1, "company1");
        companyController.createCompany(company1);
//        Company company1 = companyController.getCompany(1);
        Tour tour = new Tour(
                1,
                "Tour 1",
                "this is description",
                "this is a location",
                20,
                new Date(),
                20,
                32,
                "this is a difficultyRating",
                "this is a type",
                false,
                company1
        );
        companyController.addTour(1,tour);


        System.out.println(tour.getSeatsTaken());



    }
}
