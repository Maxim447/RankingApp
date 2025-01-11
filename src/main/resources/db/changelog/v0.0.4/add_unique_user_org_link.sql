alter table if exists users_organizations_link
    add constraint unique_user_organization unique (user_id, organization_id)