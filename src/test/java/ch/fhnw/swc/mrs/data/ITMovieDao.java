package ch.fhnw.swc.mrs.data;

import ch.fhnw.swc.mrs.model.Movie;
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

public class ITMovieDao extends AbstractITDao {

    /** Class under test: MovieDAO. */
    private MovieDAO dao;

    private Movie indianaJones, shutterIsland, shawshankRedemption;

    private Source src;
    private Properties props = new Properties();

    ITMovieDao() {
        props.setProperty("url", "jdbc:hsqldb:mem:mrs");
        props.setProperty("user", "sa");
        props.setProperty("password", "");
        props.setProperty("jdbc.driver", "org.hsqldb.jdbcDriver");
    }

    @BeforeEach
    void setUp() {
        dao = new MovieDAO(getEMF().createEntityManager());

        indianaJones        = new Movie("Raiders of the Lost Ark",  LocalDate.of(1981, 6, 12),  13);
        shutterIsland       = new Movie("Shutter Island",           LocalDate.of(2010, 2, 13),  17);
        shawshankRedemption = new Movie("Shawshank Redemption",     LocalDate.of(1994, 10, 14), 17);

        dao.saveOrUpdate(indianaJones);
        dao.saveOrUpdate(shutterIsland);
        dao.saveOrUpdate(shawshankRedemption);

        src = new Source("jdbc:hsqldb:mem:mrs", "sa", "");
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

    // Possible test:
    // test Rented column after insert

    @Test
    void testInsert() {
        Table table = new Table(src, "movies", new Table.Order[] {Table.Order.asc("ReleaseDate")});
        assertThat(table).hasNumberOfRows(3);

        assertThat(table).column("Title")
                .value().isEqualTo("Raiders of the Lost Ark")
                .value().isEqualTo("Shawshank Redemption")
                .value().isEqualTo("Shutter Island");

        assertThat(table).column("ReleaseDate")
                .value().isEqualTo(LocalDate.of(1981, 6,  12))
                .value().isEqualTo(LocalDate.of(1994, 10, 14))
                .value().isEqualTo(LocalDate.of(2010, 2,  13));

        assertThat(table).column("AgeRating")
                .value().isEqualTo(13)
                .value().isEqualTo(17)
                .value().isEqualTo(17);
    }
}
