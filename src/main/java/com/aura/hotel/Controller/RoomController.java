package com.aura.hotel.Controller;

import com.aura.hotel.entity.Room;
import com.aura.hotel.Service.RoomService;
import com.aura.hotel.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "房间管理接口", description = "提供房间的增删改查及房型获取")
@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Operation(summary = "查询酒店房间列表", description = "根据酒店ID查询所有房间，支持按房型过滤")
    @GetMapping("/list/{hotelId}")
    public Result<List<Room>> list(
            @Parameter(description = "酒店ID", required = true) @PathVariable Long hotelId,
            @Parameter(description = "房型名称（可选）") @RequestParam(required = false) String typeName) {
        return Result.success(roomService.getRoomsByHotel(hotelId, typeName));
    }

    @Operation(summary = "获取酒店所有房型", description = "返回该酒店下现有的所有房型分类名称（去重）")
    @GetMapping("/types/{hotelId}")
    public Result<List<String>> getTypes(
            @Parameter(description = "酒店ID", required = true) @PathVariable Long hotelId) {
        return Result.success(roomService.getHotelRoomTypes(hotelId));
    }

    @Operation(summary = "保存房间", description = "对象中包含id则为修改，不包含id则为新增")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody Room room) {
        if (room.getId() == null) {
            roomService.addRoom(room);
        } else {
            roomService.updateRoom(room);
        }
        return Result.success();
    }

    @Operation(summary = "删除房间", description = "根据物理ID删除具体房间")
    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @Parameter(description = "房间ID", required = true) @PathVariable Long id) {
        roomService.deleteRoom(id);
        return Result.success();
    }
}