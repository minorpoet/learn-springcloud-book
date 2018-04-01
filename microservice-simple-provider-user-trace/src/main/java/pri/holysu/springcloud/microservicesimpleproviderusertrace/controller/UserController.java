package pri.holysu.springcloud.microservicesimpleproviderusertrace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pri.holysu.springcloud.microservicesimpleproviderusertrace.entity.User;
import pri.holysu.springcloud.microservicesimpleproviderusertrace.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        User found = this.userRepository.findOne(id);
        return found;
    }
}
