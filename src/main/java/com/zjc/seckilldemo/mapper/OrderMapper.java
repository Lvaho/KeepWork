package com.zjc.seckilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.seckilldemo.pojo.Order;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvaho
 * @since 2021-12-12
 */
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT * FROM t_order where status = 1 FOR UPDATE")
    public List<Order> selectallpayedorder();
}
