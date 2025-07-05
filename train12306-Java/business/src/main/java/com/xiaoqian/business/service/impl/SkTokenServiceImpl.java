package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.SkTokenDTO;
import com.xiaoqian.business.domain.pojo.SkToken;
import com.xiaoqian.business.domain.query.SkTokenQueryDTO;
import com.xiaoqian.business.domain.vo.SkTokenVo;
import com.xiaoqian.business.mapper.SkTokenMapper;
import com.xiaoqian.business.service.IDailyTrainSeatService;
import com.xiaoqian.business.service.IDailyTrainStationService;
import com.xiaoqian.business.service.ISkTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.enums.HttpCodeEnum;
import com.xiaoqian.common.enums.RedisKeyPreEnum;
import com.xiaoqian.common.exception.BizException;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 秒杀令牌 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-06-02
 */
@Service
@RequiredArgsConstructor
public class SkTokenServiceImpl extends ServiceImpl<SkTokenMapper, SkToken> implements ISkTokenService {
    private final IDailyTrainStationService dailyTrainStationService;
    private final IDailyTrainSeatService dailyTrainSeatService;
    private final SkTokenMapper skTokenMapper;
    private final StringRedisTemplate redisTemplate;

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

    @Override
    public void generateDailyStToken(String trainCode, LocalDate date) {
        // 删除date天令牌余量
        remove(new LambdaQueryWrapper<SkToken>()
                .eq(SkToken::getTrainCode, trainCode)
                .eq(SkToken::getDate, date));

        SkToken skToken = new SkToken();
        skToken.setId(SnowUtil.getSnowFlakeNextId());
        skToken.setDate(date);
        skToken.setTrainCode(trainCode);
        LocalDateTime now = LocalDateTime.now();
        skToken.setCreateTime(now);
        skToken.setUpdateTime(now);

        int stationCount = dailyTrainStationService.getStationCountByCodeAndDate(trainCode, date);
        int seatCount = dailyTrainSeatService.getSeatCountByCodeAndDateAndSeatType(trainCode, date, null);
        // 令牌余量最大为：车座的数量 * 车站数量
        skToken.setCount(stationCount * seatCount);
        save(skToken);
    }

    @Override
    public boolean checkSkToken(String trainCode, LocalDate date, Long memberId) {
        // 防止机器人刷票
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String robotKey = RedisKeyPreEnum.SK_TOKEN.getCode() + formatter.format(date) + "-" + trainCode + "-" + memberId;
        Boolean ok = redisTemplate.opsForValue().setIfAbsent(robotKey, robotKey, 5, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(ok)) {
            throw new BizException(HttpCodeEnum.SK_TOKEN_GET_LOCK_FAIL);
        }
        // 利用redis缓存优化减少令牌余量逻辑
        String countKey = RedisKeyPreEnum.SK_TOKEN_COUNT.getCode() + formatter.format(date) + "-" + trainCode + "-" + memberId;
        String countValue = redisTemplate.opsForValue().get(countKey);
        if (StringUtils.hasText(countValue)) {
            Long curCount = redisTemplate.opsForValue().decrement(countKey, 1);
            redisTemplate.expire(countKey, 1, TimeUnit.MINUTES);
            if (curCount != null && curCount >= 0) {
                // 每减少五次令牌更新一次数据库
                if (curCount % 5 == 0) {
                    return skTokenMapper.decrease(trainCode, date, 5) > 0;
                }
                return true;
            } else {
                return false;
            }
        } else {
            SkToken skToken = lambdaQuery().eq(SkToken::getDate, date).eq(SkToken::getTrainCode, trainCode).one();
            int count = skToken.getCount();
            if (count <= 0) return false;
            redisTemplate.opsForValue().set(countKey, String.valueOf(count - 1), 1, TimeUnit.MINUTES);
            return true;
        }
    }

}
