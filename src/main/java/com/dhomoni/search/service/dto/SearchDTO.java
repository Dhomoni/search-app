package com.dhomoni.search.service.dto;

import java.util.Optional;

import com.esotericsoftware.kryo.NotNull;
import com.vividsolutions.jts.geom.Point;

import lombok.Data;

/**
 * A DTO for the Doctor entity.
 */
@Data
public class SearchDTO {

	@NotNull
	private String query;
	private Point location;
	
	public Optional<Point> getLocation() {
		return Optional.ofNullable(location);
	}
}
