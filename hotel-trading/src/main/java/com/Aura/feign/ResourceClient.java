package com.Aura.feign;

import com.Aura.common.Result;
import com.Aura.entity.Hotel;
import com.Aura.entity.Room;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "hotel-resource-service")
public interface ResourceClient {

    @GetMapping("/api/room/findAvailable") 
    Result<Room> findOneAvailableRoom(@RequestParam("hotelId") Long hotelId,
                                         @RequestParam("typeName") String typeName,
                                         @RequestParam("startDate") String startDate,
                                         @RequestParam("endDate") String endDate);

    @PutMapping("/api/room/lock/{id}")
    Result<Void> lockRoomStatus(@PathVariable("id") Long id);

    @PutMapping("/api/room/release/{id}")
    Result<Void> releaseRoomStatus(@PathVariable("id") Long id);

    @GetMapping("/api/hotel/list")
    Result<List<Hotel>> getHotelList(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "city", required = false) String city);

    @GetMapping("/api/room/list/{hotelId}")
    Result<List<Room>> getRoomList(@PathVariable("hotelId") Long hotelId,
                                   @RequestParam(value = "typeName", required = false) String typeName);

    @GetMapping("/api/hotel/{id}")
    Result<Hotel> getHotelById(@PathVariable("id") Long id);

    @GetMapping("/api/room/types/{hotelId}")
    Result<List<String>> getRoomTypes(@PathVariable("hotelId") Long hotelId);
}