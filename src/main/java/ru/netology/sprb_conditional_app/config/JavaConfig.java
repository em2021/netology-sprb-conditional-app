package ru.netology.sprb_conditional_app.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.netology.sprb_conditional_app.profiles.DevProfile;
import ru.netology.sprb_conditional_app.profiles.ProductionProfile;
import ru.netology.sprb_conditional_app.profiles.SystemProfile;

@Configuration
public class JavaConfig {

    @Bean("prod")
    @ConditionalOnProperty(prefix = "netology", name = "profile.dev", havingValue = "false")
    public SystemProfile prodProfile() {
        return new ProductionProfile();
    }

    @Bean("dev")
    @ConditionalOnProperty(prefix = "netology", name = "profile.dev", havingValue = "true")
    public SystemProfile devProfile() {
        return new DevProfile();
    }
}
