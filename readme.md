## 第三章: 简单服务提供和消费，硬绑定地址 

- microservice-simple-provider-user 服务提供者
- microservice-simple-consumer-movie 服务消费者
   
## 第四章：服务注册和发现 erueka (单词意思： 找到啦！)

启动顺序：  microservice-discovery-eureka  -> microservice-provider-user -> microservice-consumer-movie  

- microservice-discovery-eureka   单点 eureka 服务端
- microservice-provider-user     eureka client 服务提供方
- microservice-consumer-movie    eureka client 服务消费方， 通过feign指定服务提供方的spring.application.name来消费，解除和具体服务地址的绑定了

```java
//1.  入口类上 启用feign客户端
@EnableFeignClients

//2.  声明 feign 客户端并指定服务提供方应用名称（ spring.application.name)
@FeignClient(name="microservice-provider-user")
public interface UserClient {

    @GetMapping("/user/all")
    List<User> findAll();
}

//3. 注入使用
@Autowired
UserClient userClient;

/**
 * 通过 feign 调用
 * @return
 */
@GetMapping("/user/all")
public List<User> findAll(){
    return userClient.findAll();
}
```

虽然 eureka.client.serviceUrl.defaultZone = http://localhost:8761/eureka/
但是查看 eureka 注册情况使用 http://localhost:8761 就可以了

- eureka server: microservice-discovery-eureka-ha   高可用，同一份代码，两个profile 

```
sudo vim /etc/hosts
// 增加 
127.0.0.1 peer1 peer2

// 切记 :wq 后  source /etc/hosts 使其生效
```
打完jar包后，用下面命令启动不同profile 
```

// 注意打包的过程需要用 apply plugin: 'org.springframework.boot'
// springboot的插件 然后执行 bootRepackage 将所有依赖一起打包进去 
java -jar microservice-discovery-eureka-ha-0.0.1-SNAPSHOT.jar --spring.profiles.active=peer1

java -jar microservice-discovery-eureka-ha-0.0.1-SNAPSHOT.jar --spring.profiles.active=peer2
```
接着将 microservice-provider-user的注册地址 改成
```
eureka:
  client:
    serviceUrl:
      defaultZone:
  instance:
    prefer-ip-address: http://peer1:8761/eureka,http://peer2:8762/eureka
```

## 第五章 客户端负载均衡 ribbon
spring-cloud-starter-netflix-eureka-server 包中已经包含了 ribbon 依赖
所以上一章注册到 eureka 的服务会自动进行 load balance

- microservice-consumer-movie-ribbon 

```java
@Bean
// 启用负载均衡能力
@LoadBalanced
public RestTemplate restTemplate(){
    return new RestTemplate();
}
```
-  microservice-consumer-movie-ribbon-customizing 自定义负载均衡类型

注意应用名称不能包含下划线 否则ribbon调用时候会发生异常

-  microservice-consumer-movie-ribbon-without-eureka 脱离 eureka 使用ribbon
去除 eureka 依赖，删掉 @EnableDiscoveryClient 注解
修改原 eureka 配置为
```
microservice-provider-user:
  ribbon:
    listOfServers: localhost:8080,localhost:8081
```

## 第六章 Feign 声明式 Rest 客户端

启动顺序 : microservice-discovery-eureka  -> microservice-provider-user -> microservice-consumer-movie-xx

- microservice-consumer-movie-feign

```java
/**
* 指明调用的应用名称，如果启用 eureka 会自动进行负载均衡
*/
@FeignClient(name = "microservice-provider-user")
public interface UserClient {

    @GetMapping("/user/all")
    List<User> findAll();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    User findById(@PathVariable("id") Long id);
}
```

- microservice-consumer-movie-feign-customizing  自定义 feign 配置， feign 默认使用的契约是 SpringMvcContract

```
/**
 * Feignn 的配置类
 * 不应在主程序上下文的 @ComponentScan 中, 否则会被所有 FeignClient 共享
 */
@Configuration
public class FeignConfiguration {

    /**
     * 将契约改为 feign 原生的默认契约，然后就可以使用 feign 自带的注解了
     * @return
     */

    @Bean
    public Contract feignContract(){
        return new feign.Contract.Default();
    }

}
```

```
/**
 * 指明配置类
 * 
 */
@FeignClient(name = "microservice-provider-user", configuration = FeignConfiguration.class)
public interface UserClient {

    /**
     * feign 自带的注解 @RequestLine
     * @param id
     * @return
     */
    @RequestLine("GET /{id}")
    User findById(@PathVariable("id") Long id);
}
```

#### 手动创建 feign
- microservice-provider-user-auth 需要认证的用户服务
- microservice-consumer-movie-feign-manual 手动创建 feign 

#### 自定义日志级别
- microservice-consumer-movie-feign-logging
```
//1. 新建配置类
@Configuration
public class FeignLogConfiguration {

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}

// 2. 启用配置
@FeignClient(name = "microservice-provider-user",configuration = FeignLogConfiguration.class)
public interface UserClient { // ...}

// 3. 设置具体类的日志级别 application.yml
logging:
  level:
     pri.holysu.springcloud.microserviceconsumermoviefeignlogging.client: DEBUG
 
```

#### 开启压缩

```
feign.compression.request.enabled=true
feign.compression.response.enabled=true
feign.compression.request.mime-types=text/html,application/xml,application/json
feign.compression.request.min-request-size=2048
```

## 第七章 使用 Hystrix (单词意思：豪猪) 实现微服务的容错处理
微服务架构的应用不免有多个层级，服务之间也需要通过网络进行通信。"基础服务故障"会导致"级联故障"，这种现象称为雪崩效应， 服务提供者不可用
将导致消费者不可用，并将不可用逐渐放大的过程。

- microservice-consumer-movie-ribbon-hystrix  熔断回退操作
- microservice-consumer-movie-feign-hystrix-fallback 结合 feign 指定 fallback 
- microservice-consumer-movie-feign-hystrix-fallback-factory  通过指定 fallbackfactory 来设置回退策略，可以在创建里边加入日志，以更好的分析问题

启动顺序： microservice-discovery-eureka ->  microservice-provider-user -> microservice-consumer-movie-ribbon-hystrix

模拟服务不可用： 停掉 microservice-provider-user 或者按住F5狂刷(我刷了好久没反应， 把上限设置低一点应该比较容易) 再看 http://localhost:8081/user/1 的返回值 

可以通过 /health（需要引用 actuator) 查看到 "hystrix" 的状态，如
```
{
    "status":"UP",
    "hystrix":{
        "status":"UP"
    }
    ... 
}

{
    "status":"UP",
    "hystrix":{
        "status":"CIURCUIT_OPEN",
        "openCircuitBreakers":[
            "MovieController::findById"
        ]
    }
    ... 
}

```

#### hystrix 监控
启动顺序：  microservice-discovery-eureka ->  microservice-provider-user -> microservice-consumer-movie-ribbon-hystrix -> microservice-hystrix-dashboard

查看监控 /hystrix.stream

- microservice-hystrix-dashboard 使用 Hystrix Dashboard 可视化监控数据

```
// 依赖
compile('org.springframework.cloud:spring-cloud-starter-hystrix-dashboard')

// 启用
@EnableHystrixDashboard

// 使用
http://localhost:8030/hystrix
// 在豪猪页面的 url 栏位输入 http://localhost:8010/hystrix.stream
```

- microservice-hystrix-turbine 使用 turbine 聚合监控多个服务
```
# 配置
turbine:
  # 监控以下两个服务
  app-config: microservice-consumer-movie,microservice-consumer-movie-feign-hystrix-fallback-stream
  cluster-name-expression: "'default'"
```
启动：
1. microservice-discovery-eureka
2. microservice-provider-user
3. microservice-consumer-movie-ribbon-hystrix
4. microservice-consumer-movie-feign-hystrix-fallback-stream
5. microservice-hystrix-dashboard
6. microservice-hystrix-turbine
然后访问 http://localhost:8010/user/1 , http://localhost:8020/user/1 产生数据

再打开 hystrix dashboard首页 http://localhost:8030/hystrix.stream 填入 http://localhost:8031/turbine.stream 查看聚合监控

## 第八章 zuul 构建微服务网关
- microservice-gateway-zuul 
```
# 依赖
compile('org.springframework.cloud:spring-cloud-starter-zuul')

// 启用，程序入口打上标记
@EnableZuulProxy

// 转发规则
http://{Zuul_host}:{Zuul_port}/微服务在 eureka 上的serviceId/**  会被转发到 serviceId 对应的服务上
```
启动顺序： microservice-discovery-eureka -> microservice-provider-user -> microservice-consumer-movie-ribbon -> microservice-gateway-zuul

访问 http://127.0.0.1:8040/microservice-provider-user/user/1 就会转发到服务microservice-provider-user上 serviceId 对应的是配置中 spring.application.name;
注意 eureka 首页显示的 application 名称是全大写的， url 是大小写敏感的，serviceId 需要按照实际的大小写

#### 路由
可以通过 /routes 来查看路由详情， 如
```
http://127.0.0.1:8040/routes
{
"/microservice-consumer-movie/**": "microservice-consumer-movie",
"/microservice-provider-user/**": "microservice-provider-user"
}
```
```
// 设置 zuul 路由
zuul:
  routes:
    microservice-provider-user: /user/**

// 忽略指定微服务
zuul:
    ignore-services: microservice-provider-user,microservice-consumer-movie
    
// 同时设置微服务的 serviceId 和对应路径
zuul:
 routes:
    user-route: # 可以任意
        url: http://localhost:8000/
        path: /user/**
        
zuul:
 routes:
    user-route: 
        service-id: microservice-provider-user # 指定服务名称而不是写死url
        path: /user/**
        
...

```
也可以自行提供 PatternServiceRouteMapper 的bean 实现来自定义路由

#### zuul 安全与header
```
zuul:
    sensitive-headers: Cookie,Set-Cookie,Authorization
```
#### zuul 过滤器
- microservice-gateway-zuul-filter 继承 ZuulFilter 重写方法，然后在启动类申明 bean 即可

zuul 四种过滤器leixing :
1. PRE: 在请求被路由之前调用。可用来实现身份验证、在集群中选择请求的微服务、记录调试信息等
2. ROUTING： 将请求路由到微服务。用于构建发送给微服务的请求，并使用 Apache HttpClient 或 Netflix Ribbon 请求微服务
3. POST: 路由到微服务以后执行。可给响应添加标准的 Http Header、手机统计信息和指标、将响应从微服务发送给客户端等
4. ERROR: 发生错误时执行

#### zuul 回退
- microservice-gateway-zuul-fallback 实现 ZuulFallbackProvider 接口，和 Feign 的回退类似，当服务不可用的时候提供一个默认的响应

#### zuul 高可用
注册多个 zuulServer 到 eureka

#### 整合非 jvm 的微服务


## 第九章 config

配置文件名的格式为 {application}-{profile}.properties 或 yml 格式

microservice-config-server 配置服务端

查看配置可通过一下路径格式
```
/{application}/{profile}/[{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties

```
#### 手动刷新配置
- microservice-config-client-refresh 

使用 /refresh 断点手动刷新， 依赖 org.springframework.boot:spring-boot-starter-actuator 包
记得在controller添加 @RefreshScope

#### 利用 spring cloud bus + mq 实现自动刷新
- microservice-config-client-refresh-cloud-bus 支持自动更新配置的配置服务 config server
- microservice-config-server-refresh-cloud-bus 支持自动更新配置的消费者 config client

启动注意事项： rabbitmq 处于开启状态， git 仓库能调通 configServer 

在 git 的 webhook 上配置 configServer 的 xxx/bus/refresh 就可以在配置更新时候推送消息到所有客户端，通知其更新配置；如果网络不通
可以通过在命令行 curl -X POST xx/bus/refresh 来手动触发进行调试

配置的自动刷新基本原理（猜）： 以 rabbitmq 为例，通过 spring cloud bus 连接的 config server 和 config client，各自分监听一个队列， 
当 git 仓库触发webhook的时候 调用 {configServerUrl}/bus/refresh 广播更新消息，服务监听到消息则重新获取配置到本地

#### config server 配合 eureka 使用

- microservice-config-server-refresh-cloud-bus-eureka    支持自动更新配置并注册到 euraka 的配置服务 config server
- microservice-config-client-refresh-cloud-bus-eureka    支持自动更新配置并通过 eureka 与config server交互的消费者 config client

config server 注册到 eureka，参考 microservice-provider-user ; 

config client 配置调整

```
spring:
  application:
    name: microservice-foo # 对应 config server 所获取的配置文件的 {application}
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin

  cloud:
    config: 
      profile: dev # 对应 config server 所获取配置文件的 {profile}
      label: master
      discovery: # uri 改成服务发现的配置
        enabled: true
        service-id: microservice-config-server # 对应注册到 eureka 的 config Server应用名称
```
启动顺序：  microservice-discovery-eureka -> microservice-config-server-refresh-cloud-bus-eureka -> microservice-config-client-refresh-cloud-bus-eureka

#### 其他：
config server 增加用户认证功能
```
// config server配置增加
security:
 basic:
  enabled: true
user:
 name: user
  password: password123

// 客户端 uri 调整
spring:
 cloud:
  config:
   uri: http://{user}:{password}@localhost:{port}/
```

部署多个 config server 以实现 ha

## 第十章 Sleuth 实现微服务跟踪
-  microservice-simple-provider-user-trace
-  microservice-simple-consumer-movie-trace

还可以和 elk 或 zipkin 结合使用