package pri.holysu.springcloud.microserviceprovideruser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pri.holysu.springcloud.microserviceprovideruser.entity.User;
import pri.holysu.springcloud.microserviceprovideruser.repository.UserRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        User found = this.userRepository.findOne(id);
//        try {
//           TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return found;
    }

    @GetMapping("/all")
    public List<User> all(){
        List<User> userList = this.userRepository.findAll();
        return userList;
    }
}
