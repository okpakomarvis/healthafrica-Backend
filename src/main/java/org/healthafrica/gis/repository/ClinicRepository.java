package org.healthafrica.gis.repository;

import org.healthafrica.gis.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for clinic geospatial queries using PostGIS.
 */
public interface ClinicRepository extends JpaRepository<Clinic, Long> {

    @Query(value = """
            SELECT * FROM clinic c
            WHERE c.tenant_id = :tenantId
              AND ST_DWithin(
                    c.location::geography,
                    ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                    :radiusMeters
              )
            """, nativeQuery = true)
    List<Clinic> findNearby(
            @Param("tenantId") String tenantId,
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radiusMeters") double radiusMeters);
}
