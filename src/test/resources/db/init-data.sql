insert into Organization (organizationId, address, businessName, contactNumber, contactPerson, dateCreated, state)
values (1, '9 elizabeth streeet, martins, lagos', 'lizb', '08485452124', 'dapsido', CURRENT_TIMESTAMP, 'Lagos');

insert into PTSPOrganization (LicenseExpiryDate, licenseIssueDate, organizationId)
values ('2023-01-01', '2020-01-01', 1);

insert into AccountDetails(username, email, activated, first_name , last_name, password , telephone, organization_id)
values ('dman2022', 'dman2022@outlook.com', true, 'dapo', 'main', 'pass', '080654543432', 1);

insert into UserRole(id, role) values (1, 'admin'), (2, 'user'), (3, 'super_admin');

insert into AccountDetails_UserRole(AccountDetails_username, authorities_id) values ('dman2022', 2);


--insert insert Organization(DTYPE, organizationId, address, businessName, contactNumber,contactPerson,dateCreated,state,businessType,LicenseExpiryDate,licenseIssueDate ,parent_organization_id)
--values()
