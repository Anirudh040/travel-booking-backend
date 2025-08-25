package com.examly.springapp.service;

import com.examly.springapp.dto.SearchDtos;
import com.examly.springapp.exception.ApiException;
import com.examly.springapp.model.Hotel;
import com.examly.springapp.repository.HotelRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelService {
    private final HotelRepository repo;
    public HotelService(HotelRepository repo){ this.repo = repo; }

    public List<Hotel> search(SearchDtos.HotelSearch q){
        List<Hotel> all = repo.findAll();
        return all.stream().filter(h -> {
            boolean ok = true;
            if(q.location != null && !q.location.isBlank()) ok &= h.getLocation().equalsIgnoreCase(q.location);
            if(q.hotelName != null && !q.hotelName.isBlank()) ok &= h.getName().equalsIgnoreCase(q.hotelName);
            if(q.maxPrice != null) ok &= h.getPricePerNight().intValue() <= q.maxPrice;
            return ok;
        }).toList();
    }

    public Hotel get(Long id){ return repo.findById(id).orElseThrow(() -> new ApiException("Hotel not found")); }
    public Hotel create(Hotel h){ return repo.save(h); }
    public Hotel update(Long id, Hotel h){
        Hotel e = get(id);
        e.setName(h.getName());
        e.setLocation(h.getLocation());
        e.setPricePerNight(h.getPricePerNight());
        e.setAmenities(h.getAmenities());
        e.setAvailableRooms(h.getAvailableRooms());
        return repo.save(e);
    }
    public void delete(Long id){ repo.deleteById(id); }
}
