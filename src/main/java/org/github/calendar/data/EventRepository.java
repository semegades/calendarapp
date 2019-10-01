package org.github.calendar.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository for Event entities.
 * Does all searches to database
 */
public interface EventRepository extends CrudRepository<Event, Long> {
	
	
	/**
	 * Does time-based search. Any parameter
	 * can be null and is just left out from query.  
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return
	 */
	@Query("select e from Event e where "
			+ "(:year is null or year(e.date) = :year ) and "
			+ "(:month is null or month(e.date) = :month ) and "
			+ "(:day is null or day(e.date) = :day ) and "
			+ "(:hour is null or hour(e.date) = :hour) and"
			+ "(:minute is null or minute(e.date) = :minute) ")
	List<Event> findByTimeParams(
			@Param("year") Integer year,
			@Param("month") Integer month,
			@Param("day") Integer day,
			@Param("hour") Integer hour,
			@Param("minute") Integer minute
	);
	
	//Finds events by name
	@Query("SELECT e FROM Event e WHERE LOWER(e.name) LIKE LOWER ( CONCAT('%',:name,'%') ) ")
	List<Event> findByName(@Param("name") String name);
		
	//Finds events that are ongoing
	@Query("SELECT e FROM Event e WHERE EXTRACT( EPOCH FROM (current_timestamp())) BETWEEN EXTRACT( EPOCH FROM (e.date)) AND  EXTRACT( EPOCH FROM (e.date)) + e.length * 60 * 60 ")
	List<Event> findOngoingEvents();
			
}
