package com.aura.hotel.Mapper;

import com.aura.hotel.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoomMapper {
    // 增加房间
    int insertRoom(Room room);

    // 修改房间信息（比如调价或改状态）
    int updateRoom(Room room);

    // 删除房间
    int deleteRoom(Long id);

    // 根据ID查单间
    Room selectById(Long id);

    // 获取某个酒店的所有房间（可按房型过滤）
    List<Room> selectByHotel(Long hotelId, String typeName);

    // 获取某个酒店拥有的所有房型列表（用于前端筛选下拉框）
    List<String> selectTypeNamesByHotel(Long hotelId);
}