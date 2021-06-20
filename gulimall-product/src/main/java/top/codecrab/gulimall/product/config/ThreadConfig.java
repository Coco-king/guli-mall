package top.codecrab.gulimall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author codecrab
 * @since 2021年06月20日 20:15
 */
@Configuration
public class ThreadConfig {

    @Bean
    public ThreadPoolExecutor executor(ThreadProperties threadProperties) {
        return new ThreadPoolExecutor(
                threadProperties.getCoreSize(),
                threadProperties.getMaxSize(),
                threadProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

}
