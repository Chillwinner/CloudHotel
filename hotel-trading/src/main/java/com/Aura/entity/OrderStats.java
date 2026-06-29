package com.Aura.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderStats {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Long finishCount;
    private Long cancelCount;
}
