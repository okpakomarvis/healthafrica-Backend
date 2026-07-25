package org.healthafrica.gis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.healthafrica.shared.entity.BaseEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

/**
 * Health facility with a geospatial location for proximity queries.
 */
@Getter
@Setter
@Entity
@Table(name = "clinic")
public class Clinic extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point location;
}
