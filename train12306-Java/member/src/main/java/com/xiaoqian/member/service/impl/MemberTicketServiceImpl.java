package com.xiaoqian.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import org.springframework.stereotype.Service;

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
public class MemberTicketServiceImpl extends ServiceImpl<MemberTicketMapper, MemberTicket> implements IMemberTicketService {

    @Override
    public ResponseResult<PageVo<MemberTicketVo>> listMemberTicketPage(MemberTicketQueryDTO query) {
        Page<MemberTicket> page = new Page<>(query.getPageNum(), query.getPageSize());
        page(page);
        List<MemberTicket> memberTicketList =page.getRecords();
        List<MemberTicketVo> memberTicketVoList = BeanUtil.copyToList(memberTicketList, MemberTicketVo.class);

        return ResponseResult.okResult(new PageVo<>(memberTicketVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> saveMemberTicket(MemberTicketDTO memberTicketDTO) {
        MemberTicket memberTicket = BeanUtil.copyProperties(memberTicketDTO, MemberTicket.class);
        LocalDateTime now = LocalDateTime.now();
        SeatColEnum seatColEnum = SeatColEnum.fromCode(memberTicketDTO.getSeatCol());
        SeatTypeEnum seatTypeEnum = SeatTypeEnum.fromCode(memberTicketDTO.getSeatType());
        memberTicket.setSeatCol(seatColEnum);
        memberTicket.setSeatType(seatTypeEnum);
        memberTicket.setId(SnowUtil.getSnowFlakeNextId());
        memberTicket.setCreateTime(now);
        memberTicket.setUpdateTime(now);
        save(memberTicket);

        return ResponseResult.okEmptyResult();
    }
}
