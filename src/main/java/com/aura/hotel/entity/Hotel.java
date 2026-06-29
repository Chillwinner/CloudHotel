package com.aura.hotel.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "酒店信息实体")
public class Hotel {
    @Schema(description = "酒店ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(description = "酒店名称", example = "Aura国际酒店")
    private String name;

    @Schema(description = "所属城市", example = "北京") // 新增字段
    private String city;
    
    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "酒店简介", example = "坐落于繁华的王府井大街，尽享故宫美景...")
    private String description;
    
    @Schema(description = "前台联系电话")
    private String phone;
    
    @Schema(description = "星级 (1-5)")
    private Integer starLevel;
    
    @Schema(description = "状态: 1-营业, 0-停业")
    private Integer status;
    
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;
}