package org.github.calendar;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.github.calendar.data.DateDeserializer;
import org.github.calendar.data.Event;
import org.github.calendar.data.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SpringBootApplication
@EnableAutoConfiguration(exclude = RepositoryRestMvcAutoConfiguration.class)
public class CalendarApplication {
	
	@Autowired
	EventRepository eventRepo;
		
	public static void main(String[] args) {
		SpringApplication.run(CalendarApplication.class, args);
	}
	
	/**
	 * JSON Settings:
	 * - Set DateFormat
	 * - Set Timezone to System's default timezone
	 * - Set Deserializer for Date classes
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustom() {
		return builder -> {
			builder.simpleDateFormat("dd.MM.yyyy HH:mm");
			builder.timeZone(TimeZone.getDefault());
			builder.deserializerByType(Date.class, new DateDeserializer());
		};
	}
			
}
