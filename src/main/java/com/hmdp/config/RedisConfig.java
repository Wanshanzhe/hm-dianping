package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 完善者
 * @date 2024/3/13
 * @desc
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedissonClient redissonClient() {
        // 配置类
        Config config = new Config();
        // 添加redis地址，这里添加了单点的地址，也可以使用config.useClusterServers()添加集群地址
        // .setPassword() 省略
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        // 创建客户端
        return Redisson.create(config);
    }
}
