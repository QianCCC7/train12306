package com.xiaoqian.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.common.context.MemberContext;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.enums.HttpCodeEnum;
import com.xiaoqian.common.exception.BizException;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import com.xiaoqian.member.domain.dto.PassengerDTO;
import com.xiaoqian.member.domain.pojo.Passenger;
import com.xiaoqian.member.domain.query.PassengerQueryDTO;
import com.xiaoqian.member.domain.vo.PassengerVo;
import com.xiaoqian.member.mapper.PassengerMapper;
import com.xiaoqian.member.service.IPassengerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 乘车人 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-16
 */
@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger> implements IPassengerService {

    @Override
    public ResponseResult<Void> savePassenger(PassengerDTO passengerDTO) {
        Passenger passenger = BeanUtil.copyProperties(passengerDTO, Passenger.class);
        if (passengerDTO.getId() == null) {
            int size = getAllPassengers().getData().size();
            if (size >= 50) {
                throw new BizException(HttpCodeEnum.PASSENGERS_REACH_LIMIT);
            }
            passenger.setId(SnowUtil.getSnowFlakeNextId());
            passenger.setMemberId(MemberContext.getId());
            LocalDateTime now = LocalDateTime.now();
            passenger.setUpdateTime(now);
            passenger.setCreateTime(now);
            save(passenger);
        } else {
            passenger.setUpdateTime(LocalDateTime.now());
            updateById(passenger);
        }


        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<PassengerVo>> listPassengers(PassengerQueryDTO query) {
        Long memberId = query.getMemberId();
        Page<Passenger> page = new Page<>(query.getPageNum(), query.getPageSize());
        page(page, new LambdaQueryWrapper<Passenger>()
                .eq(memberId != null, Passenger::getMemberId, memberId)
                .orderByAsc(true, Passenger::getId));
        List<Passenger> passengerList =page.getRecords();
        List<PassengerVo> passengerVoList = BeanUtil.copyToList(passengerList, PassengerVo.class);

        return ResponseResult.okResult(new PageVo<>(passengerVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        removeById(id);
        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<List<PassengerVo>> getAllPassengers() {
        Long userId = MemberContext.getId();
        List<Passenger> passengerList = lambdaQuery().eq(Passenger::getMemberId, userId).list();

        return ResponseResult.okResult(BeanUtil.copyToList(passengerList, PassengerVo.class));
    }
}
