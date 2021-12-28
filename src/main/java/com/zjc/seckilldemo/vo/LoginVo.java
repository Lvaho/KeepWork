package com.zjc.seckilldemo.vo;

import com.zjc.seckilldemo.validation.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;
    @NotNull
    @Length(min = 32)
    private String password;

}
