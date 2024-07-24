package com.victiny.dida.config;

import com.victiny.dida.KeyConstant;
import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/23 11:21
 */
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {

    private String apiKey;

    @Bean
    public ClientV4 getClientV4(){
        return new ClientV4.Builder(apiKey).build();
    }
}
