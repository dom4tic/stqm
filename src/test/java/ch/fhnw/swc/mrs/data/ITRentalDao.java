package ch.fhnw.swc.mrs.data;

import ch.fhnw.swc.mrs.model.Movie;
import ch.fhnw.swc.mrs.model.Rental;
import ch.fhnw.swc.mrs.model.User;
import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.hsqldb.jdbc.JDBCDataSourceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Properties;

import static org.assertj.db.api.Assertions.assertThat;

public class ITRentalDao extends AbstractITDao {
    /** Class under test: RentalDao. */
    private RentalDAO dao;

    private Rental indianaJonesForDagobert, shawshankRedemptionForMickey, shutterIslandForDonald;
    private User donald, dagobert, mickey;
    private Movie indianaJones, shutterIsland, shawshankRedemption;

    private Source src;
    private Properties props = new Properties();

    ITRentalDao() {
        props.setProperty("url", "jdbc:hsqldb:mem:mrs");
        props.setProperty("user", "sa");
        props.setProperty("password", "");
        props.setProperty("jdbc.driver", "org.hsqldb.jdbcDriver");
    }

    @BeforeEach
    void setUp() {
        // create the users used for the rental tests
        UserDAO userDao = new UserDAO(getEMF().createEntityManager());

        donald   = new User("Duck",  "Donald",   LocalDate.of(2001,  1, 13));
        dagobert = new User("Duck",  "Dagobert", LocalDate.of(1945,  9,  9));
        mickey   = new User("Mouse", "Mickey",   LocalDate.of(1935, 11,  3));

        userDao.saveOrUpdate(donald);
        userDao.saveOrUpdate(dagobert);
        userDao.saveOrUpdate(mickey);

        // create the movies used for the rental tests
        MovieDAO movieDao = new MovieDAO(getEMF().createEntityManager());

        indianaJones        = new Movie("Raiders of the Lost Ark",  LocalDate.of(1981, 6, 12),  13);
        shutterIsland       = new Movie("Shutter Island",           LocalDate.of(2010, 2, 13),  17);
        shawshankRedemption = new Movie("Shawshank Redemption",     LocalDate.of(1994, 10, 14), 17);

        movieDao.saveOrUpdate(indianaJones);
        movieDao.saveOrUpdate(shutterIsland);
        movieDao.saveOrUpdate(shawshankRedemption);

        // create the rentals
        dao = new RentalDAO(getEMF().createEntityManager());

        shawshankRedemptionForMickey = new Rental(mickey,   shawshankRedemption, LocalDate.of(2021, 12, 28));
        shutterIslandForDonald       = new Rental(donald,   shutterIsland,       LocalDate.of(2021, 12, 29));
        indianaJonesForDagobert      = new Rental(dagobert, indianaJones,        LocalDate.of(2024,  6,  6));

        dao.save(indianaJonesForDagobert);
        dao.save(shawshankRedemptionForMickey);
        dao.save(shutterIslandForDonald);

        src = new Source("jdbc:hsqldb:mem:mrs", "sa", "");
    }

    // Possible tests:
    // Test rental creation with a user that does not exist
    // Test rental creation with a movie that does not exist
    // Test rental with user under age (could also be done as unit-test)

    @Test
    void testInsert() throws Exception {
        Table table = new Table(src, "rentals", new Table.Order[] {Table.Order.asc("RentalDate")});
        assertThat(table).hasNumberOfRows(3);

        assertThat(table).column("MovieId")
                .value().isEqualTo(shawshankRedemption.getMovieid())
                .value().isEqualTo(shutterIsland      .getMovieid())
                .value().isEqualTo(indianaJones       .getMovieid());

        assertThat(table).column("UserId")
                .value().isEqualTo(mickey  .getUserid())
                .value().isEqualTo(donald  .getUserid())
                .value().isEqualTo(dagobert.getUserid());

        assertThat(table).column("RentalDate")
                .value().isEqualTo(LocalDate.of(2021, 12, 28))
                .value().isEqualTo(LocalDate.of(2021, 12, 29))
                .value().isEqualTo(LocalDate.of(2024,  6,  6));
    }

    @AfterEach
    void tearDown() throws Exception {
        DataSource ds = JDBCDataSourceFactory.createDataSource(props);
        Connection conn = ds.getConnection();
        conn.createStatement().executeUpdate("delete from rentals");
        conn.createStatement().executeUpdate("delete from users");
        conn.createStatement().executeUpdate("delete from movies");
        conn.close();
    }
}
