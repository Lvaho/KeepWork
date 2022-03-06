package com.zjc.seckilldemo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//用户违规记录
public class ViolationRecordVo {
    private Integer id;
    private String user_identity;
    private Integer screen_id;
    private String description;
}
