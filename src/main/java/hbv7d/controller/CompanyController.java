package hbv7d.controller;

import hbv7d.repository.*;
import hbv7d.model.Company;
import hbv7d.model.Tour;
import java.util.List;

/**
 * The CompanyController class is responsible for handling operations
 * related to Company objects and their associated Tours.
 */
public class CompanyController {
    private CompanyRepository companyRepository; // Repository for managing companies.
    private TourRepository tourRepository;       // Repository for managing tours.

    /**
     * Constructs a CompanyController with the specified repositories.
     *
     * @param companyRepository the repository for company operations
     * @param tourRepository the repository for tour operations
     */
    public CompanyController(CompanyRepository companyRepository, TourRepository tourRepository) {
        this.companyRepository = companyRepository;
        this.tourRepository = tourRepository;
    }

    /**
     * Default constructor. It initializes the controller with new instances of
     * CompanyRepository and TourRepository.
     */
    public CompanyController(){
        this.companyRepository = new CompanyRepository();
        this.tourRepository = new TourRepository();
    }
    
    /**
     * Creates a new company if one with the same ID does not already exist.
     *
     * @param company the Company object to be saved
     * @return true if the company was successfully created, false otherwise
     */
    public boolean createCompany(Company company) {
        if (companyRepository.findById(company.getCompanyId()) == null) {
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a Company by its ID.
     *
     * @param companyId the ID of the company to retrieve
     * @return the Company object if found; otherwise, null
     */
    public Company getCompany(int companyId) {
        return companyRepository.findById(companyId);
    }

    /**
     * Deletes a Company identified by the given ID.
     *
     * @param companyId the ID of the company to delete
     * @return true if the company was found and deleted; false otherwise
     */
    public boolean deleteCompany(int companyId) {
        Company company = companyRepository.findById(companyId);
        if (company != null) {
            companyRepository.delete(companyId);
            return true;
        }
        return false;
    }

    /**
     * Adds a Tour for a specified Company.
     * The company is retrieved and set as the host of the tour before saving.
     *
     * @param companyId the ID of the company under which the tour is to be added
     * @param tour the Tour object to add
     * @return true if the company exists and the tour is successfully added; false otherwise
     */
    public boolean addTour(int companyId, Tour tour) {
        Company company = companyRepository.findById(companyId);
        if (company != null) {
            tour.setHost(company);
            tourRepository.save(tour);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a list of Tours associated with the specified Company.
     *
     * @param companyId the ID of the company whose tours are to be viewed
     * @return a List of Tour objects; if no tours exist, the list may be empty
     */
    public List<Tour> viewCompanyTours(int companyId) {
        return tourRepository.findByCompanyId(companyId);
    }


    /**
     * Retrieves a list of all tours available in the database.
     *
     * @return a List of Tour objects representing all available tours.
     */
    public List<Tour> viewAllATours() {
        return tourRepository.findAllTours();
    }
}
