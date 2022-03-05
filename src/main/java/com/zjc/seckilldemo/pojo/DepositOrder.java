package com.zjc.seckilldemo.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author lvaho
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_deposit_order")
public class DepositOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    private String outTradeNo;

    private BigDecimal totalAmount;

    private String timestamp;

    private String identity;

    private String status;


}
