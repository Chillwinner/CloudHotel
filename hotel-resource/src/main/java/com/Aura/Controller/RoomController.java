package com.Aura.Controller;

import com.Aura.entity.Room;
import com.Aura.Service.RoomService;
import com.Aura.common.Result;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "房间管理")
@RestController
@SentinelResource("hotel-list-api")
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Operation(summary = "房间列表")
    @GetMapping("/list/{hotelId}")
    public Result<List<Room>> list(@PathVariable Long hotelId,
                                   @RequestParam(required = false) String typeName) {
        return Result.success(roomService.getRoomsByHotel(hotelId, typeName));
    }

    @Operation(summary = "房型列表")
    @GetMapping("/types/{hotelId}")
    public Result<List<String>> getTypes(@PathVariable Long hotelId) {
        return Result.success(roomService.getHotelRoomTypes(hotelId));
    }

    @Operation(summary = "保存房间")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody Room room) {
        if (room.getId() == null) {
            roomService.addRoom(room);
        } else {
            roomService.updateRoom(room);
        }
        return Result.success();
    }

    @Operation(summary = "删除房间")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return Result.success();
    }

    @Operation(summary = "查可用房")
    @GetMapping("/findAvailable")
    public Result<Room> findOneAvailable(Long hotelId,
                                         String typeName,
                                         String startDate,
                                         String endDate) {
        Room room = roomService.findOneAvailable(hotelId, typeName, startDate, endDate);
        return Result.success(room);
    }

    @Operation(summary = "锁定房间")
    @PutMapping("/lock/{id}")
    public Result<Void> lockRoomStatus(@PathVariable Long id) {
        roomService.lockRoom(id);
        return Result.success();
    }

    @Operation(summary = "释放房间")
    @PutMapping("/release/{id}")
    public Result<Void> releaseRoomStatus(@PathVariable Long id) {
        roomService.releaseRoom(id);
        return Result.success();
    }
}