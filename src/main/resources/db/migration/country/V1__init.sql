create extension if not exists "uuid-ossp";

create table if not exists "countries"
(
    id          UUID unique  not null default uuid_generate_v1() primary key,
    name        varchar(255) not null,
    iso         char(2)      not null,
    coordinates text         not null
);

insert into "countries"(name, iso, coordinates)
values ('Fiji',
        'FJ',
        '[[[[180,-16.067],[180,-16.555],[179.364,-16.801],[178.725,-17.012],[178.597,-16.639],[179.097,-16.434],[179.414,-16.379],[180,-16.067]]],
     [[[178.126,-17.505],[178.374,-17.34],[178.718,-17.628],[178.553,-18.151],[177.933,-18.288],[177.381,-18.164],[177.285,-17.725],[177.671,-17.381],[178.126,-17.505]]],
     [[[-179.793,-16.021],[-179.917,-16.502],[-180,-16.555],[-180,-16.067],[-179.793,-16.021]]]]');