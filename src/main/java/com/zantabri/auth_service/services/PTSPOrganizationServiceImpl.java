package com.zantabri.auth_service.services;

import com.zantabri.auth_service.repositories.PTPSOrganizationRepository;
import com.zantabri.auth_service.errors.ResourceNotFoundException;
import com.zantabri.auth_service.model.PTSPOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PTSPOrganizationServiceImpl implements IPTSPOrganizationService {


    private PTPSOrganizationRepository ptpsOrganizationRepository;

    @Autowired
    public PTSPOrganizationServiceImpl(PTPSOrganizationRepository ptpsOrganizationRepository) {
        this.ptpsOrganizationRepository = ptpsOrganizationRepository;
    }

    @Override
    public Page<PTSPOrganization> getPagedPTSPOrganizationsList(int page, int count, String sortBy, String sortDirection) {

        int _page = page <= 0 ? 1 : page;
        int _count = count <= 0 ? 10 : count;
        String _sortBy = sortBy == null ? "businessName" : sortBy;
        String _sortDirection = sortDirection == null ? "asc" : sortDirection;
        Pageable pageable = PageRequest.of(_page, _count, Sort.Direction.fromString(_sortDirection), _sortBy);
        return ptpsOrganizationRepository.findAll(pageable);

    }

    @Override
    public PTSPOrganization getPTSPOrganization(Long organizationID) {

        Optional<PTSPOrganization> opt =  ptpsOrganizationRepository.findById(organizationID);

        if (opt.isEmpty()) {
            throw new ResourceNotFoundException(organizationID,PTSPOrganization.class);
        }

        return opt.get();
    }

    @Override
    public void updatePTSPOrganization(Long organizationID, PTSPOrganization organization) {

        organization.setOrganizationId(organizationID);
        ptpsOrganizationRepository.save(organization);

    }

    @Override
    public PTSPOrganization addPTSPOrganization(PTSPOrganization organization) {
        return ptpsOrganizationRepository.save(organization);
    }

}
