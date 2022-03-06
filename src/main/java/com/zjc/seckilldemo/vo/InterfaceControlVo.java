package com.zjc.seckilldemo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//接口权限控制
public class InterfaceControlVo {
    private Integer id;
    private Integer screen_id;
    private Integer method_id;
}
