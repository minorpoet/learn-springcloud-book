package pri.holysu.springcloud.microserviceconsumermovie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pri.holysu.springcloud.microserviceconsumermovie.client.UserClient;
import pri.holysu.springcloud.microserviceconsumermovie.entity.User;

import java.util.List;

@RestController
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 硬编码 url 调用
     *
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public User findById(@PathVariable Long id) {
        User user = this.restTemplate.getForObject("http://localhost:8000/user/" + id, User.class);
        return user;
    }


    @Autowired
    UserClient userClient;

    /**
     * 通过 feign 调用
     *
     * @return
     */
    @GetMapping("/user/all")
    public List<User> findAll() {
        return userClient.findAll();
    }

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/serverinfo")
    public List<ServiceInstance> getServiceInfo() {
        return this.discoveryClient.getInstances("microservice-provider-user");
    }
}
