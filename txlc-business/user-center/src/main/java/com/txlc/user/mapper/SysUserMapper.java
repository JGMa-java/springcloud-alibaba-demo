package com.txlc.user.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txlc.common.model.SysUser;
import com.txlc.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户表 Mapper 接口
 *
 * @author zlt
 * @data 2018-10-29
 */
@Mapper
public interface SysUserMapper extends SuperMapper<SysUser> {
    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<SysUser> findList(Page<SysUser> page, @Param("u") Map<String, Object> params);
}
