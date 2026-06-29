package com.aura.hotel.Service;

import com.aura.hotel.entity.Room;
import com.aura.hotel.Mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    public void addRoom(Room room) { roomMapper.insertRoom(room); }

    public void updateRoom(Room room) { roomMapper.updateRoom(room); }

    public void deleteRoom(Long id) { roomMapper.deleteRoom(id); }

    public Room getRoomById(Long id) { return roomMapper.selectById(id); }

    public List<Room> getRoomsByHotel(Long hotelId, String typeName) {
        return roomMapper.selectByHotel(hotelId, typeName);
    }

    public List<String> getHotelRoomTypes(Long hotelId) {
        return roomMapper.selectTypeNamesByHotel(hotelId);
    }
}