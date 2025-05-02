package com.xiaoqian.business.service.transaction;

import com.xiaoqian.business.domain.pojo.DailyTrainSeat;
import com.xiaoqian.business.mapper.DailyTrainSeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmOrderTransaction {
    private final DailyTrainSeatMapper dailyTrainSeatMapper;

    @Transactional
    public void afterConfirmOrder(List<DailyTrainSeat> finalTrainSeatList) {
        for (DailyTrainSeat dailyTrainSeat : finalTrainSeatList) {
            // 更新座位售卖情况
            DailyTrainSeat dailyTrainSeatForUpdate = new DailyTrainSeat();
            dailyTrainSeatForUpdate.setId(dailyTrainSeat.getId());
            dailyTrainSeatForUpdate.setSell(dailyTrainSeat.getSell());
            dailyTrainSeatForUpdate.setUpdateTime(LocalDateTime.now());
            dailyTrainSeatMapper.updateById(dailyTrainSeatForUpdate);

        }

        // 更新余票数量
        // 增加购票记录
        // 更新订单状态
    }
}
