package com.aura.hotel.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Room {
    private Long id;
    private Long hotelId;
    private String roomNumber;
    private String typeName; // 核心：房型（标准间、总统套房等）
    private BigDecimal price;
    private Integer status; // 1-正常, 0-维修
}