package com.zantabri.auth_service;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zantabri.auth_service.controllers.PTSPOrganizationController;
import com.zantabri.auth_service.model.PTSPOrganization;
import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.services.IPTSPOrganizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@WebMvcTest(PTSPOrganizationController.class)
public class PTSPOrganizationControllerTest {

    private Logger logger = LoggerFactory.getLogger(PTSPOrganizationControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPTSPOrganizationService iptspOrganizationService;

    @MockBean
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @WithMockUser(roles = "SUPER_ADMIN")
    @Test
    public void testGetPTSPOrganizationBySuperAdminIsOk() throws Exception {

        PTSPOrganization organization = new PTSPOrganization();
        organization.setOrganizationId(1L);
        organization.setLicenseExpiryDate(LocalDate.of(2022, 1, 1));
        organization.setLicenseIssueDate(LocalDate.of(2023, 1, 1));
        organization.setAddress("9 Elizabeth Street, Off Martins, Lagos");
        organization.setBusinessName("Da Business");
        organization.setContactNumber("02056541254");
        organization.setContactPerson("Da Man");
        organization.setDateCreated(LocalDate.now());

        given(this.iptspOrganizationService.getPTSPOrganization(1L)).willReturn(organization);
        this.mockMvc.perform(get("/ptsp/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(this::logResult);

    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void testGetPTSPOrganizationByAdminIsOk() throws Exception {
        PTSPOrganization organization = buildPTSPOrganization(1l, LocalDate.of(2023,1,1), LocalDate.of(2022, 1, 1), "12 Queens road, surulere", "Da Business", "02086541254", "da man");

        given(this.iptspOrganizationService.getPTSPOrganization(1L)).willReturn(organization);
        this.mockMvc.perform(get("/ptsp/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(this::logResult);
    }

    @WithMockUser(roles = "USER")
    @Test
    public void testGetPTSPOrganizationByUserIsForbidden() throws Exception {

        PTSPOrganization organization = buildPTSPOrganization(1l, LocalDate.of(2023,1,1), LocalDate.of(2022, 1, 1), "12 Queens road, surulere", "Da Business", "02086541254", "da man");

        given(this.iptspOrganizationService.getPTSPOrganization(1L)).willReturn(organization);
        this.mockMvc.perform(get("/ptsp/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value())).andDo(this::logResult);

    }

    @WithMockUser(roles = "SUPER_ADMIN")
    @Test
    public void testGetAllPTSPOrganizationsBySuperAdminIsOK() throws Exception {

        given(this.iptspOrganizationService.getPagedPTSPOrganizationsList(1, 10, "businessName", "asc")).willReturn(listOfPTSPOrganizationsPage());
        this.mockMvc.perform(
                get("/ptsp").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(this::logResult);

    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void testGetAllPTSPOrganizationsByAdminIsForbidden() throws Exception {

        given(this.iptspOrganizationService.getPagedPTSPOrganizationsList(1, 10, "businessName", "asc")).willReturn(listOfPTSPOrganizationsPage());
        this.mockMvc.perform(
                        get("/ptsp").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));

    }

    @WithMockUser(roles = "USER")
    @Test
    public void testGetAllPTSPOrganizationsByUserIsForbidden() throws Exception {

        given(this.iptspOrganizationService.getPagedPTSPOrganizationsList(1, 10, "businessName", "asc")).willReturn(listOfPTSPOrganizationsPage());
        this.mockMvc.perform(
                        get("/ptsp").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));

    }

    @WithMockUser(roles = "SUPER_ADMIN")
    @Test
    public void testCreatePTSPOrganizationBySuperAdminIsOk() throws Exception {

        PTSPOrganization orgBefore = buildPTSPOrganization(null, LocalDate.of(2023,1,1), LocalDate.of(2022, 1, 1), "14 Queens road, surulere", "Da Business 4", "02086541256", "da man");

        PTSPOrganization orgAfter = buildPTSPOrganization(4l, LocalDate.of(2023,1,1), LocalDate.of(2022, 1, 1), "14 Queens road, surulere", "Da Business 4", "02086541256", "da man");

        given(this.iptspOrganizationService.addPTSPOrganization(any(PTSPOrganization.class))).willReturn(orgAfter);
        this.mockMvc.perform(
                        post("/ptsp").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(orgBefore))
                )
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andDo(this::logResult);

    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void testCreatePTSPOrganizationByAdminIsForbidden() throws Exception {

        PTSPOrganization orgBefore = buildPTSPOrganization(null, LocalDate.of(2023,1,1), LocalDate.of(2022, 1, 1), "14 Queens road, surulere", "Da Business 4", "02086541256", "da man");

        PTSPOrganization orgAfter = buildPTSPOrganization(4l, LocalDate.of(2023,1,1), LocalDate.of(2022, 1, 1), "14 Queens road, surulere", "Da Business 4", "02086541256", "da man");

        given(this.iptspOrganizationService.addPTSPOrganization(any(PTSPOrganization.class))).willReturn(orgAfter);
        this.mockMvc.perform(
                        post("/ptsp").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(orgBefore))
                )
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andDo(this::logResult);

    }

    @WithMockUser(roles = "USER")
    @Test
    public void testCreatePTSPOrganizationByUserIsForbidden() throws Exception {

        PTSPOrganization orgBefore = buildPTSPOrganization(null, LocalDate.of(2023,1,1), LocalDate.of(2022, 1, 1), "14 Queens road, surulere", "Da Business 4", "02086541256", "da man");

        PTSPOrganization orgAfter = buildPTSPOrganization(4l, LocalDate.of(2023,1,1), LocalDate.of(2022, 1, 1), "14 Queens road, surulere", "Da Business 4", "02086541256", "da man");

        given(this.iptspOrganizationService.addPTSPOrganization(any(PTSPOrganization.class))).willReturn(orgAfter);
        this.mockMvc.perform(
                        post("/ptsp").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(orgBefore))
                )
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andDo(this::logResult);

    }

    private PTSPOrganization buildPTSPOrganization(Long id, LocalDate expiryDate, LocalDate issueDate, String address, String businessName, String contactNumber, String contactPerson) {

        PTSPOrganization organization = new PTSPOrganization();
        organization.setOrganizationId(id);
        organization.setLicenseExpiryDate(expiryDate);
        organization.setLicenseIssueDate(issueDate);
        organization.setAddress(address);
        organization.setBusinessName(businessName);
        organization.setContactNumber(contactNumber);
        organization.setContactPerson(contactPerson);
        organization.setDateCreated(LocalDate.now());

        return organization;

    }

    private List<PTSPOrganization> listOfPTSPOrganizations() {

        return List.of(
                buildPTSPOrganization(1l, LocalDate.of(2023,1,1), LocalDate.of(2022, 1, 1), "11 Queens road, surulere", "Da Business 1", "02086541251", "da man1"),
                buildPTSPOrganization(2l, LocalDate.of(2025,1,1), LocalDate.of(2020, 1, 1), "12 Queens road, surulere", "Da Business 2", "02086541252", "da man2"),
                buildPTSPOrganization(3l, LocalDate.of(2022,11,1), LocalDate.of(2022, 1, 1), "13 Queens road, surulere", "Da Business 3", "02086541253", "da man3")
        );

    }

    private Page<PTSPOrganization> listOfPTSPOrganizationsPage() {

        Page<PTSPOrganization> page = new PageImpl<PTSPOrganization>(listOfPTSPOrganizations(), PageRequest.of(0, 3), 1);

        return page;

    }

    private void logResult(MvcResult result) throws UnsupportedEncodingException {
        logger.info("received {}", result.getResponse().getContentAsString());
    }

}
