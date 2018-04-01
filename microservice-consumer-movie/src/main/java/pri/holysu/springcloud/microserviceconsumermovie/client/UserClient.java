package pri.holysu.springcloud.microserviceconsumermovie.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import pri.holysu.springcloud.microserviceconsumermovie.entity.User;

import java.util.List;

@FeignClient(name="microservice-provider-user")
public interface UserClient {

    @GetMapping("/user/all")
    List<User> findAll();
}
