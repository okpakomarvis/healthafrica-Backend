package org.healthafrica.audit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.healthafrica.audit.dto.AuditResponse;
import org.healthafrica.audit.service.AuditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Audit", description = "Immutable audit trail")
public class AuditController {

    private final AuditService service;

    @GetMapping
    @Operation(summary = "List audit logs", description = "Returns audit events for the current tenant.")
    public List<AuditResponse> getAll() {

        return service.findAll();
    }
}
