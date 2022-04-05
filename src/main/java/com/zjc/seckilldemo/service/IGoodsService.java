package com.zjc.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.zjc.seckilldemo.pojo.Goods;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.vo.GoodsVo;
import com.zjc.seckilldemo.vo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvaho
 * @since 2021-12-12
 */
public interface IGoodsService extends IService<Goods> {
    /**
     * 获取商品列表(筛选)
     * @return
     */
    List<GoodsVo> findGoodsVo(User user);
    /**
     * 根据商品id获取商品详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Integer goodsId);

    /**
     * 重载缓存
     */
    void reloadstock();

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();
}
