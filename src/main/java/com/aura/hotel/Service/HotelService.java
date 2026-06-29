package com.aura.hotel.Service;

import com.aura.hotel.entity.Hotel;
import com.aura.hotel.Mapper.HotelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelService {

    @Autowired
    private HotelMapper hotelMapper;

    public void addHotel(Hotel hotel) {
        hotelMapper.insert(hotel);
    }

    public void removeHotel(Long id) {
        hotelMapper.delete(id);
    }

    public void modifyHotel(Hotel hotel) {
        hotelMapper.update(hotel);
    }

    public Hotel getHotel(Long id) {
        return hotelMapper.selectById(id);
    }

    public List<Hotel> findHotels(String name, String city) {
        return hotelMapper.selectList(name, city);
    }
}