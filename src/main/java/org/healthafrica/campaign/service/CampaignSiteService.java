package org.healthafrica.campaign.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.auth.security.SecurityUtils;
import org.healthafrica.campaign.dto.*;
import org.healthafrica.campaign.entity.VaccinationCampaignSite;
import org.healthafrica.campaign.repository.VaccinationCampaignSiteRepository;
import org.healthafrica.gis.dto.GeoJsonFeature;
import org.healthafrica.gis.dto.GeoJsonResponse;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CampaignSiteService {

    private final VaccinationCampaignSiteRepository repository;
    private final GeometryFactory geometryFactory;

    public List<CampaignSiteResponse> list() {
        return repository.findByTenantId(TenantContextHolder.getTenant()).stream()
                .map(this::map)
                .toList();
    }

    public CampaignSiteResponse create(CreateCampaignSiteRequest request) {
        VaccinationCampaignSite site = new VaccinationCampaignSite();
        site.setTenantId(TenantContextHolder.getTenant());
        site.setName(request.name());
        site.setDescription(request.description());
        site.setLocation(toPoint(request.latitude(), request.longitude()));
        site.setActiveFrom(request.activeFrom());
        site.setActiveTo(request.activeTo());
        site.setCreatedByUserId(SecurityUtils.currentUserIdLong());
        repository.save(site);
        return map(site);
    }

    public CampaignSiteResponse update(Long id, UpdateCampaignSiteRequest request) {
        VaccinationCampaignSite site = find(id);
        if (request.name() != null) {
            site.setName(request.name());
        }
        if (request.description() != null) {
            site.setDescription(request.description());
        }
        if (request.latitude() != null && request.longitude() != null) {
            site.setLocation(toPoint(request.latitude(), request.longitude()));
        }
        if (request.activeFrom() != null) {
            site.setActiveFrom(request.activeFrom());
        }
        if (request.activeTo() != null) {
            site.setActiveTo(request.activeTo());
        }
        if (request.enabled() != null) {
            site.setEnabled(request.enabled());
        }
        repository.save(site);
        return map(site);
    }

    public GeoJsonResponse mapSites() {
        List<GeoJsonFeature> features = new ArrayList<>();
        for (VaccinationCampaignSite site : repository.findByTenantId(TenantContextHolder.getTenant())) {
            if (!site.isEnabled() || site.getLocation() == null) {
                continue;
            }
            Map<String, Object> geometry = Map.of(
                    "type", "Point",
                    "coordinates", List.of(
                            site.getLocation().getX(),
                            site.getLocation().getY()));
            Map<String, Object> properties = new HashMap<>();
            properties.put("id", site.getId());
            properties.put("name", site.getName());
            properties.put("description", site.getDescription());
            properties.put("activeFrom", site.getActiveFrom());
            properties.put("activeTo", site.getActiveTo());
            features.add(new GeoJsonFeature("Feature", geometry, properties));
        }
        return new GeoJsonResponse("FeatureCollection", features);
    }

    private VaccinationCampaignSite find(Long id) {
        return repository.findByIdAndTenantId(id, TenantContextHolder.getTenant())
                .orElseThrow(() -> new NoSuchElementException("Campaign site not found"));
    }

    private Point toPoint(double latitude, double longitude) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(4326);
        return point;
    }

    private CampaignSiteResponse map(VaccinationCampaignSite site) {
        Double lat = site.getLocation() != null ? site.getLocation().getY() : null;
        Double lng = site.getLocation() != null ? site.getLocation().getX() : null;
        return new CampaignSiteResponse(
                site.getId(),
                site.getTenantId(),
                site.getName(),
                site.getDescription(),
                lat,
                lng,
                site.getActiveFrom(),
                site.getActiveTo(),
                site.isEnabled(),
                site.getCreatedByUserId(),
                site.getCreatedAt());
    }
}
