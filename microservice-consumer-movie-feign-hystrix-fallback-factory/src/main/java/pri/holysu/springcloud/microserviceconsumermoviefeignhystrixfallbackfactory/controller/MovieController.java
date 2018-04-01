package pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallbackfactory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallbackfactory.client.FeignUserClient;
import pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallbackfactory.entity.User;

import java.util.List;

@RestController
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    FeignUserClient feignUserClient;

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

    @GetMapping("/serverinfo")
    public List<ServiceInstance> getServiceInfo() {
        return this.discoveryClient.getInstances("microservice-provider-user");
    }
}
