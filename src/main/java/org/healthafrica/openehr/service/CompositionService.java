package org.healthafrica.openehr.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.openehr.dto.*;
import org.healthafrica.openehr.entity.Composition;
import org.healthafrica.openehr.repository.CompositionRepository;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CompositionService {

    private final CompositionRepository repository;

    public CompositionResponse create(
            CreateCompositionRequest request) {

        Composition composition =
                new Composition();

        composition.setTenantId(
                TenantContextHolder.getTenant());

        composition.setPatientId(
                request.patientId());

        composition.setArchetypeId(
                request.archetypeId());

        composition.setContent(
                request.content());

        composition.setTimestamp(
                Instant.now());

        repository.save(composition);

        return map(composition);
    }

    public CompositionResponse get(
            Long id) {

        Composition composition =
                repository.findByIdAndTenantId(
                                id,
                                TenantContextHolder.getTenant())
                        .orElseThrow();

        return map(composition);
    }

    private CompositionResponse map(
            Composition composition) {

        return new CompositionResponse(
                composition.getId(),
                composition.getTenantId(),
                composition.getArchetypeId(),
                composition.getPatientId(),
                composition.getContent(),
                composition.getTimestamp(),
                composition.getVersion()
        );
    }
}
