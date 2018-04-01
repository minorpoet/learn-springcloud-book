package pri.holysu.springcloud.microserviceconsumermoviefeigncustomizing.client;

import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pri.holysu.springcloud.microserviceconsumermoviefeigncustomizing.FeignConfiguration;
import pri.holysu.springcloud.microserviceconsumermoviefeigncustomizing.entity.User;

import java.util.List;

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
