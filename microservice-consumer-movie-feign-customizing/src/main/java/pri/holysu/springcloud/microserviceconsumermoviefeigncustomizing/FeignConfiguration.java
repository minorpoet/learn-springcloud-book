package pri.holysu.springcloud.microserviceconsumermoviefeigncustomizing;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feignn 的配置类
 * 不应在主程序上下文的 @ComponentScan 中, 否则会被所有 FeignClient 共享
 */
@Configuration
public class FeignConfiguration {

    /**
     * 将契约改为 feign 原生的默认契约，然后就可以使用 feign 自带的注解了
     * @return
     */

    @Bean
    public Contract feignContract(){
        return new feign.Contract.Default();
    }

}
