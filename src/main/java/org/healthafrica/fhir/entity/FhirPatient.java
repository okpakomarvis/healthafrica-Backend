package org.healthafrica.fhir.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.healthafrica.shared.entity.BaseEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@Entity
@Table(name = "fhir_patient")
public class FhirPatient extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String gender;

    private java.time.LocalDate birthDate;

    @Column(nullable = false)
    private String clientReferenceId;

    @Version
    private Integer version;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String resourceJson;

    private Long createdByUserId;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;
}
