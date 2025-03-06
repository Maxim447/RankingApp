create sequence if not exists map_coordinates_sequence;

create table if not exists map_coordinates
(
    coordinate_id   bigint PRIMARY KEY default nextval('map_coordinates_sequence'::regclass),
    geometry        GEOMETRY(Geometry, 4326),
    description     text   not null,
    name            text   not null,
    organization_id bigint not null
        constraint fk_organization_reference references organizations
)