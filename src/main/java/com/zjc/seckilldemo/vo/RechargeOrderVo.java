package com.zjc.seckilldemo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargeOrderVo {
    private String out_trade_no;
    private String total_amount;
    private String identity;
    private String timestamp;
    private String status;
}
