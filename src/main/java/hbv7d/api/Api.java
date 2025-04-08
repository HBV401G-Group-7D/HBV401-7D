package hbv7d.api;

import hbv7d.controller.CompanyController;
import hbv7d.controller.UserController;
import hbv7d.model.Company;
import hbv7d.model.Tour;
import hbv7d.repository.CompanyRepository;
import hbv7d.repository.TourRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

//TODO think about if we should split this up in companyapi and user api or just use them for the ui
public class Api {
    CompanyController companyController;
    UserController userController;
    public Api(){
        userController = new UserController();
        start();

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
    }

    private void start(){
        //Býr til töflunnar ef þær eru ekki núþegar til
        CompanyRepository companyRepository = new CompanyRepository();
        TourRepository tourRepository = new TourRepository();
        companyController = new CompanyController(companyRepository,tourRepository);
        companyRepository.createCompanyTable();
        tourRepository.createTourTable();
    }

    public boolean createCompany(Company company){
        return companyController.createCompany(company);
    }

    public Company getCompany(int companyId) {
        return companyController.getCompany(companyId);
    }

    public boolean deleteCompany(int companyId){
        return companyController.deleteCompany(companyId);
    }

    public boolean addTour(int companyId, Tour tour){
        return companyController.addTour(companyId,tour);
    }

    public List<Tour> viewCompanyTours(int companyId){
        return companyController.viewCompanyTours(companyId);
    }


}
