package com.examly.springapp.repository;

import com.examly.springapp.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FlightRepository extends JpaRepository<Flight, Long> {}
