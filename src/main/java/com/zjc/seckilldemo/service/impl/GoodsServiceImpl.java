package com.zjc.seckilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.controller.SeckillController;
import com.zjc.seckilldemo.mapper.GoodsMapper;
import com.zjc.seckilldemo.pojo.Goods;
import com.zjc.seckilldemo.service.IGoodsService;
import com.zjc.seckilldemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

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
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillController seckillController;
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

    @Override
    public void reloadstock() {
        List<GoodsVo> list = findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<Integer, Boolean> EmptyStockMap = seckillController.getStockMap();
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        });
        System.out.println("库存刷新成功");
    }


}
