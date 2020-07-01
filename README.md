# auth-system
## 简介
这是使用了springboot,shiro,mybatis-plus,redis等开发的一个简单的权限项目(目前完成了一些通用的封装 尚未全部完成)

## 项目结构介绍
```
mp-server(主服务)
.
├── common (一些通用的内容 不会涉及实现)
│   ├── anno 所有的自定义注解类
│   ├── buider 全局对象的构造器类
│   ├── constant 全局的常量类
│   ├── context 一些上下文对象 
│   ├── entity 全局的实体类
│   ├── enums 全局的基础枚举
│   └── exeception 和异常相关的一些基础类
├── controller(控制器)
├── dao(数据访问层)
├── dto(数据对象)
├── function(包含一些系统职能相关的内容 包括定时任务,策略服务,过滤器,拦截器等)
│   ├── aspect 放置所有的aop切面
│   ├── basic 放置一些提供实现的基本类
│   ├── config 包含spring的一些bean配置
│   ├── filter(过滤器)
│   ├── handler(处理器)
│   ├── interceptor(拦截器)
│   ├── job(定时任务)
│   └── strategy(策略相关)
│       ├── enums 策略枚举
│       ├── factory 策略工厂
│       └── service 策略服务
├── properties(系统配置类)
├── service(服务层)
├── tool(业务相关的工具类)
└── util(业务无关的工具类)
mp-common-core(核心工具 提供工具依赖和一些自定义工具)
mp-code-generator-spring-boot-starter(代码生成器starter)
mp-shiro-spring-boot-starter(shiro starter)
```

## starter配置介绍

### mp-code-generator-spring-boot-starter
计划加入: 多文件夹生成,根据备注生成枚举
配置前缀为mp.tool.generator,数据库配置默认由spring.datasource或spring.datasource.druid下读取
为了方便配置读取需要借助spring注入运行
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {
     @Autowired
     private CodeGeneratorUtils codeGeneratorUtils; 
     @Test
     public void generator() {
        codeGeneratorUtils.generator();
     }
}
```
支持所有的原生配置,例如:
```yaml
mp:
  tool:
    generator:
      global:
        fileOverride: true
```
将部分配置移动至顶层并新增部分配置
```yaml
mp:
  tool:
    generator:
      # 相当于globalConfig.include
      gen-tables: xxx,xxx
      # 相当于packageConfig.parent
      base-package: com.maple
      # 模块名 模块名将统一插入实际生成的文件之前 例如entity的文件路径将是 basePackage + entity + modelName + '文件名'
      model-name: admin
```

### mp-shiro-spring-boot-starter
计划从官方starter依赖中独立出来
配置前缀为mp.shiro,支持所有官方starter配置,例如:
```yaml
shiro:
  loginUrl: /login
  successUrl: /
  sessionManager:
    cookie:
      name: MPJSESSIONID
      maxAge: 200000
  unauthorizedUrl: /error/403
```
额外增加的配置:
```yaml
mp:
  shiro:
    # 过滤链 由最后一个:分隔anon user等权限动作
    filter-chain:
      - /login:anon
      - /logout:logout
      - /**:jwt,authc
    # 由全限定包名自动加载realms并设置加密方式 也支持直接用bean形式定义 自选即可
    realms:
      - realm:
        target: com.maple.server.function.auth.MyRealm
        credentials-matcher:
          target: org.apache.shiro.authc.credential.HashedCredentialsMatcher
          property:
            hashAlgorithmName: MD5
            hashIterations: 2
            storedCredentialsHexEncoded: true
    # 自动代理创建配置 由于官方默认配置会使权限注解与aop冲突 因此增加此配置自定义
    advisor-auto-proxy-creator:
      use-prefix: true
      proxy-target-class: false
    cache:
      redis:
        # 是否开启redis进行session管理 redis配置自动由spring.redis下读取
        enable: true
        # 标识不同用户所用的字段
        principalIdFieldName: id
        # 过期时间
        expire: 20000
    web:
      # 以map形式读取并覆盖默认shiro filter
      filters:
        authc: com.maple.starter.shiro.filter.CustomUserFilter
    jwt:
      # 是否开启jwt 开启则自动注册jwtRealm jwtFilter(name为jwt)
      enable: true
      # token生成秘钥
      secret: xxxxxx
      # 过期时间
      expire: 200000
      # token请求时的前缀
      prefix: Bearer 
      # token签名时返回的前缀 通常和prefix是一个值
      sign-prefix: Bearer 
      # 请求头携带token的key
      header-key: Authorization
```


## 数据库介绍
初始化sql详见init.sql项目引入了liquibase配置数据库启动项目将会自动执行
如果需要关闭自动执行请修改配置spring.liquibase.enabled属性为false

## 系统工具介绍
### 后台工具
#### 缓存工具
```
// 配置项目prefix 在application.yml/application.properties 工具类和注解中的实际key将加上这个值
cache.prefix=...
// 普通工具类(注入使用)
@Autowired
RedisUtils redisUtils;
//设置缓存且不超时
redisUtils.set(key,object);
//设置缓存且自定义超时时间(/分钟)
redisUtils.set(key,object,ttlMinutes);
//设置缓存且自定义超时时间和时间单位
redisUtils.set(key,object,ttl,timeUnit);
//获取对象 什么类型存入就以什么类型取出来
redisUtils.get(key);
 
// 缓存切面(任意方法上使用,key支持SpEL表达式) 
// 此例将为该方法返回值设置10分钟的key为 fileCache-[key]的缓存,即接下来10分钟从缓存获取方法返回值。
// 注: 当切面方法返回值为 Optional,Map,List类型时 内容不存在不会存储缓存
@Cache(key = "'fileCache-' + #key",ttlMinutes = 10)
public List<UserDTO> queryList(String key)
```
#### 用户上下文注入
```
//方法拦截器将会为任意带CurrentUser注解的UserDTO对象注入获取到的用户上下文
public UserDTO getUser(@CurrentUser UserDTO userDTO)
```
#### 策略模式
提供的策略实现包含三部分内容:策略工厂，策略枚举和策略服务
工厂声明 策略使用时主要是注入工厂根据枚举值获取服务,为了便于拓展 策略接口可由使用方自行定义:
```java
@Component
public class TestStrategyFactory extends BaseStrategyFactory<TestStrategyEnum, StrategyService> {}
```
枚举声明 枚举的码值需要和spring的bean名称相同:
```java
public enum TestStrategyEnum implements BaseStrategyEnum {
    /**
     * 枚举信息
     */
    SERVICE1(TestService1Impl.class, "测试策略1"), SERVICE2(TestService2Impl.class, "测试策略2");

    TestStrategyEnum(Class<?> clazz, String strategyDesc) {
        this.value = SpringUtils.getBeanNameByType(clazz);
        this.strategyDesc = strategyDesc;
    }

    /**
     * 策略值
     */
    private String value;
    /**
     * 策略描述
     */
    private String strategyDesc;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getStrategyDesc() {
        return strategyDesc;
    }
}
```
服务声明 提供了几个可用的接口(分别对应无参数无返回值[StrategyService] 一参数一返回值[FunctionStrategyService] 二参数一返回值[BiFunctionStrategyService]) 
不满足需求也可自行定义:
```java
@Service
@Slf4j
public class TestService1Impl implements StrategyService {
    @Override
    public void exec() {
        log.info("service1");
    }
}
```
使用:
```java
public class StrategyFactoryTest extends BaseTest {
    @Autowired
    private TestStrategyFactory testStrategyFactory;

    @Test
    public void testService() {
        testStrategyFactory.getByType(TestStrategyEnum.SERVICE1).exec();
    }
}
```


### 前端工具

#### tableServer.js
基于amaze-ui data-tables的一层封装详见文档:
[tableServer](http://note.youdao.com/noteshare?id=d1831a1a33f68265315104999e5d8085&sub=9EF2DC58A15C42E78290038D73133345)

#### operateServer.js

#### strTool.js

#### optional.js