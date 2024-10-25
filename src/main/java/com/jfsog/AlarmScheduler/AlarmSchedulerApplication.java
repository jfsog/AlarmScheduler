package com.jfsog.AlarmScheduler;

import com.jfsog.AlarmScheduler.Services.Alarm;
import graphql.scalars.ExtendedScalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AlarmSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmSchedulerApplication.class, args);
		var t=ExtendedScalars.DateTime;
	}

}
