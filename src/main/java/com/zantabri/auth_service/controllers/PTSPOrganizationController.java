package com.zantabri.auth_service.controllers;

import com.zantabri.auth_service.model.PTSPOrganization;
import com.zantabri.auth_service.services.IPTSPOrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ptsp")
public class PTSPOrganizationController {

    private final IPTSPOrganizationService iptspOrganizationService;

    public PTSPOrganizationController(IPTSPOrganizationService organizationService) {
        this.iptspOrganizationService = organizationService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<PTSPOrganization> getPTSPOrganizations(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int count, @RequestParam(required = false, defaultValue = "businessName") String sortBy, @RequestParam(required = false, defaultValue = "asc") String sortDirection) {

        return iptspOrganizationService.getPagedPTSPOrganizationsList(page, count, sortBy, sortDirection);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PTSPOrganization addPTSPOrganization(@RequestBody PTSPOrganization ptspOrganization) {
        return iptspOrganizationService.addPTSPOrganization(ptspOrganization);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public PTSPOrganization getPTSPOrganization(@PathVariable Long id) {
        return iptspOrganizationService.getPTSPOrganization(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updatePTSPOrganization(@PathVariable Long id,  @RequestBody PTSPOrganization ptspOrganization) {
        iptspOrganizationService.updatePTSPOrganization(id, ptspOrganization);
    }

}
