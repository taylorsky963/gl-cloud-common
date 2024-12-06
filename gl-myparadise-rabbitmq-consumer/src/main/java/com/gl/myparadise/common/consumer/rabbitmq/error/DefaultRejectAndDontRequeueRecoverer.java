package com.gl.myparadise.common.consumer.rabbitmq.error;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.retry.interceptor.MethodInvocationRecoverer;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: LGRejectAndDontRequeueRecoverer
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-30 - 21:30
 */
public class DefaultRejectAndDontRequeueRecoverer implements MethodInvocationRecoverer {

    protected Log logger = LogFactory.getLog(RejectAndDontRequeueRecoverer.class); // NOSONAR protected

    @Override
    public Object recover(Object[] args, Throwable cause) {

        this.logger.error("DefaultRejectAndDontRequeueRecoverer Retries exhausted for message " + args, cause);

        return null;
    }

}
