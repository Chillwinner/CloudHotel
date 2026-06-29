package com.aura.hotel.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Schema(description = "酒店订单实体")
public class HotelOrder {
    @Schema(description = "订单主键ID")
    private Long id;

    @Schema(description = "唯一订单编号")
    private String orderNo;

    @Schema(description = "用户ID", defaultValue = "1")
    private Long userId = 1L;

    @Schema(description = "酒店ID")
    private Long hotelId;

    @Schema(description = "分配的房间ID")
    private Long roomId;

    @Schema(description = "入住日期")
    private LocalDate startDate;

    @Schema(description = "离店日期")
    private LocalDate endDate;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "订单状态：1-待入住, 2-已入住, 3-已完成, 0-已取消")
    private Integer status;

    @Schema(description = "下单时间")
    private Date createTime;
}