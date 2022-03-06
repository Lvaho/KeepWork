package com.zjc.seckilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.vo.UserCallVo;
import com.zjc.seckilldemo.vo.ViolationRecordVo;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvaho
 * @since 2021-11-24
 */
public interface UserMapper extends BaseMapper<User> {
    public User selectUserByIdentity(String identity);
    public List<ViolationRecordVo> findUserViolationRecordVoByUseridentity(String identity);
    public int recordScreenResult(UserCallVo userCallVo);
}