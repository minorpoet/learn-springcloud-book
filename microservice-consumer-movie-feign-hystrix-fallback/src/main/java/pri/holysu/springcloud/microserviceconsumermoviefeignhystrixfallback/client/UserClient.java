package pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallback.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallback.entity.User;

import java.util.ArrayList;
import java.util.List;

/** 通过设置 @FeignClient 的 fallback 属性来指定回退策略 */
//@FeignClient(name = "microservice-provider-user", fallback = FeignClientFallback.class)

/** 测试硬编码 url 是否可以使用回退*/
@FeignClient(name="microservice-provider-user", url="localhost:8000", fallback = FeignClientFallback.class)
public interface UserClient {

    @GetMapping("/user/all")
    List<User> findAll();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    User findById(@PathVariable("id") Long id);
}

@Component
class FeignClientFallback implements UserClient {
    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setId(-1L);
        user.setName("default user");
        userList.add(user);
        return userList;
    }

    @Override
    public User findById(Long id) {
        User user = new User();
        user.setId(-1L);
        user.setName("default user");
        return user;
    }
}
