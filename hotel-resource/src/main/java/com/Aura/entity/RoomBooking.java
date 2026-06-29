package com.Aura.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RoomBooking {
    private Long id;
    private Long roomId;
    private Long userId;
    private String userName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status; // 1-有效, 0-取消
}