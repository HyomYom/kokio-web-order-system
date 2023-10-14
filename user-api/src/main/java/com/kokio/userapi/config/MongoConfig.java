package com.kokio.userapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.kokio.entitymodule.domain.user.repository")
public class MongoConfig {

}
