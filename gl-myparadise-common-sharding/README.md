# 概述。
封装了数据库连接池，及其对数据库的操作，支持SPRING DATASOURE 和SHARDING两种方式

## SPRING DATASOURE 配置说明

## 单独数据源

配置spring.datasource 的 dataSourceName 必须配置

    spring.datasource.names=productdb

spring.datasource + 数据源名称 + master-data-source-name 配置数据库名称

    spring.datasource.productdb.master-data-source-name=dsmaster

数据库连接池配置 spring.datasource + 数据源名称 + 数据库名称 + 数据库连接池配置

    spring.datasource.productdb.dsmaster.type=org.apache.commons.dbcp2.BasicDataSource
    spring.datasource.productdb.dsmaster.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.productdb.dsmaster.url=xxx
    spring.datasource.productdb.dsmaster.username=xxx
    spring.datasource.productdb.dsmaster.password=xxx
    spring.datasource.productdb.dsmaster.initial-size=1
    spring.datasource.productdb.dsmaster.max-total=2
    spring.datasource.productdb.dsmaster.max-idle=2
    spring.datasource.productdb.dsmaster.min-idle=1
    spring.datasource.productdb.dsmaster.max-wait-millis=10000
    spring.datasource.productdb.dsmaster.validation-query=SELECT 1
    spring.datasource.productdb.dsmaster.connection-properties=characterEncoding=utf8
    
对应的Mybatis配置

gl.mybatis + 数据源名称 + 配置项  
    
    gl.mybatis.productdb.basePackage=com.gl.myparadise.demo.server.dao
    gl.mybatis.productdb.typeAliasesPackage=com.gl.myparadise.demo.entity
    gl.mybatis.productdb.configLocations=classpath:mybatis-config.xml
    gl.mybatis.productdb.mapperLocations=classpath*:mapper/**/*.xml
    
## 读写分离配置

主库按照单独数据源配置
    
添加从库名称 spring.datasource + 数据源名称 + slave-data-source-names

    spring.datasource.productdb.slave-data-source-names=dsslave0,dsslave1

配置读写分离规则从库读取规则 spring.datasource + 数据源名称 + 配置项

    spring.datasource.productdb.load-balance-algorithm-type=RANDOM
    spring.datasource.productdb.props.k1=v1

从库数据库连接池配置 spring.datasource + 数据源名称 + 从库名称 + 数据库连接池配置

    spring.datasource.productdb.dsslave0.type=org.apache.commons.dbcp2.BasicDataSource
    spring.datasource.productdb.dsslave0.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.productdb.dsslave0.url=xxx
    spring.datasource.productdb.dsslave0.username=xxx
    spring.datasource.productdb.dsslave0.password=xxx
    spring.datasource.productdb.dsslave0.initial-size=1
    spring.datasource.productdb.dsslave0.max-total=10
    spring.datasource.productdb.dsslave0.max-idle=5
    spring.datasource.productdb.dsslave0.min-idle=1
    spring.datasource.productdb.dsslave0.max-wait-millis=30000
    spring.datasource.productdb.dsslave0.test-while-idle=true
    spring.datasource.productdb.dsslave0.minEvictableIdleTimeMillis=300000
    spring.datasource.productdb.dsslave0.timeBetweenEvictionRunsMillis=600000
    spring.datasource.productdb.dsslave0.validation-query=SELECT 1
    spring.datasource.productdb.dsslave0.connection-properties=characterEncoding=utf8
    
每一个从库都要配置相应的数据库连接池

对应的Mybatis配置 同单独的数据源

## 多SPRING DATASOURCE

配置spring.datasource 的 dataSourceName 必须配置

    spring.datasource.names=productdb,userdb

指定主数据源，如不指定SPRING会报错    
    
    spring.datasource.primary=productdb

配置事务管理器名称 spring.datasource  + 数据源名称 + .transaction.name
    
    spring.datasource.productdb.transaction.name=productdbTransactionManager

配置事务管理的切入点，如果多个切入点请用， 分隔（注：不执行到类和方法，请配置到PACKAGE，通配符 支持 * 和 **）

    spring.datasource.productdb.transaction.base-packages=com.gl.myparadise.demo.server.service.sharding

如果不配置的话，也可以在 @Transactional 标签里直接要使用的事务管理器 否者事务将默认是主数据源事务

    @Transactional(transactionManager="productdbTransactionManager", rollbackFor = Exception.class)

每一个数据源都要配置一套，数据库连接池的配置请参考 单独数据源配置和读写分离配置

Mybatis配置同 单独数据源配置，每个数据源配置一套

## 单独 sharding 读写分离配置

配置数据库名称

    sharding.jdbc.datasource.names=master,slave0,slave1

配置数据源名称

    sharding.jdbc.config.masterslave.source-name=ds_master_slave
    
配置主从数据配名称及从库读取规则
    
    sharding.jdbc.config.masterslave.master-data-source-name=master
    sharding.jdbc.config.masterslave.slave-data-source-names=slave0,slave1
    sharding.jdbc.config.masterslave.load-balance-algorithm-type=round_robin

配置每个数据库的数据库连接池 sharding.jdbc.datasource + 数据库名称 + 连接池配置

    sharding.jdbc.datasource.master.url=xxx
    sharding.jdbc.datasource.master.username=xxx
    sharding.jdbc.datasource.master.password=xxx
    sharding.jdbc.datasource.master.driverClassName=com.mysql.cj.jdbc.Driver
    sharding.jdbc.datasource.master.type=com.alibaba.druid.pool.DruidDataSource

    sharding.jdbc.datasource.slave0.url=xxx
    sharding.jdbc.datasource.slave0.username=xxx
    sharding.jdbc.datasource.slave0.password=xxx
    sharding.jdbc.datasource.slave0.driverClassName=com.mysql.cj.jdbc.Driver
    sharding.jdbc.datasource.slave0.type=com.alibaba.druid.pool.DruidDataSource 
    
    sharding.jdbc.datasource.slave1.url=xxx
    sharding.jdbc.datasource.slave1.username=xxx
    sharding.jdbc.datasource.slave1.password=xxx
    sharding.jdbc.datasource.slave1.driverClassName=com.mysql.cj.jdbc.Driver
    sharding.jdbc.datasource.slave1.type=com.alibaba.druid.pool.DruidDataSource 

Mybatis 配置 同单独数据源配置

## sharding 与 spring datasource 一起使用

sharding 数据源系统默认为主数据源，因此不需要配置 事务管理器及数据源名称

sharding的配置请 参考 上面的单独使用sharding的配置

spring datasource 的配置请参考 多数据源的配置

    spring.datasource.names=productdb
    spring.datasource.productdb.transaction.name=productdbTransactionManager
    spring.datasource.productdb.transaction.base-packages=com.gl.myparadise.demo.server.service.dy.user

Mybatis配置参靠多数据源配置 

    gl.mybatis.productdb.basePackage=com.gl.myparadise.demo.server.dao.dy
    gl.mybatis.productdb.typeAliasesPackage=com.gl.myparadise.demo.entity.dy
    gl.mybatis.productdb.configLocations=classpath:mybatis-config.xml
    gl.mybatis.productdb.mapperLocations=classpath*:mapper/dy/**/*.xml
    
    gl.mybatis.shardingDataSource.basePackage=com.gl.myparadise.demo.server.dao.sharding
    gl.mybatis.shardingDataSource.typeAliasesPackage=com.gl.myparadise.demo.entity.sharding
    gl.mybatis.shardingDataSource.configLocations=classpath:mybatis-config.xml
    gl.mybatis.shardingDataSource.mapperLocations=classpath*:mapper/sharding/**/*.xml

分页封装

分页自定义返回类 PageListModel
    
PageListModel 属性为   

    //存放LIST数据
    private List<T> rsData
    //当前页码
    private Integer pageNum;
    //每页显示多少条
    private Integer pageSize;
    //总共多少条
    private Long pageTotal;
    //总共多少页
    private int pages;
    
分页自定义标签为 PageQuery

    //页号的参数名
    String pageNumParaName() default "pageNum";

    //每页行数的参数名
    String pageSizeParaName() default "pageSize";
    
    //是否关闭COUNT(0)
    String isSqlCount() default "isSqlCount";

如果参数名不是默认参数名，请指定参数

    @PageQuery(pageNumParaName="pageNum1", pageSizeParaName="pageSize1")
    
如果参数对象是BEAN 格式为 userInfo.pageSize
    
    @PageQuery(pageNumParaName="userInfo.pageNum", pageSizeParaName="userInfo.pageSize")
 
返回值 必须为 PageListModel 对象

#pagehelper分页插件配置

supportMethodsArguments 配置为FALSE , 否则与自定义查询BEAN冲突
    
    pagehelper.helperDialect=mysql
    pagehelper.reasonable=true
    pagehelper.supportMethodsArguments=false
    pagehelper.params=count=countSql