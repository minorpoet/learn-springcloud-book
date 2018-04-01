package pri.holysu.springcloud.microservicegatewayzuulfilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import pri.holysu.springcloud.microservicegatewayzuulfilter.filter.PreRequestLogFilter;

@SpringBootApplication
@EnableZuulProxy
public class MicroserviceGatewayZuulFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceGatewayZuulFilterApplication.class, args);
	}

	@Bean
	public PreRequestLogFilter preRequestLogFilter(){
		return new PreRequestLogFilter();
	}
}
