package com.dhomoni.search.service.dto;

import java.util.Optional;

import com.esotericsoftware.kryo.NotNull;
import com.vividsolutions.jts.geom.Point;

/**
 * A DTO for the Doctor entity.
 */
public class SearchDTO {

	@NotNull
	private String query;
	private Point location;
	private Double radius;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public Optional<Point> getLocation() {
		return Optional.ofNullable(location);
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Optional<Double> getRadius() {
		return Optional.ofNullable(radius);
	}
	
	public void setRadius(Double radius) {
		this.radius = radius;
	}
}
