package com.gl.myparadise.common.objectpool.config;

import com.gl.myparadise.common.objectpool.constants.ObjectPoolConfigConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @description:
 * @className: MultipleObjectPoolProperties
 * @projectName: gl-cloud-base
 * @date 2025-03-25 - 03:12
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class MultipleObjectPoolProperties implements Serializable {

    private static final long serialVersionUID = 2599247497674029883L;

    private List<String> names;

}
