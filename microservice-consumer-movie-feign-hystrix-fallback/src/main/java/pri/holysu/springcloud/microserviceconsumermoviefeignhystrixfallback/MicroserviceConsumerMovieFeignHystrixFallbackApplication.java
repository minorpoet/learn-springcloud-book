package pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// 启用 feign 客户端
@EnableFeignClients
@EnableDiscoveryClient
public class MicroserviceConsumerMovieFeignHystrixFallbackApplication {

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceConsumerMovieFeignHystrixFallbackApplication.class, args);
	}
}
