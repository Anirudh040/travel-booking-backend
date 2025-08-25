package com.examly.springapp.repository;

import com.examly.springapp.model.Reservation;
import com.examly.springapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByStartDateBetween(LocalDate from, LocalDate to);
}
