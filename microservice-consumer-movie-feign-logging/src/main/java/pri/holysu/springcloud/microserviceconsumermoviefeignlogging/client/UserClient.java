package pri.holysu.springcloud.microserviceconsumermoviefeignlogging.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pri.holysu.springcloud.microserviceconsumermoviefeignlogging.entity.User;
import pri.holysu.springcloud.microserviceconsumermoviefeignlogging.feignconfig.FeignLogConfiguration;

import java.util.List;

/**
 * 声明 feign
 * 指明调用的应用名称，如果启用 eureka 会自动进行负载均衡
 */
@FeignClient(name = "microservice-provider-user",configuration = FeignLogConfiguration.class)
public interface UserClient {

    @GetMapping("/user/all")
    List<User> findAll();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    User findById(@PathVariable("id") Long id);
}
