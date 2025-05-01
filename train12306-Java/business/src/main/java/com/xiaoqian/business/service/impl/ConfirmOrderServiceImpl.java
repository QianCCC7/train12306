package com.xiaoqian.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoqian.business.domain.dto.ConfirmOrderDTO;
import com.xiaoqian.business.domain.dto.PassengerTicketsDTO;
import com.xiaoqian.business.domain.pojo.ConfirmOrder;
import com.xiaoqian.business.domain.pojo.DailyTrainCarriage;
import com.xiaoqian.business.domain.pojo.DailyTrainSeat;
import com.xiaoqian.business.domain.pojo.DailyTrainTicket;
import com.xiaoqian.business.domain.query.ConfirmOrderQueryDTO;
import com.xiaoqian.business.domain.vo.ConfirmOrderVo;
import com.xiaoqian.business.enums.ConfirmOrderStatusEnum;
import com.xiaoqian.business.enums.SeatColEnum;
import com.xiaoqian.business.enums.SeatTypeEnum;
import com.xiaoqian.business.mapper.ConfirmOrderMapper;
import com.xiaoqian.business.service.IConfirmOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqian.business.service.IDailyTrainCarriageService;
import com.xiaoqian.business.service.IDailyTrainSeatService;
import com.xiaoqian.business.service.IDailyTrainTicketService;
import com.xiaoqian.common.context.MemberContext;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.enums.HttpCodeEnum;
import com.xiaoqian.common.exception.BizException;
import com.xiaoqian.common.query.PageVo;
import com.xiaoqian.common.utils.SnowUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 确认订单 服务实现类
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-30
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConfirmOrderServiceImpl extends ServiceImpl<ConfirmOrderMapper, ConfirmOrder> implements IConfirmOrderService {
    private final IDailyTrainTicketService dailyTrainTicketService;
    private final IDailyTrainCarriageService dailyTrainCarriageService;
    private final IDailyTrainSeatService dailyTrainSeatService;

    @Override
    public ResponseResult<Void> saveOrder(ConfirmOrderDTO confirmOrderDTO) {
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(confirmOrderDTO, ConfirmOrder.class);
        LocalDateTime now = LocalDateTime.now();
        if (confirmOrderDTO.getId() == null) {
            confirmOrder.setId(SnowUtil.getSnowFlakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            save(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            updateById(confirmOrder);
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<PageVo<ConfirmOrderVo>> listOrderPage(ConfirmOrderQueryDTO query) {
        Page<ConfirmOrder> page = new Page<>(query.getPageNum(), query.getPageSize());
        page(page, new LambdaQueryWrapper<ConfirmOrder>()
                .orderByAsc(true, ConfirmOrder::getDate)
                .orderByAsc(true, ConfirmOrder::getTrainCode));
        List<ConfirmOrder> confirmOrderList = page.getRecords();
        List<ConfirmOrderVo> confirmOrderVoList = BeanUtil.copyToList(confirmOrderList, ConfirmOrderVo.class);

        return ResponseResult.okResult(new PageVo<>(confirmOrderVoList, page.getPages(), page.getTotal()));
    }

    @Override
    public ResponseResult<Void> submitOrder(ConfirmOrderDTO confirmOrderDTO) {
        // 数据校验：车次是否存在，余票是否存在，车次是否在有效期内，ticket条数>0，同乘客同车次是否已经买过
        // 初始化订单状态
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = confirmOrderDTO.getDate();
        String code = confirmOrderDTO.getTrainCode(), start = confirmOrderDTO.getStart(), end = confirmOrderDTO.getEnd();
        List<PassengerTicketsDTO> passengerTickets = confirmOrderDTO.getTickets();
        ConfirmOrder confirmOrder =
                new ConfirmOrder(SnowUtil.getSnowFlakeNextId(), MemberContext.getId(), date, code, start, end,
                        confirmOrderDTO.getDailyTrainTicketId(), confirmOrderDTO.getTotalPrice(), JSON.toJSONString(passengerTickets),
                        ConfirmOrderStatusEnum.INIT, now, now);
        save(confirmOrder);
        // 查询余票，得到真实库存
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.geyByDateAndCodeAndStartAndEnd(date, code, start, end);
        // 预扣减库存，校验余票是否充足
        reduceTicketsCount(dailyTrainTicket, confirmOrderDTO.getTickets());
        // 判断是否选座，只需要随机判断一个乘客是否选座即可，因为要么全部选座，要么全部不选
        PassengerTicketsDTO passengerTicket = passengerTickets.get(0);
        SeatTypeEnum seatType = SeatTypeEnum.getByType(passengerTicket.getSeatType()); // 车座类型 ydz edz rw yw
        List<DailyTrainSeat> finalTrainSeatList = new ArrayList<>();
        if (StringUtils.hasText(passengerTicket.getSeat())) { // 选了座
            List<SeatColEnum> seatColEnumList = SeatColEnum.getColsByType(seatType.getCode()); // 座位列 A1 B1 C1 D1 F1
            Map<String, Integer> seatColmap = new HashMap<>(); // 座位排列 A1-1 B1-1 后面的1是第几排
            int index = 0;
            for (int i = 1; i <= 2; i++) {
                for (SeatColEnum seatColEnum : seatColEnumList) {
                    seatColmap.put(seatColEnum.getCode() + "-" + i, index++);
                }
            }
            log.info("newSeatCol:{}", seatColmap);
            // 处理每个乘客座位的绝对偏移值，以第一个座位基准，比如绝对偏移值为 [1, 3, 5]
            List<Integer> absoluteOffsetList = passengerTickets.stream().map(el -> seatColmap.get(el.getSeat())).collect(Collectors.toList());
            log.info("absoluteOffsetList:{}", absoluteOffsetList);
            // 最终偏移值, 将绝对偏移值优化为 [0, 2, 4]
            List<Integer> offsetList = absoluteOffsetList.stream().map(el -> el - absoluteOffsetList.get(0)).collect(Collectors.toList());
            log.info("offsetList:{}", offsetList);
            chooseSeat(date, code, seatType.getCode(), dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex(), passengerTicket.getSeat().split("-")[0], offsetList, finalTrainSeatList);
        } else { // 未选座
            for (int i = 0; i < passengerTickets.size(); i++) {
                chooseSeat(date, code, seatType.getCode(), dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex(), null, Collections.emptyList(), finalTrainSeatList);
            }
        }
        log.info("最终选座结果:{}", finalTrainSeatList);
        // 选座逻辑
            // 遍历车厢获取座位数据
            // 筛选合适座位
        // 更新座位售卖情况
        // 更新余票数量
        // 增加购票记录
        // 更新订单状态
        return ResponseResult.okEmptyResult();
    }

    // 选座逻辑
    private void chooseSeat(LocalDate date, String code, String seatType, Integer startIndex, Integer endIndex, String firstColumn, List<Integer> offsetList, List<DailyTrainSeat> finalTrainSeatList) {
        List<DailyTrainCarriage> dailyTrainCarriageList = dailyTrainCarriageService.getListByDateAndCodeAndSeatType(date, code, seatType);
        List<DailyTrainSeat> tempFinalSeatList = new ArrayList<>(); // 临时保存最终选座结果
        // 遍历车厢获取座位数据
        for (DailyTrainCarriage dailyTrainCarriage : dailyTrainCarriageList) {
            List<DailyTrainSeat> dailyTrainSeatList = dailyTrainSeatService.getListByDateAndCodeAndCarriageIndex(code, date, dailyTrainCarriage.getIndexOrder());
            for (DailyTrainSeat dailyTrainSeat : dailyTrainSeatList) {
                boolean hasChecked = false; // 用于未选座的情况，防止座位被重复选择
                for (DailyTrainSeat trainSeat : finalTrainSeatList) {
                    if (trainSeat.getId().equals(dailyTrainSeat.getId())) {
                        hasChecked = true;
                        break;
                    }
                }
                if (hasChecked) {
                    log.info("当前座位{}已被选择", dailyTrainSeat.getCarriageIndex());
                    continue;
                }
                // 判断列号
                if (!StringUtils.hasText(firstColumn)) {
                    log.info("未选座位");
                } else {
                    // 选了座位，但当前座位不匹配第一个乘客所选列，那么就枚举下一个座位
                    if (!firstColumn.equals(dailyTrainSeat.getCol().getCode())) {
                        continue;
                    }
                }
                // 为第一个乘客选座位，校验从 start-end 当前这个座位是否可选
                boolean res = checkCanSell(dailyTrainSeat, startIndex, endIndex);
                if (!res) {
                    continue;
                }
                log.info("当前乘客选取座位:{}", dailyTrainSeat);
                tempFinalSeatList.add(dailyTrainSeat);
                // 为后续乘客根据偏移值选取座位
                boolean ok = true; // 是否已经为所有乘客选好座位
                for (int i = 1; i < offsetList.size(); i++) {
                    Integer offset = offsetList.get(i);
                    int nextIndex = dailyTrainSeat.getCarriageIndex() + offset - 1;
                    if (nextIndex >= dailyTrainSeatList.size()) {
                        log.info("偏移索引超出车厢的座位数");
                        ok = false; // 任何一个乘客无法选中座位，那么直接跳出循环
                        break;
                    }
                    DailyTrainSeat nextSeat = dailyTrainSeatList.get(nextIndex);
                    boolean resT = checkCanSell(nextSeat, startIndex, endIndex);
                    if (resT) {
                        tempFinalSeatList.add(nextSeat);
                        log.info("第{}个乘客选取座位:{}", i + 1, nextSeat);
                    } else {
                        ok = false; // 任何一个乘客无法选中座位，那么直接跳出循环
                        break;
                    }
                }
                if (ok) {
                    // 保存选好的座位
                    finalTrainSeatList.addAll(tempFinalSeatList);
                    log.info("所有乘客已全部选好座位");
                    return;
                } else {
                    tempFinalSeatList.clear();
                }
            }
        }
    }

    /**
     * 校验从 start-end 当前这个座位是否可选
     * 例如 sell=10001, 站分别为A(起始站) B C D E F,则上面 10001表示 A-B, E-F已被购买
     * 本次购票为B-E，即区间为 [1-3], 值为 000，那么就可以买
     */
    private boolean checkCanSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex) {
        String sell = dailyTrainSeat.getSell(); // 10001
        String sellPart = sell.substring(startIndex, endIndex); // 000
        log.info("sell:{}, startIndex:{}, endIndex:{}, sellPart:{}", sell, startIndex, endIndex, sellPart);
        if (sellPart.contains("1")) {
            log.info("座位{}已经售卖", dailyTrainSeat.getCarriageIndex());
            return false;
        }
        String curSell = sellPart.replace('0', '1'); // 111
        curSell = StrUtil.fillBefore(curSell, '0', endIndex); // 0111
        curSell = StrUtil.fillAfter(curSell, '0', sell.length()); // 01110
        // 进行或运算，得到最终的售票结果
        int res = Integer.parseInt(sell) | Integer.parseInt(curSell); // 1110
        String sellRes = StrUtil.fillBefore(String.valueOf(res), '0', sell.length()); // 01110
        log.info("最终结果sellRes:{}", sellRes);
        dailyTrainSeat.setSell(sellRes);
        return true;
    }

    // 预扣减库存，校验余票是否充足
    private void reduceTicketsCount(DailyTrainTicket dailyTrainTicket, List<PassengerTicketsDTO> tickets) {
        for (PassengerTicketsDTO ticket : tickets) {
            String seatType = ticket.getSeatType();
            for (SeatTypeEnum value : SeatTypeEnum.values()) {
                if (seatType.equals(value.getType())) {
                    switch (value) {
                        case YDZ:
                            dailyTrainTicket.setYdz(dailyTrainTicket.getYdz() - 1);
                            if (dailyTrainTicket.getYdz() < 0) {
                                throw new BizException(HttpCodeEnum.TICKET_COUNT_NOT_ENOUGH);
                            }
                            break;
                        case EDZ:
                            dailyTrainTicket.setEdz(dailyTrainTicket.getEdz() - 1);
                            if (dailyTrainTicket.getEdz() < 0) {
                                throw new BizException(HttpCodeEnum.TICKET_COUNT_NOT_ENOUGH);
                            }
                            break;
                        case RW:
                            dailyTrainTicket.setRw(dailyTrainTicket.getRw() - 1);
                            if (dailyTrainTicket.getRw() < 0) {
                                throw new BizException(HttpCodeEnum.TICKET_COUNT_NOT_ENOUGH);
                            }
                            break;
                        case YW:
                            dailyTrainTicket.setYw(dailyTrainTicket.getYw() - 1);
                            if (dailyTrainTicket.getYw() < 0) {
                                throw new BizException(HttpCodeEnum.TICKET_COUNT_NOT_ENOUGH);
                            }
                            break;
                    }
                }
            }
        }
    }
}
