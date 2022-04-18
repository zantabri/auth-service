package com.zantabri.auth_service.repositories;

import com.zantabri.auth_service.model.PTSPOrganization;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PTPSOrganizationRepository extends PagingAndSortingRepository<PTSPOrganization, Long> {

}
