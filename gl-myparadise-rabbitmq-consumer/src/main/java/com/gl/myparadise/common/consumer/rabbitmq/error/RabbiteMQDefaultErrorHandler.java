package com.gl.myparadise.common.consumer.rabbitmq.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbiteMQDefaultErrorHandler
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-30 - 18:19
 */
public class RabbiteMQDefaultErrorHandler implements ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(RabbiteMQDefaultErrorHandler.class);

    @Override
    public void handleError(Throwable throwable) {

        logger.error("RabbiteMQDefaultErrorHandler drop : ", throwable);
    }
}
