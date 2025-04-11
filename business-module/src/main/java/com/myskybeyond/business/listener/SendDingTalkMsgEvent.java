package com.myskybeyond.business.listener;

import com.myskybeyond.business.domain.bo.TestMsgSendBo;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author MySkyBeyond
 * @version 1.0
 * Brief description of the class.
 * @date 2024/6/7
 */
@Getter
@ToString
public class SendDingTalkMsgEvent extends ApplicationEvent {
    private final TestMsgSendBo msgSendBo;

    public SendDingTalkMsgEvent(Object source, TestMsgSendBo msgSendBo) {
        super(source);
        this.msgSendBo = msgSendBo;
    }
}
