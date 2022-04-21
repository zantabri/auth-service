insert into AccountDetails(username, email, activated, first_name , last_name, password , telephone, organization_id)
values ('dman2022', 'dman2022@outlook.com', true, 'dapo', 'main', 'pass', '080654543432', 1);

insert into UserRole(id, role) values (1, 'admin'), (2, 'user'), (3, 'super_admin');

insert into AccountDetails_UserRole(AccountDetails_username, authorities_id) values ('dman2022', 2);


--insert insert Organization(DTYPE, organizationId, address, businessName, contactNumber,contactPerson,dateCreated,state,businessType,LicenseExpiryDate,licenseIssueDate ,parent_organization_id)
--values()
