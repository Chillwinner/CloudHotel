package com.Aura.Controller;

import com.Aura.entity.Hotel;
import com.Aura.Service.HotelService;
import com.Aura.common.Result;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "酒店管理")
@RestController
@SentinelResource("hotel-list-api")
@RequestMapping("/api/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Operation(summary = "新增酒店")
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Hotel hotel) {
        hotelService.addHotel(hotel);
        return Result.success();
    }

    @Operation(summary = "删除酒店")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        hotelService.removeHotel(id);
        return Result.success();
    }

    @Operation(summary = "修改酒店")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Hotel hotel) {
        hotelService.modifyHotel(hotel);
        return Result.success();
    }

    @Operation(summary = "酒店详情")
    @GetMapping("/{id}")
    public Result<Hotel> getOne(@PathVariable Long id) {
        return Result.success(hotelService.getHotel(id));
    }

    @Operation(summary = "酒店列表")
    @GetMapping("/list")
    public Result<List<Hotel>> list(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String city) {
        return Result.success(hotelService.findHotels(name, city));
    }
}