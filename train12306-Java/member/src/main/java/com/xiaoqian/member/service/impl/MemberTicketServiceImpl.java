package com.xiaoqian.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.domain.dto.MemberTicketDTO;
import com.xiaoqian.common.enums.SeatColEnum;
import com.xiaoqian.common.enums.SeatTypeEnum;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import com.xiaoqian.member.domain.pojo.MemberTicket;
import com.xiaoqian.member.domain.query.MemberTicketQueryDTO;
import com.xiaoqian.member.domain.vo.MemberTicketVo;
import com.xiaoqian.member.mapper.MemberTicketMapper;
import com.xiaoqian.member.service.IMemberTicketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 乘客购买的车票记录 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-05-03
 */
@Service
@Slf4j
public class MemberTicketServiceImpl extends ServiceImpl<MemberTicketMapper, MemberTicket> implements IMemberTicketService {

    @Override
    public ResponseResult<PageVo<MemberTicketVo>> listMemberTicketPage(MemberTicketQueryDTO query) {
        Page<MemberTicket> page = new Page<>(query.getPageNum(), query.getPageSize());
        page(page, new LambdaQueryWrapper<MemberTicket>()
                .eq(query.getMemberId() != null, MemberTicket::getMemberId, query.getMemberId())
                .eq(StringUtils.hasText(query.getTrainCode()), MemberTicket::getTrainCode, query.getTrainCode())
                .eq(query.getDate() != null, MemberTicket::getTrainDate, query.getDate()));
        List<MemberTicket> memberTicketList =page.getRecords();
        List<MemberTicketVo> memberTicketVoList = BeanUtil.copyToList(memberTicketList, MemberTicketVo.class);

        return ResponseResult.okResult(new PageVo<>(memberTicketVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> saveMemberTicket(MemberTicketDTO memberTicketDTO) {
        LocalDateTime now = LocalDateTime.now();
        MemberTicket memberTicket = new MemberTicket(SnowUtil.getSnowFlakeNextId(), memberTicketDTO.getMemberId(),
                memberTicketDTO.getPassengerId(), memberTicketDTO.getPassengerName(), memberTicketDTO.getTrainDate(),
                memberTicketDTO.getTrainCode(), memberTicketDTO.getCarriageIndex(), memberTicketDTO.getSeatRow(),
                SeatColEnum.fromCode(memberTicketDTO.getSeatCol()), memberTicketDTO.getStartStation(), memberTicketDTO.getStartTime(),
                memberTicketDTO.getEndStation(), memberTicketDTO.getEndTime(), SeatTypeEnum.fromCode(memberTicketDTO.getSeatType()),
                now, now);
        save(memberTicket);

        return ResponseResult.okEmptyResult();
    }
}
