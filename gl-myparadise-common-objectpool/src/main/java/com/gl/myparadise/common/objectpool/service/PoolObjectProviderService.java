package com.gl.myparadise.common.objectpool.service;

/**
 * @author Vincent
 * @description:
 * @className: PoolObjectProviderService
 * @projectName: gl-cloud-base
 * @date 2025-03-25 - 20:49
 */
public interface PoolObjectProviderService {

    /**
     * 提供池中要使用的唯一对象（例如从数据库选中的邮箱/短信等）
     */
    Object provide();

    void destroy();

}
