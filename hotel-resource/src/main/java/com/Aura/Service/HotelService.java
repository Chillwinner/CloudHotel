package com.Aura.Service;

import com.Aura.entity.Hotel;
import com.Aura.Mapper.HotelMapper;
import com.Aura.utils.CacheService;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private CacheService cache;

    public void addHotel(Hotel hotel) {
        hotelMapper.insert(hotel);
        cache.delPattern("hotel:list:*");
    }

    public void removeHotel(Long id) {
        hotelMapper.delete(id);
        cache.del("hotel:" + id);
        cache.delPattern("hotel:list:*");
    }

    public void modifyHotel(Hotel hotel) {
        hotelMapper.update(hotel);
        cache.del("hotel:" + hotel.getId());
        cache.delPattern("hotel:list:*");
    }

    public Hotel getHotel(Long id) {
        String key = "hotel:" + id;
        Hotel h = cache.get(key, Hotel.class);
        if (h != null && !cache.needRefresh(key)) return h;

        RLock lock = cache.getLock(key);
        lock.lock();
        try {
            if (cache.needRefresh(key)) {
                h = hotelMapper.selectById(id);
                cache.set(key, h, 3600);
            } else {
                h = cache.get(key, Hotel.class);
            }
            return h;
        } finally {
            lock.unlock();
        }
    }

    public List<Hotel> findHotels(String name, String city) {
        String key = "hotel:list:" + (city != null ? city : "") + ":" + (name != null ? name : "");
        List<Hotel> list = cache.getList(key, Hotel.class);
        if (list != null && !cache.needRefresh(key)) return list;

        RLock lock = cache.getLock(key);
        lock.lock();
        try {
            if (cache.needRefresh(key)) {
                list = hotelMapper.selectList(name, city);
                cache.set(key, list, 1800);
            } else {
                list = cache.getList(key, Hotel.class);
            }
            return list;
        } finally {
            lock.unlock();
        }
    }
}
