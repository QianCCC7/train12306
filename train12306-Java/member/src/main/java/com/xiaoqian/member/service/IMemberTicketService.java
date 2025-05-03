package com.xiaoqian.member.service;

import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.domain.dto.MemberTicketDTO;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.member.domain.pojo.MemberTicket;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqian.member.domain.query.MemberTicketQueryDTO;
import com.xiaoqian.member.domain.vo.MemberTicketVo;

/**
 * <p>
 * 乘客购买的车票记录 服务类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-05-03
 */
public interface IMemberTicketService extends IService<MemberTicket> {

    ResponseResult<PageVo<MemberTicketVo>> listMemberTicketPage(MemberTicketQueryDTO query);

    ResponseResult<Void> saveMemberTicket(MemberTicketDTO memberTicketDTO);
}
