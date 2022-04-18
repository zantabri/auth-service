package com.zantabri.auth_service.services;

import com.zantabri.auth_service.model.PTSPOrganization;
import org.springframework.data.domain.Page;

public interface IPTSPOrganizationService {

    /**
     *
     * @param page
     * @param count
     * @return
     */
    Page<PTSPOrganization> getPagedPTSPOrganizationsList(int page, int count, String sortBy, String sortDirection);

    /**
     *
     * @param organizationID
     * @return
     */
    PTSPOrganization getPTSPOrganization(Long organizationID);

    /**
     *
     * @param orgaizationID
     * @param organization
     */
    void updatePTSPOrganization(Long orgaizationID, PTSPOrganization organization);

    /**
     *
     * @param organization
     * @return
     */
    PTSPOrganization addPTSPOrganization(PTSPOrganization organization);

}
