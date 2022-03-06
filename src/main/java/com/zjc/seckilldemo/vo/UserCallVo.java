package com.zjc.seckilldemo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
//用户调用接口记录
public class UserCallVo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String user_identity;
    private Integer method_id;
    private Timestamp timestamp;
    private String result;
}
