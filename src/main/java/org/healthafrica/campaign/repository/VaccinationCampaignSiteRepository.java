package org.healthafrica.campaign.repository;

import org.healthafrica.campaign.entity.VaccinationCampaignSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VaccinationCampaignSiteRepository
        extends JpaRepository<VaccinationCampaignSite, Long> {

    List<VaccinationCampaignSite> findByTenantId(String tenantId);

    Optional<VaccinationCampaignSite> findByIdAndTenantId(Long id, String tenantId);
}
