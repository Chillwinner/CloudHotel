package com.Aura.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Room  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long hotelId;
    private String roomNumber;
    private String typeName;
    private BigDecimal price;
    private Integer status; // 1-正常, 0-维修

    private String img1;
    private String img2;
    private String img3;
}