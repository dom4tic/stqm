package ch.fhnw.swc.mrs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class StatementTest {
    // possible approaches to test the abstract class Statement:
    // Concrete Subclass: Best for cases where you might reuse the subclass in multiple tests or when the subclassing
    // method provides additional clarity or functionality.
    // Mocking Framework: Ideal when the abstract class has complex dependencies or when you want to test interactions
    // with other objects without extensive setup.
    // Anonymous Class: Useful for quick tests or when you only need minimal implementations of abstract methods.
    
    private Statement s;
    private List<Rental> rentals;

    @BeforeEach
    public void setup() {
        Rental r1 = mock(Rental.class);
        Rental r2 = mock(Rental.class);
        Rental r3 = mock(Rental.class);

        rentals = new ArrayList<>(3);
        rentals.add(r1);
        rentals.add(r2);
        rentals.add(r3);
    }

    @Test
    public void testStatementCtor() {
        // option 1: Subclass
        s = new TestStatement("Muster", "Hans", rentals);
        assertEquals("Muster", s.getLastName());
        assertEquals("Hans",   s.getFirstName());
        assertEquals(3,        s.getRentals().size());

        // option 2: mocking
        // doesnt make sense at the state of the code right now, too much time would be spent

        // option 3: anonymous class
        // quickly implemented to show that it would work
        Statement statement = new Statement("Muster", "Hans", rentals) {
            @Override
            public String getStatement() {
                return null;
            }
        };

        assertEquals("Muster", statement.getLastName());
        assertEquals("Hans",   statement.getFirstName());
        assertEquals(3,        statement.getRentals().size());
    }

    @Test
    public void testInvalidFirstName() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestStatement("Muster", "Maximilian", rentals));

        assertThrows(IllegalArgumentException.class,
                () -> new Statement("Muster", "Maximilian", rentals) {
                    @Override
                    public String getStatement() {
                        return null;
                    }
                });
    }

    @Test
    public void testInvalidLastName() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestStatement("Mustermann", "Hans", rentals));

        assertThrows(IllegalArgumentException.class,
            () -> new Statement("Mustermann", "Hans", rentals) {
                @Override
                public String getStatement() {
                    return null;
                }
            });
    }

    @Test
    public void testInvalidRentals() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestStatement("Muster", "Hans", null));

        assertThrows(IllegalArgumentException.class,
            () -> new Statement("Muster", "Hans", null) {
                @Override
                public String getStatement() {
                    return null;
                }
            });
    }

    // Concrete subclass of Statement to test it
    public class TestStatement extends Statement {

        /**
         * Creates a statement object for a person (with the given name parameter) and a list of rentals.
         *
         * @param name      the family name.
         * @param firstName the first name.
         * @param rentals   a list of rentals to be billed.
         */
        public TestStatement(String name, String firstName, List<Rental> rentals) {
            super(name, firstName, rentals);
        }

        @Override
        public String getStatement() {
            return null;
        }
    }
}
