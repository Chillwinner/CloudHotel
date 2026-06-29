package com.Aura.Service;

import com.Aura.entity.Room;
import com.Aura.Mapper.RoomMapper;
import com.Aura.utils.CacheService;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private CacheService cache;

    public void addRoom(Room room) {
        roomMapper.insertRoom(room);
        cache.delPattern("room:list:" + room.getHotelId() + ":*");
        cache.del("room:types:" + room.getHotelId());
    }

    public void updateRoom(Room room) {
        roomMapper.updateRoom(room);
        cache.del("room:" + room.getId());
        cache.delPattern("room:list:" + room.getHotelId() + ":*");
        cache.del("room:types:" + room.getHotelId());
    }

    public void deleteRoom(Long id) {
        roomMapper.deleteRoom(id);
        cache.del("room:" + id);
        cache.delPattern("room:list:*");
        cache.delPattern("room:types:*");
    }

    public Room getRoomById(Long id) {
        String key = "room:" + id;
        Room r = cache.get(key, Room.class);
        if (r != null && !cache.needRefresh(key)) return r;

        RLock lock = cache.getLock(key);
        lock.lock();
        try {
            if (cache.needRefresh(key)) {
                r = roomMapper.selectById(id);
                cache.set(key, r, 3600);
            } else {
                r = cache.get(key, Room.class);
            }
            return r;
        } finally {
            lock.unlock();
        }
    }

    public List<Room> getRoomsByHotel(Long hotelId, String typeName) {
        String key = "room:list:" + hotelId + ":" + (typeName != null ? typeName : "");
        List<Room> list = cache.getList(key, Room.class);
        if (list != null && !cache.needRefresh(key)) return list;

        RLock lock = cache.getLock(key);
        lock.lock();
        try {
            if (cache.needRefresh(key)) {
                list = roomMapper.selectByHotel(hotelId, typeName);
                cache.set(key, list, 1800);
            } else {
                list = cache.getList(key, Room.class);
            }
            return list;
        } finally {
            lock.unlock();
        }
    }

    public List<String> getHotelRoomTypes(Long hotelId) {
        String key = "room:types:" + hotelId;
        List<String> list = cache.getList(key, String.class);
        if (list != null && !cache.needRefresh(key)) return list;

        RLock lock = cache.getLock(key);
        lock.lock();
        try {
            if (cache.needRefresh(key)) {
                list = roomMapper.selectTypeNamesByHotel(hotelId);
                cache.set(key, list, 3600);
            } else {
                list = cache.getList(key, String.class);
            }
            return list;
        } finally {
            lock.unlock();
        }
    }

    public Room findOneAvailable(Long hotelId, String typeName, String startDate, String endDate) {
        return roomMapper.findOneAvailableRoom(hotelId, typeName, startDate, endDate);
    }

    public void lockRoom(Long id) {
        roomMapper.updateStatusById(id, 2);
    }

    public void releaseRoom(Long id) {
        roomMapper.updateStatusById(id, 1);
    }
}
