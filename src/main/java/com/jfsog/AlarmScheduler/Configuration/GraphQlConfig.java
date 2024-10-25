package com.jfsog.AlarmScheduler.Configuration;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlConfig {
    @Bean
    public RuntimeWiringConfigurer extendedDateTimeScalar() {
        return wiring -> wiring.scalar(ExtendedScalars.DateTime);
    }
}
