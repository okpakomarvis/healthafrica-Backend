package org.healthafrica.openehr.entity;

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
@Table(name = "openehr_composition")
public class Composition extends BaseEntity {

    @Column(nullable = false)
    private String archetypeId;

    @Column(nullable = false)
    private Long patientId;

    @Version
    private Integer version;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String content;

    private Instant timestamp;
}