package org.healthafrica.communityevents.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.healthafrica.shared.entity.BaseEntity;
import org.locationtech.jts.geom.Point;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Table(name = "vaccination_event")
@Getter
@Setter
public class VaccinationEvent extends BaseEntity {

    private Long patientId;

    private String vaccineName;

    private LocalDate dateAdministered;

    @Version
    private Long version;

    private Long recordedByUserId;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;
}
