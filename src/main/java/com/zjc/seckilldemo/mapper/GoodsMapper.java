package com.zjc.seckilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.seckilldemo.pojo.Goods;
import com.zjc.seckilldemo.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvaho
 * @since 2021-12-12
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();
    /**
     * 根据商品id获取商品详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Integer goodsId);


}
