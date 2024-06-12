package ch.fhnw.swc.mrs.model;

import ch.fhnw.swc.mrs.api.MRSServices;
import ch.fhnw.swc.mrs.data.SimpleMRSServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integration")
public class ITBill {
    private MRSServices service;

    @BeforeEach
    void setUp() {
        service = new SimpleMRSServices();
        service.createDB();
    }

    @Test
    void testGetStatement() {
        // Start capturing
        PrintStream previousSystemOut = System.out;
        OutputStream redirectedSystemOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(redirectedSystemOut));

        // Run what is supposed to output something
        service.createRental(5, 4, LocalDate.now());

        // Stop capturing
        System.setOut(previousSystemOut);

        // Use captured content
        String content = redirectedSystemOut.toString();

        // convert the statement string into an array of strings (line-by-line)
        // then make the assertions
        String[] lines = content.split("\n");
        assertEquals(7, lines.length);
        assertEquals("Statement", lines[0]);
        assertEquals("=========", lines[1]);
        assertEquals("for: Daniela Meyer", lines[2]);
        assertEquals("", lines[3]);
        assertEquals("Days   Price  Title", lines[4]);
        assertEquals("-------------------", lines[5]);
        assertEquals("   0    5.00  Eragon", lines[6]);
//        assertEquals("\r", lines[7]);
    }
}
