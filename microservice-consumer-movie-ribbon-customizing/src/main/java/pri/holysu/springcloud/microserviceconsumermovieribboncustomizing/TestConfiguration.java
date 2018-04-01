package pri.holysu.springcloud.microserviceconsumermovieribboncustomizing;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClient(name = "microservice-provider-user", configuration = RibbonConfiguration.class)
public class TestConfiguration {

}
