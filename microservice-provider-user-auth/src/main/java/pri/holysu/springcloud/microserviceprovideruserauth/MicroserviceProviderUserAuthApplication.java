package pri.holysu.springcloud.microserviceprovideruserauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication

// 启动服务发现客户端
@EnableDiscoveryClient
public class MicroserviceProviderUserAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceProviderUserAuthApplication.class, args);
    }


}
