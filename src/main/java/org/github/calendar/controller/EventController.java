package org.github.calendar.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.github.calendar.data.Event;
import org.github.calendar.data.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.events.EventException;

/**
 * Controller that handles all web requests
 */
@RestController
public class EventController {

	@Autowired
	private EventRepository eventRepo;
	
	@RequestMapping(path = "/events", method = RequestMethod.GET) 
	public Iterable<Event> getEvents(
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month,
			@RequestParam(required = false) Integer day,
			@RequestParam(required = false) String time) throws Exception {
		
		if(month != null || year != null || day != null || time != null) {
			
			Integer hour = null;
			Integer minutes = null;
			
			if(time != null) {
				//Parse time to hours and minutes if parameter is present
				Calendar parsedTime = parseTime(time);
				hour = parsedTime.get(Calendar.HOUR_OF_DAY);
				minutes = parsedTime.get(Calendar.MINUTE);
			}
			
			//Find events based on time parameters
			return eventRepo.findByTimeParams(year, month, day, hour, minutes);
			
		} else {
			//Just return all events
			return eventRepo.findAll();
		}
		
	}
	
	/**
	 * Gets all ongoing events
	 */
	@RequestMapping(path = "/eventsongoing", method = RequestMethod.GET)
	public Iterable<Event> getOngoingEvents() {
		return eventRepo.findOngoingEvents();
	}
	
	/**
	 * Gets events by name
	 */
	@RequestMapping(path = "/eventsbyname", method = RequestMethod.GET)
	public Iterable<Event> getEventsByName(
			@RequestParam(required = false) String name) throws EventControllerException {
		
		if(name != null) {
			return eventRepo.findByName(name);
		} else {
			throw new EventControllerException("Name required");
		}
		
	}
			
	/**
	 * Parses Time parameter
	 * @param time request parameter time
	 * @return Calendar objejct with EPOCH 0 except hour and minute fields are in place, throws exception otherwise
	 * @throws EventControllerException
	 */
	private Calendar parseTime(String time) throws EventControllerException {

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		
		try {
			
			//Parse given time-String to Date. It's EPOC 0 except hours and minutes are there
			//and that's only thing it's interesting here
			Date date = format.parse(time);
			Calendar dar = Calendar.getInstance();
			dar.setTime(date);
			return dar;
			
		} catch (ParseException e) {
			//Failed to parse time, throw EventControllerException and that is handled in Exception handler
			throw new EventControllerException("Failed to parse time parameter: " + time);
		}
	}
	
	/**
	 * ExceptionHandler. Handles errors when API is used incorrectly,
	 * fields are wrong type, fields missing etc..s
	 */
	@ExceptionHandler({EventControllerException.class, HttpMessageNotReadableException.class})
	public ResponseEntity<Object> handleError(Exception e) {		
		ErrorEntity error = new ErrorEntity(400, "Bad Request", e.getMessage());
		
		if(e instanceof HttpMessageNotReadableException) {
			error.setMessage("No data. Message body missing");
		}
		
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
		
	}
	
	/**
	 * Saves new event to database
	 */
	@RequestMapping(path = "/events", method = RequestMethod.POST)
	public ResponseEntity<Object> saveEvent(@RequestBody Event event) throws EventControllerException {
		
		//Check that none of the parameters are not null
		if(event.getLength() == null) {
			throw new EventControllerException("Length is missing");
		}
		
		if(event.getName() == null) {
			throw new EventControllerException("Name is missing");
		}
		
		if(event.getDate() == null) {
			throw new EventControllerException("Date is missing. Format: (yyyy.MM.dd HH:mm)");
		}
		
		eventRepo.save(event);
		return new ResponseEntity<Object>(event, HttpStatus.CREATED);
		
	}
	
}
