package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.SkTokenDTO;
import com.xiaoqian.business.domain.pojo.SkToken;
import com.xiaoqian.business.domain.query.SkTokenQueryDTO;
import com.xiaoqian.business.domain.vo.SkTokenVo;
import com.xiaoqian.business.mapper.SkTokenMapper;
import com.xiaoqian.business.service.ISkTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 秒杀令牌 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-06-02
 */
@Service
public class SkTokenServiceImpl extends ServiceImpl<SkTokenMapper, SkToken> implements ISkTokenService {

    @Override
    public ResponseResult<Void> saveSkToken(SkTokenDTO skTokenDTO) {
        if (skTokenDTO.getId() == null) {
            SkToken skToken = BeanUtil.copyProperties(skTokenDTO, SkToken.class);
            skToken.setId(SnowUtil.getSnowFlakeNextId());
            LocalDateTime now = LocalDateTime.now();
            skToken.setUpdateTime(now);
            skToken.setCreateTime(now);
            save(skToken);
        } else {
            SkToken skToken = BeanUtil.copyProperties(skTokenDTO, SkToken.class);
            skToken.setUpdateTime(LocalDateTime.now());
            updateById(skToken);
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<SkTokenVo>> listSkTokenPage(SkTokenQueryDTO queryDTO) {
        Page<SkToken> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        page(page, new LambdaQueryWrapper<SkToken>()
                .orderByDesc(true, SkToken::getDate)
                .orderByDesc(true, SkToken::getTrainCode));
        List<SkToken> skTokenList = page.getRecords();
        List<SkTokenVo> skTokenVoList = BeanUtil.copyToList(skTokenList, SkTokenVo.class);

        return ResponseResult.okResult(new PageVo<>(skTokenVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }
}
