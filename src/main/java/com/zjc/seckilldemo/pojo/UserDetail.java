package com.zjc.seckilldemo.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lvaho
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user_detail")
public class UserDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String identity;

    private String overdue;

    private String workstate;

    private String defaulter;


}
