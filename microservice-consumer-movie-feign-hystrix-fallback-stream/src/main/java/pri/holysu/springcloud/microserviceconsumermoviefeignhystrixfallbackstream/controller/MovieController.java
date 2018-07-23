package pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallbackstream.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallbackstream.client.UserClient;
import pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallbackstream.entity.User;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    UserClient userClient;

    /**
     * 硬编码 url 调用
     *
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    @HystrixCommand(fallbackMethod = "findByIdFallback")
    public User findById(@PathVariable Long id) {
        User user = this.restTemplate.getForObject("http://localhost:8000/user/" + id, User.class);
        return user;
    }

    /**
     * 通过 feign 调用
     *
     * @return
     */
    @GetMapping("/user/all")
    @HystrixCommand(fallbackMethod = "findALlFallback")
    public List<User> findAll() {
        return userClient.findAll();
    }

    @GetMapping("/serverinfo")
    public List<ServiceInstance> getServiceInfo() {
        return this.discoveryClient.getInstances("microservice-provider-user");
    }

    public User findByIdFallback(Long id) {
        User user = new User();
        user.setId(-1L);
        user.setName(("默认用户"));
        return user;
    }

    public List<User> findALlFallback(){
        List<User> all = new ArrayList<>();
        User user = new User();
        user.setId(-1L);
        user.setName(("默认用户"));

        all.add(user);
        return all;
    }
}
