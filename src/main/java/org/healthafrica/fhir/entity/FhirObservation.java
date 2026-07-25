package org.healthafrica.fhir.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.healthafrica.shared.entity.BaseEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "fhir_observation")
public class FhirObservation extends BaseEntity {

    private Long patientId;

    private String observationType;

    private String value;

    private Instant timestamp;

    @Version
    private Integer version;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String resourceJson;
}