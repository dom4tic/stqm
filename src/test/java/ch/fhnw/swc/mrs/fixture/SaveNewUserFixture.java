package ch.fhnw.swc.mrs.fixture;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ch.fhnw.swc.mrs.data.SimpleMRSServices;
import fit.ActionFixture;

/**
 * Test the Fitness ActionFixture SaveNewUserFixture specified in the
 * test/fitnesse/MovieRentalSystem/content.txt file
 *
 */
public class SaveNewUserFixture extends ActionFixture {

    private SimpleMRSServices mrsServices = MRSServicesFactory.getInstance();

    private String surname;
    private String firstname;
    private LocalDate birthdate;

    public void surname(String surname) {
        this.surname = surname;
    }

    public void firstName(String firstname) {
        this.firstname = firstname;
    }

    public void birthdate(String birthdate) {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.birthdate = LocalDate.parse(birthdate, dtf);
    }

    public void save() {
        mrsServices.createUser(surname, firstname, birthdate);
    }
    /**
     * Counts the entries in the table.
     * @return how many entries are saved
     */
    public int countUsers() {
        return mrsServices.getAllUsers().size();
    }

}
