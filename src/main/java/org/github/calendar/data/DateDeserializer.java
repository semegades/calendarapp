package org.github.calendar.data;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Class that handles Date -> JSON deserialization
 */
public class DateDeserializer extends JsonDeserializer<Date> {
	
	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		String dateAsText = p.getText();
		
		try {
			return format.parse(dateAsText);
		} catch(Exception e) {
			return null;
		}
	}

}
