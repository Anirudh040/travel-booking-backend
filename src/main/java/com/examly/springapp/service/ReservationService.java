package com.examly.springapp.service;

import com.examly.springapp.dto.BookingRequestDTO;
import com.examly.springapp.dto.ReservationResponseDTO;
import com.examly.springapp.exception.ApiException;
import com.examly.springapp.model.*;
import com.examly.springapp.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepo;
    private final FlightRepository flightRepo;
    private final HotelRepository hotelRepo;
    private final UserRepository userRepo;
    private final NotificationService notifier;

    @Value("${app.booking.fee:50}")
    private BigDecimal bookingFee;

    public ReservationService(ReservationRepository reservationRepo, FlightRepository flightRepo, HotelRepository hotelRepo,
                              UserRepository userRepo, NotificationService notifier){
        this.reservationRepo = reservationRepo; this.flightRepo = flightRepo; this.hotelRepo = hotelRepo;
        this.userRepo = userRepo; this.notifier = notifier;
    }

    public ReservationResponseDTO book(String email, BookingRequestDTO request){
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ApiException("User not found"));
        if(!user.isActive()) throw new ApiException("Account deactivated");
        if(request.getSeatsOrRooms() == null || request.getSeatsOrRooms() <= 0) throw new ApiException("Invalid seats/rooms");

        Reservation res = new Reservation();
        res.setUser(user);
        res.setType(request.getBookingType());
        res.setSeatsOrRooms(request.getSeatsOrRooms());
        res.setStartDate(request.getStartDate());
        res.setEndDate(request.getEndDate());
        res.setStatus(ReservationStatus.PENDING);

        if(request.getBookingType() == ReservationType.FLIGHT){
            Flight flight = flightRepo.findById(request.getReferenceId()).orElseThrow(() -> new ApiException("Flight not found"));
            if(flight.getAvailableSeats() < request.getSeatsOrRooms()) throw new ApiException("Not enough seats");
            flight.setAvailableSeats(flight.getAvailableSeats() - request.getSeatsOrRooms());
            res.setFlight(flight);
            res.setTotalPrice(flight.getPrice().multiply(BigDecimal.valueOf(request.getSeatsOrRooms())).add(bookingFee));
            flightRepo.save(flight);
        } else if(request.getBookingType() == ReservationType.HOTEL){
            Hotel hotel = hotelRepo.findById(request.getReferenceId()).orElseThrow(() -> new ApiException("Hotel not found"));
            if(hotel.getAvailableRooms() < request.getSeatsOrRooms()) throw new ApiException("Not enough rooms");
            hotel.setAvailableRooms(hotel.getAvailableRooms() - request.getSeatsOrRooms());
            res.setHotel(hotel);
            res.setTotalPrice(hotel.getPricePerNight().multiply(BigDecimal.valueOf(request.getSeatsOrRooms())).add(bookingFee));
            hotelRepo.save(hotel);
        } else {
            throw new ApiException("Unknown booking type");
        }

        Reservation saved = reservationRepo.save(res);
        notifier.notify(user, "Booking created: #" + saved.getId() + " (" + saved.getType() + ")");
        return toDto(saved);
    }

    public List<Reservation> forUser(User user){ return reservationRepo.findByUser(user); }

    public Reservation cancel(Long id, String email){
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ApiException("User not found"));
        Reservation res = reservationRepo.findById(id).orElseThrow(() -> new ApiException("Reservation not found"));
        if(!res.getUser().getId().equals(user.getId())) throw new ApiException("Not your reservation");
        if(res.getStatus() == ReservationStatus.CANCELED) return res;
        res.setStatus(ReservationStatus.CANCELED);

        if(res.getType()==ReservationType.FLIGHT && res.getFlight()!=null){
            Flight f = res.getFlight();
            f.setAvailableSeats(f.getAvailableSeats() + res.getSeatsOrRooms());
            flightRepo.save(f);
        } else if(res.getType()==ReservationType.HOTEL && res.getHotel()!=null){
            Hotel h = res.getHotel();
            h.setAvailableRooms(h.getAvailableRooms() + res.getSeatsOrRooms());
            hotelRepo.save(h);
        }
        Reservation saved = reservationRepo.save(res);
        notifier.notify(user, "Booking canceled: #" + saved.getId());
        return saved;
    }

    public Reservation adminUpdateStatus(Long id, ReservationStatus status){
        Reservation res = reservationRepo.findById(id).orElseThrow(() -> new ApiException("Reservation not found"));
        res.setStatus(status);
        Reservation saved = reservationRepo.save(res);
        notifier.notify(res.getUser(), "Booking " + saved.getId() + " status changed to " + status);
        return saved;
    }

    private ReservationResponseDTO toDto(Reservation r){
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.id = r.getId();
        dto.type = r.getType();
        dto.status = r.getStatus();
        dto.seatsOrRooms = r.getSeatsOrRooms();
        dto.totalPrice = r.getTotalPrice();
        dto.startDate = r.getStartDate();
        dto.endDate = r.getEndDate();
        dto.flightId = r.getFlight() != null ? r.getFlight().getId() : null;
        dto.hotelId = r.getHotel() != null ? r.getHotel().getId() : null;
        dto.summary = "Reservation #" + r.getId() + " - " + r.getType() + " - " + r.getStatus();
        return dto;
    }
}
