package com.zjc.seckilldemo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositVo {
    //充值金额
    private BigDecimal total = BigDecimal.ZERO;
    //充值账号的身份证
    private String identity;

}
