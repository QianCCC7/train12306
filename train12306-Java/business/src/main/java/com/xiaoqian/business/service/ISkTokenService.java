package com.xiaoqian.business.service;

import com.xiaoqian.business.domain.dto.SkTokenDTO;
import com.xiaoqian.business.domain.pojo.SkToken;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.business.domain.query.SkTokenQueryDTO;
import com.xiaoqian.business.domain.vo.SkTokenVo;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;

/**
 * <p>
 * 秒杀令牌 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-06-02
 */
public interface ISkTokenService extends IService<SkToken> {

    ResponseResult<Void> saveSkToken(SkTokenDTO skTokenDTO);

    ResponseResult<PageVo<SkTokenVo>> listSkTokenPage(SkTokenQueryDTO queryDTO);

    ResponseResult<Void> deleteById(Long id);
}
