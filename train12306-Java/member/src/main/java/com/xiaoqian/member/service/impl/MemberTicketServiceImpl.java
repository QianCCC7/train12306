package com.xiaoqian.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.member.domain.pojo.MemberTicket;
import com.xiaoqian.member.domain.query.MemberTicketQueryDTO;
import com.xiaoqian.member.domain.vo.MemberTicketVo;
import com.xiaoqian.member.mapper.MemberTicketMapper;
import com.xiaoqian.member.service.IMemberTicketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
