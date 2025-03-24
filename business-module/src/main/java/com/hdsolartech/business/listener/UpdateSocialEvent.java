package com.hdsolartech.business.listener;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author MySkyBeyond
 * @version 1.0
 * Brief description of the class.
 * @date 2024/6/4
 */
@Getter
public class UpdateSocialEvent extends ApplicationEvent {
    private final String message;
    public UpdateSocialEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

}
