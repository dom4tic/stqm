package ch.fhnw.swc.mrs.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BillTest {

    /**
     * Simple test to test if the Bill.getStatement method returns the correct statement
     * for a user with rentals. 
     *
     */
    @Test
    public void testGetStatement() {
        // create a user with a list of rentals
        Bill b = new Bill("Muster", "Hans", createRentalList());
        
        //execute the MUT
        String s = b.getStatement();
        
        // convert the statement string into an array of strings (line-by-line)
        // then make the assertions
        String[] lines = s.split("\n");
        assertEquals(9, lines.length);
        assertEquals("Statement", lines[0]);
        assertEquals("=========", lines[1]);
        assertEquals("for: Hans Muster", lines[2]);
        assertEquals("", lines[3]);
        assertEquals("Days   Price  Title", lines[4]);
        assertEquals("-------------------", lines[5]);
        assertEquals("   1    8.40  Avatar", lines[6]);
        assertEquals("   2   17.20  Casablanca", lines[7]);
        assertEquals("   3   26.40  Tron", lines[8]);
    }

    /**
     * Create test data for the test.
     * @return a list of rentals with test data
     */
    private List<Rental> createRentalList() {
        List<Rental> rentals = new ArrayList<>(3);
       
        Movie m1 = mock(Movie.class);
        Movie m2 = mock(Movie.class);
        Movie m3 = mock(Movie.class);
        when(m1.getTitle()).thenReturn("Avatar");
        when(m2.getTitle()).thenReturn("Casablanca");
        when(m3.getTitle()).thenReturn("Tron");
        
        Rental r1 = mock(Rental.class);
        Rental r2 = mock(Rental.class);
        Rental r3 = mock(Rental.class);
        when(r1.getMovie()).thenReturn(m1);
        when(r2.getMovie()).thenReturn(m2);
        when(r3.getMovie()).thenReturn(m3);
        when(r1.getRentalDays()).thenReturn(1);
        when(r2.getRentalDays()).thenReturn(2);
        when(r3.getRentalDays()).thenReturn(3);
        when(r1.getRentalFee()).thenReturn(8.4);
        when(r2.getRentalFee()).thenReturn(17.2);
        when(r3.getRentalFee()).thenReturn(26.4);
        
        rentals.add(r1);
        rentals.add(r2);
        rentals.add(r3);

        return rentals;
    }
    
}
