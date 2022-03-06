package com.zjc.seckilldemo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
//用户调用接口记录
public class UserCallVo {

    private Integer id;
    private String user_identity;
    private String method_id;
    private Timestamp timestamp;
    private String result;
}
