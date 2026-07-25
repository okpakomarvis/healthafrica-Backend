package org.healthafrica.gis.dto;

import java.util.List;

public record GeoJsonResponse(

        String type,

        List<GeoJsonFeature> features

) {
}