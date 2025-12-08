package com.project.vistaro.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.vistaro.model.Event;
import com.project.vistaro.util.EventRowMapper;

@Repository
public class EventDaoImpl implements EventDao {

	private final JdbcTemplate jdbc;

	public EventDaoImpl(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Event save(Event event) {
		String sql = """
				    INSERT INTO event(title, description, category, sub_category,
				                      banner_url, thumbnail_url, start_time, end_time)
				    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""";

		GeneratedKeyHolder kh = new GeneratedKeyHolder();

		jdbc.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, event.getTitle());
			ps.setString(2, event.getDescription());
			ps.setString(3, event.getCategory().name());
			ps.setString(4, event.getSubCategory());
			ps.setString(5, event.getBannerUrl());
			ps.setString(6, event.getThumbnailUrl());
			ps.setTimestamp(7, Timestamp.valueOf(event.getStartTime()));
			ps.setTimestamp(8, Timestamp.valueOf(event.getEndTime()));
			return ps;
		}, kh);

		if (kh.getKey() != null)
			event.setEventId(kh.getKey().intValue());
		return event;
	}

	@Override
	public Optional<Event> findById(int eventId) {
		String sql = "SELECT * FROM event WHERE event_id = ?";
		List<Event> list = jdbc.query(sql, new EventRowMapper(), eventId);
		return list.stream().findFirst();
	}

	@Override
	public List<Event> findAllByCity(String city) {
		String sql = """
				           SELECT e.*
				FROM event e
				JOIN eventslot es ON e.event_id = es.event_id
				JOIN venue v ON es.venue_id = v.venue_id
				WHERE v.city = ?
				GROUP BY e.event_id
				ORDER BY e.start_time ASC;

				        """;
		return jdbc.query(sql, new EventRowMapper(), city);
	}

	@Override
	public List<Event> searchByTitleAndCity(String title, String city) {
		String sql = """
				    SELECT DISTINCT e.*
				    FROM event e
				    JOIN eventslot es ON e.event_id = es.event_id
				    JOIN venue v ON es.venue_id = v.venue_id
				    WHERE v.city = ? AND LOWER(e.title) LIKE ?
				""";
		return jdbc.query(sql, new EventRowMapper(), city, "%" + title.toLowerCase() + "%");
	}

	@Override
	public List<Event> searchByCategoryAndCity(String category, String city) {
		String sql = """
				    SELECT DISTINCT e.*
				    FROM event e
				    JOIN eventslot es ON e.event_id = es.event_id
				    JOIN venue v ON es.venue_id = v.venue_id
				    WHERE v.city = ? AND e.category = ?
				""";
		return jdbc.query(sql, new EventRowMapper(), city, category);
	}

	@Override
	public List<Event> searchBySubCategoryAndCity(String subCategory, String city) {
		String sql = """
				    SELECT DISTINCT e.*
				    FROM event e
				    JOIN eventslot es ON e.event_id = es.event_id
				    JOIN venue v ON es.venue_id = v.venue_id
				    WHERE v.city = ? AND LOWER(e.sub_category) LIKE ?
				""";
		return jdbc.query(sql, new EventRowMapper(), city, "%" + subCategory.toLowerCase() + "%");
	}

	@Override
	public Event update(Event event) {
		String sql = """
				    UPDATE event SET
				        title=?, description=?, category=?, sub_category=?,
				        banner_url=?, thumbnail_url=?, start_time=?, end_time=?
				    WHERE event_id=?
				""";

		jdbc.update(sql, event.getTitle(), event.getDescription(), event.getCategory().name(), event.getSubCategory(),
				event.getBannerUrl(), event.getThumbnailUrl(), Timestamp.valueOf(event.getStartTime()),
				Timestamp.valueOf(event.getEndTime()), event.getEventId());

		return event;
	}

	@Override
	public int delete(Integer eventId) {
		// TODO Auto-generated method stub
		String sql = "Delete from event where event_id=?";
		return jdbc.update(sql,eventId);
		
	}
}
