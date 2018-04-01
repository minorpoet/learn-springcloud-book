package pri.holysu.springcloud.microserviceconfigclientrefresh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
public class MicroserviceConfigClientRefreshApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceConfigClientRefreshApplication.class, args);
	}
}
