package com.gl.myparadise.common.database.dynamic.masterslave.loadbalance;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;


public class MasterSlaveLoadBalanceRule {

    private static final ConcurrentHashMap<String, AtomicInteger> COUNT_MAP = new ConcurrentHashMap<>();

    private static AtomicInteger count = new AtomicInteger();

    public static class SingletonHolder {

        private static final MasterSlaveLoadBalanceRule INSTANCE = new MasterSlaveLoadBalanceRule();

    }

    private MasterSlaveLoadBalanceRule() {}

    public static final MasterSlaveLoadBalanceRule getInstance() {

        return SingletonHolder.INSTANCE;
    }

    public Integer getMasterSlaveLoadBalance(int dataSourceNum, String loadBalanceRule){

        if(dataSourceNum > 1){

            if(DataSourceConfigrceConstants.SLAVE_LOADBALANCE_TYPE_RANDOM.equals(loadBalanceRule)){

                return getMasterSlaveLoadBalanceRandom(dataSourceNum);

            }

            if(DataSourceConfigrceConstants.SLAVE_LOADBALANCE_TYPE_ROBIN.equals(loadBalanceRule)){

                return getMasterSlaveLoadBalanceRobin(dataSourceNum);

            }
        }

        return 0;

    }

    private Integer getMasterSlaveLoadBalanceRandom(int dataSourceNum){

        return ThreadLocalRandom.current().nextInt(0,dataSourceNum);

    }

    private Integer getMasterSlaveLoadBalanceRobin(int dataSourceNum){

        count.compareAndSet(dataSourceNum,0);

        return count.getAndIncrement() % dataSourceNum;

    }

}
