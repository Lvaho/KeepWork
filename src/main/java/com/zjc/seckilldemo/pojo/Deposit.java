package com.zjc.seckilldemo.pojo;

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
 * @since 2022-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_deposit")
public class Deposit implements Serializable {

    private static final long serialVersionUID = 1L;

    private String identity;

    private BigDecimal deposit;


}
