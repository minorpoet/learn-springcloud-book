package pri.holysu.springcloud.microserviceconfigclientrefreshcloudbuseureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceConfigClientRefreshCloudBusEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceConfigClientRefreshCloudBusEurekaApplication.class, args);
	}
}
