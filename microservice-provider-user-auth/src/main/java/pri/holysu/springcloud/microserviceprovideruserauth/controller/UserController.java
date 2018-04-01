package pri.holysu.springcloud.microserviceprovideruserauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pri.holysu.springcloud.microserviceprovideruserauth.entity.User;
import pri.holysu.springcloud.microserviceprovideruserauth.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {

        Object pricipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (pricipal instanceof UserDetails) {
            UserDetails user = (UserDetails) pricipal;
            Collection<? extends GrantedAuthority> collection = user.getAuthorities();
            for (GrantedAuthority c : collection) {
                UserController.LOGGER.info("当前用户是{}, 角色是{}", user.getUsername(), c.getAuthority());
            }
        }
        User found = this.userRepository.findOne(id);
        return found;
    }

    @GetMapping("/all")
    public List<User> all() {
        List<User> userList = this.userRepository.findAll();
        return userList;
    }
}
