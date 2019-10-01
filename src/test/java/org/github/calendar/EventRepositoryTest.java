package org.github.calendar;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.github.calendar.data.Event;
import org.github.calendar.data.EventRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

//THIS DOES NOT COVER ALL POSSIBLE TESTS THAT CAN BE DONE!

@RunWith(SpringRunner.class)
@DataJpaTest
public class EventRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private EventRepository eventRepo;
	
	@Test
	public void testtSaveEvent() {
		
		Date date = new Date();
		
		Event event = new Event("Halloween", 12, date);
		entityManager.persistAndFlush(event);
		Assert.assertNotNull(event.getId());
		
		Optional<Event> eventFromRepo = eventRepo.findById(event.getId());
		
		Assert.assertTrue(eventFromRepo.isPresent());
		Assert.assertNotNull(eventFromRepo.get().getId());
		Assert.assertEquals("Halloween", eventFromRepo.get().getName());
		Assert.assertEquals(12, eventFromRepo.get().getLength().intValue());
		Assert.assertEquals(date, eventFromRepo.get().getDate());
		Event anotherEvent = new Event("Provinssi Rock", 72, new Date());
		entityManager.persistAndFlush(anotherEvent);
		
		Iterable<Event> events = eventRepo.findAll();
		Assert.assertEquals(2, ((Collection<?>) events).size());
		
	}
	
	/**
	 * Tests EventRepository#findByTimeParams method
	 */
	@Test
	public void testFindByTimeParams() {
		
		//Event: 2019-10-10 11:12
		Event event = new Event("Something", 12, generateDate(2019, 9, 10, 11, 12));
		entityManager.persistAndFlush(event);
		
		List<Event> events;
		
		//Find by year
		events = eventRepo.findByTimeParams(2019, null, null, null, null);
		Assert.assertEquals(1, events.size());
				
		//Find by month (Must be searched as ten because generated Date is 0-based month)
		//So Event was actually saved as October. Quirks of Calendar object
		events = eventRepo.findByTimeParams(null, 10, null, null, null);
		Assert.assertEquals(1, events.size());
		
		//Find by Day
		events = eventRepo.findByTimeParams(null, null, 10, null, null);
		Assert.assertEquals(1, events.size());
		
		//Find by time
		events = eventRepo.findByTimeParams(null, null, null, 11, 12);
		Assert.assertEquals(1, events.size());
		
		//Second Event: 2019-10-11 11:12
		Event event2 = new Event("Something", 12, generateDate(2019, 9, 11, 11, 12));
		entityManager.persistAndFlush(event2);
		
		//Find by Year
		events = eventRepo.findByTimeParams(2019, null, null, null, null);
		Assert.assertEquals(2, events.size());
		
		//Find by Month
		events = eventRepo.findByTimeParams(null, 10, null, null, null);
		Assert.assertEquals(2, events.size());
		
		//Find by Day (Should be only one since the day differs from two different events)
		events = eventRepo.findByTimeParams(null, null, 10, null, null);
		Assert.assertEquals(1, events.size());
		
		//Find by time
		events = eventRepo.findByTimeParams(null, null, null, 11, 12);
		Assert.assertEquals(2, events.size());
				
	}
	
	@Test
	public void testEventsByName() {
		
		//Add some Events...
		Event event = new Event("Halloween", 12, new Date());
		Event event2 = new Event("Provinssi Rock", 12, new Date());
		Event event3 = new Event("Ilosaari Rock", 12, new Date());
		Event event4 = new Event("Qstock", 12, new Date());
		Event event5 = new Event("Wood Stock", 12, new Date());
		
		//Save
		entityManager.persistAndFlush(event);
		entityManager.persistAndFlush(event2);
		entityManager.persistAndFlush(event3);
		entityManager.persistAndFlush(event4);
		entityManager.persistAndFlush(event5);
		
		//Find with different names and check results
		List<Event> events = eventRepo.findByName("halloween");
		Assert.assertEquals(1, events.size());
		
		events = eventRepo.findByName("rock");
		Assert.assertEquals(2, events.size());
		
		events = eventRepo.findByName("stock");
		Assert.assertEquals(2, events.size());
		
		events = eventRepo.findByName("ock");
		Assert.assertEquals(4, events.size());
		
		events = eventRepo.findByName("nothing");
		Assert.assertEquals(0, events.size());
		
	}
	
	private Date generateDate(int year, int month, int date, int hourOfDay, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, date, hourOfDay, minute);
		return calendar.getTime();
	}
	
	
}
