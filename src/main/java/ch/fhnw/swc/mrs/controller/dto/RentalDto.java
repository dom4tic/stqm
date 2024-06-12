package ch.fhnw.swc.mrs.controller.dto;

import ch.fhnw.swc.mrs.model.Movie;
import ch.fhnw.swc.mrs.model.User;

import java.time.LocalDate;

public class RentalDto {
    private long id;
    private Movie movie;
    private User user;
    private LocalDate rentalDate;

    public RentalDto(long id, Movie movie, User user, LocalDate rentalDate) {
        this.id = id;
        this.movie = movie;
        this.user = user;
        this.rentalDate = rentalDate;
    }

    public RentalDto(User user, Movie movie, LocalDate rentalDate) {
        this.movie = movie;
        this.user = user;
        this.rentalDate = rentalDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }
}
