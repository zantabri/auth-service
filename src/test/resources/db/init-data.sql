insert into AccountDetails(username, email, activated, first_name , last_name, password , telephone, organization_id)
values ('dman2022', 'dman2022@outlook.com', true, 'dapo', 'main', 'password111111', '080654543432', 1);

insert into UserRole(id, role) values (1, 'admin'), (2, 'user'), (3, 'super_admin');

insert into AccountDetails_UserRole(AccountDetails_username, authorities_id) values ('dman2022', 2);

insert into ActivationCode (code, expires , username) values ('235621', DATEADD('DAY', -1, CURRENT_TIMESTAMP), 'dman2022'),
('154745', DATEADD('DAY', 1, CURRENT_TIMESTAMP), 'johnD');