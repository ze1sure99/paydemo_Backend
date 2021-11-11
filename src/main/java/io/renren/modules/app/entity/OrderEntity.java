package io.renren.modules.app.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("tb_order")
public class OrderEntity implements Serializable {
    @TableId
    private  Integer id;
    private String code;
    private Integer userId;
    private BigDecimal amount;
    private Integer paymentType;
    private Integer status;
    private Data createTime;
}
