package pri.holysu.springcloud.microserviceconfigserverrefreshcloudbuseureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class MicroserviceConfigServerRefreshCloudBusApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceConfigServerRefreshCloudBusApplication.class, args);
	}
}
