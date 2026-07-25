package org.healthafrica.campaign.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "vaccination_campaign_site")
@Getter
@Setter
public class VaccinationCampaignSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    private LocalDate activeFrom;

    private LocalDate activeTo;

    @Column(nullable = false)
    private boolean enabled = true;

    private Long createdByUserId;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();
}
