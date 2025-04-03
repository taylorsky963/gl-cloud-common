package com.gl.myparadise.common.webclient.register;

import com.gl.myparadise.common.tools.util.ListUtil;
import com.gl.myparadise.common.webclient.config.HttpPoolClientRetryProperties;
import com.gl.myparadise.common.webclient.config.HttpPoolConfigurationProperties;
import com.gl.myparadise.common.webclient.config.HttpPoolNameConfigurationProperties;
import com.gl.myparadise.common.webclient.constants.WebClientConstants;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: WebClientConnectRegister
 * @ProjectName: gl-common
 * @Date 2023-06-22 - 19:59
 */
@Component
@ConditionalOnProperty(prefix = WebClientConstants.HTTPPOOL_CONFIG_TAG, name = "names")
public class WebClientConnectRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(WebClientConnectRegister.class);

    private Binder binder;

    private Environment environment;

    private Map<String, HttpPoolConfigurationProperties> httpPoolConfigurationPropertiesMap
            = new HashMap<String, HttpPoolConfigurationProperties>();

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        binder = Binder.get(environment); //绑定配置器

        bulidHttpPoolConfigurationPropertiesMap();

    }

    private void bulidHttpPoolConfigurationPropertiesMap() {

        HttpPoolNameConfigurationProperties poolNameConfigurationProperties
                = binder.bind((WebClientConstants.HTTPPOOL_CONFIG_TAG).trim(), HttpPoolNameConfigurationProperties.class).get();

        if (poolNameConfigurationProperties != null
                && ListUtil.isNotEmpty(poolNameConfigurationProperties.getNames())) {

            List<String> poolNames = poolNameConfigurationProperties.getNames();

            for (int i = 0; i < poolNames.size(); i++) {

                String poolName = poolNames.get(i);

                logger.debug("bulidHttpPoolConfigurationPropertiesMap poolName is : " + poolName);

                HttpPoolConfigurationProperties httpPoolConfigurationProperties = bulidHttpPoolConfigurationProperties(poolName);

                httpPoolConfigurationPropertiesMap.put(poolName, httpPoolConfigurationProperties);

                WebClientConstants.httpPoolRetryPropertiesMap.put(httpPoolConfigurationProperties.getBeanName(), bulidHttpPoolClientRetryProperties(poolName));
            }

        } else {

            logger.error("bulidHttpPoolConfigurationProperties poolNameConfigurationProperties  is error");
        }

    }

    private HttpPoolConfigurationProperties bulidHttpPoolConfigurationProperties(String poolName) {

        return binder.bind((WebClientConstants.HTTPPOOL_CONFIG_TAG + WebClientConstants.PROPERTIES_SPLIT_TAG + poolName).trim(), HttpPoolConfigurationProperties.class).get();

    }

    private HttpPoolClientRetryProperties bulidHttpPoolClientRetryProperties(String poolName) {

        return binder.bind((WebClientConstants.HTTP_POOL_RETRY_BASE_TAG + WebClientConstants.PROPERTIES_SPLIT_TAG + poolName).trim(), HttpPoolClientRetryProperties.class).get();

    }

    //获取WEBCLIENT
    private WebClient getWebClient(String httpPoolName, HttpPoolConfigurationProperties httpPoolConfigurationProperties) {

        return WebClient.builder()
                .defaultCookies(cookie -> {

                    Map<String, String> cookieMap = httpPoolConfigurationProperties.getCookieMap();

                    if(!cookieMap.isEmpty()){

                        Iterator<String> iterator = cookieMap.keySet().iterator();

                        while (iterator.hasNext()) {

                            String key = iterator.next();

                            cookie.add(iterator.next(), cookieMap.get(key));
                        }

                    }

                })
                .defaultHeaders(httpHeaders -> {

                    Map<String, String> httpMap = httpPoolConfigurationProperties.getHeaderMap();

                    if(!httpMap.isEmpty()){

                        Iterator<String> iterator = httpMap.keySet().iterator();

                        while (iterator.hasNext()) {

                            String key = iterator.next();

                            httpHeaders.add(iterator.next(), httpMap.get(key));

                        }

                    }

                })
                .baseUrl(httpPoolConfigurationProperties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(getHttpClient(httpPoolName, httpPoolConfigurationProperties)))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(httpPoolConfigurationProperties.getMaxInMemorySize() * 1024 * 1024))
                .build();

    }

    //获取httpclient
    private HttpClient getHttpClient(String httpPoolName, HttpPoolConfigurationProperties httpPoolConfigurationProperties) {

        HttpClient httpClient = HttpClient.create(getConnectionProvider(httpPoolName, httpPoolConfigurationProperties))
                .tcpConfiguration(tcpClient -> {

                    return tcpClient.doOnConnected(connection -> {

                        connection.addHandlerLast(new ReadTimeoutHandler(httpPoolConfigurationProperties.getReadTimeout(), TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(httpPoolConfigurationProperties.getWriteTimeout(), TimeUnit.SECONDS));

                    }).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, httpPoolConfigurationProperties.getConnectTimeout())
                            .option(ChannelOption.TCP_NODELAY, true);

                });

        //没有证书暂时无法测试
        if (StringUtils.isNotEmpty(httpPoolConfigurationProperties.getSslPath())) {

            httpClient.secure(sslContextSpec -> {
                SslContextBuilder sslContextBuilder = SslContextBuilder.forClient()
                        .trustManager(new File(httpPoolConfigurationProperties.getSslPath()));

                sslContextSpec.sslContext(sslContextBuilder);

            });
        }

        return httpClient;

    }

    //设置NETTY 的连接池
    private ConnectionProvider getConnectionProvider(String httpPoolName, HttpPoolConfigurationProperties httpPoolConfigurationProperties) {

        return ConnectionProvider.builder(httpPoolName)
                .maxConnections(httpPoolConfigurationProperties.getMaxTotalConnect())
                .pendingAcquireMaxCount(httpPoolConfigurationProperties.getPendingAcquireMaxCount())
                .pendingAcquireTimeout(Duration.ofSeconds(httpPoolConfigurationProperties.getPendingAcquireTimeout()))
                .maxIdleTime(Duration.ofSeconds(httpPoolConfigurationProperties.getMaxIdleTime()))
                .maxLifeTime(Duration.ofSeconds(httpPoolConfigurationProperties.getMaxLifeTime()))
                .build();

    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        if (!httpPoolConfigurationPropertiesMap.isEmpty()) {

            Iterator<String> iterator = httpPoolConfigurationPropertiesMap.keySet().iterator();

            while (iterator.hasNext()) {

                String httpPoolName = iterator.next();

                HttpPoolConfigurationProperties httpPoolConfigurationProperties = httpPoolConfigurationPropertiesMap.get(httpPoolName);

                logger.debug("WebClientConnectRegister postProcessBeanDefinitionRegistry httpPoolName : " + httpPoolName);

                String httpPoolBeanName = httpPoolConfigurationProperties.getBeanName();

                logger.debug("WebClientConnectRegister postProcessBeanDefinitionRegistry beanName : " + httpPoolBeanName);

                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(WebClient.class, () -> {
                    // 这个方法可以定义这个bean的实例创建方式，如构造函数之后还想调用其他的方法，也可以在这里做
                    return getWebClient(httpPoolName, httpPoolConfigurationProperties);

                });

                BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

                if(httpPoolConfigurationProperties.getPrimary()){

                    beanDefinition.setPrimary(true);

                }
                //注册bean定义
                beanDefinitionRegistry.registerBeanDefinition(httpPoolBeanName, beanDefinition);

            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }


}
