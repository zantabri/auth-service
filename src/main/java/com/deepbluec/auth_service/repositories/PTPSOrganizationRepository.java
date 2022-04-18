package com.deepbluec.auth_service.repositories;

import com.deepbluec.auth_service.model.PTSPOrganization;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PTPSOrganizationRepository extends PagingAndSortingRepository<PTSPOrganization, Long> {

}
