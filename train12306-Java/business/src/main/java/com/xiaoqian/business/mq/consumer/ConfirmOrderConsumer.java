package com.xiaoqian.business.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.xiaoqian.business.mq.dto.ConfirmOrderMQDto;
import com.xiaoqian.business.service.IConfirmOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "default", topic = "CONFIRM_ORDER", maxReconsumeTimes = 0)
@RequiredArgsConstructor
public class ConfirmOrderConsumer implements RocketMQListener<MessageExt> {
    private final IConfirmOrderService confirmOrderService;
    @Override
    public void onMessage(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        log.info("MQ收到消息：{}", new String(body));
        ConfirmOrderMQDto confirmOrderMQDto = JSONObject.parseObject(new String(body), ConfirmOrderMQDto.class);
        confirmOrderService.doConfirm(confirmOrderMQDto);
    }
}
