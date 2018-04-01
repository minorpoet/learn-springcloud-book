package pri.holysu.springcloud.microserviceconsumermovieribbon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pri.holysu.springcloud.microserviceconsumermovieribbon.client.UserClient;
import pri.holysu.springcloud.microserviceconsumermovieribbon.entity.User;

import java.util.List;

@RestController
public class MovieController {
    private static final String SERVER_HOST = "microservice-provider-user";
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * 硬编码 url 调用
     *
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public User findById(@PathVariable Long id) {
        User user = this.restTemplate.getForObject("http://" + SERVER_HOST + "/user/" + id, User.class);

        ServiceInstance serviceInstance = this.loadBalancerClient.choose(SERVER_HOST);
        System.out.println(serviceInstance.getServiceId() + ":" + serviceInstance.getHost() + ":" + serviceInstance.getPort());

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
        return this.discoveryClient.getInstances(SERVER_HOST);
    }
}
