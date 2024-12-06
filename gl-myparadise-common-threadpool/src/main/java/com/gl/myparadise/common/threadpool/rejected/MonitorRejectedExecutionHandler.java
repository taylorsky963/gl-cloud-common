package com.gl.myparadise.common.threadpool.rejected;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: MonitorRejectedExecutionHandler
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-23 - 22:23
 */
public abstract class MonitorRejectedExecutionHandler implements RejectedExecutionHandler{

    public long rejectedNum = 0;

    public void addRejectedExecutionCount(){

        this.rejectedNum = this.rejectedNum + 1;

    }

    public long getRejectedExecutionCount(){

        return this.rejectedNum;
    }

}
