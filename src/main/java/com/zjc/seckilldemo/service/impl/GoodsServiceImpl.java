package com.zjc.seckilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.mapper.GoodsMapper;
import com.zjc.seckilldemo.pojo.Goods;
import com.zjc.seckilldemo.service.IGoodsService;
import com.zjc.seckilldemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvaho
 * @since 2021-12-12
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    /**
     * 获取商品列表
     * @return
     */
    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    /**
     * 根据商品id获取商品详情
     * @param goodsId
     * @return
     */
    @Override
    public GoodsVo findGoodsVoByGoodsId(Integer goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }


}
