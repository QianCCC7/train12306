package com.xiaoqian.business.service.transaction;

import com.xiaoqian.business.domain.pojo.DailyTrainSeat;
import com.xiaoqian.business.domain.pojo.DailyTrainTicket;
import com.xiaoqian.business.mapper.DailyTrainSeatMapper;
import com.xiaoqian.business.service.IDailyTrainTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfirmOrderTransaction {
    private final DailyTrainSeatMapper dailyTrainSeatMapper;
    private final IDailyTrainTicketService dailyTrainTicketService;

    @Transactional
    public void afterConfirmOrder(List<DailyTrainSeat> finalTrainSeatList, DailyTrainTicket dailyTrainTicket, String seatType) {
        for (DailyTrainSeat dailyTrainSeat : finalTrainSeatList) {
            // 更新座位售卖情况
            DailyTrainSeat dailyTrainSeatForUpdate = new DailyTrainSeat();
            dailyTrainSeatForUpdate.setId(dailyTrainSeat.getId());
            dailyTrainSeatForUpdate.setSell(dailyTrainSeat.getSell());
            dailyTrainSeatForUpdate.setUpdateTime(LocalDateTime.now());
            dailyTrainSeatMapper.updateById(dailyTrainSeatForUpdate);

            // 更新余票数量
            // 例如总共10个站，现在需要购买 4-7的站
            // 原售：001000001 购买：000011100 售后：001011101 影响：XXX11111X 受影响的是和本次购买的区间有交集的区间
            // 逻辑当前购票为[A1,A2],那么和当前区间有交集的的票假设为[B1,B2]，那么应该满足 B1 < A2 && A1 < B2
            int startIndex = dailyTrainTicket.getStartIndex(), endIndex = dailyTrainTicket.getEndIndex();
            List<DailyTrainTicket> dailyTrainTicketList = dailyTrainTicketService.lambdaQuery()
                    .eq(DailyTrainTicket::getTrainCode, dailyTrainTicket.getTrainCode())
                    .eq(DailyTrainTicket::getDate, dailyTrainTicket.getDate())
                    .lt(DailyTrainTicket::getStartIndex, endIndex) // B1 < A2
                    .gt(DailyTrainTicket::getEndIndex, startIndex) // A1 < B2
                    .list();
            List<DailyTrainTicket> collect;
            switch (seatType) {
                case "1":
                    collect = dailyTrainTicketList.stream().map(el -> el.setYdz(el.getYdz() - 1)).collect(Collectors.toList());
                    dailyTrainTicketService.updateBatchById(collect);
                    break;
                case "2":
                    collect = dailyTrainTicketList.stream().map(el -> el.setEdz(el.getEdz() - 1)).collect(Collectors.toList());
                    dailyTrainTicketService.updateBatchById(collect);
                    break;
                case "3":
                    collect = dailyTrainTicketList.stream().map(el -> el.setRw(el.getRw() - 1)).collect(Collectors.toList());
                    dailyTrainTicketService.updateBatchById(collect);
                    break;
                case "4":
                    collect = dailyTrainTicketList.stream().map(el -> el.setYw(el.getYw() - 1)).collect(Collectors.toList());
                    dailyTrainTicketService.updateBatchById(collect);
                    break;
            }
        }

        // 增加购票记录
        // 更新订单状态
    }
}
