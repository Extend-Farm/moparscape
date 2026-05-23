drop table if exists character_social_links;
drop table if exists character_item_slots;
drop table if exists character_skill_progression;
drop table if exists character_appearances;
drop table if exists character_profiles;
drop table if exists character_positions;
drop table if exists characters;
drop table if exists account_credentials;
drop table if exists accounts;

create sequence account_id_seq start with 1000;
create sequence character_id_seq start with 1000;

create table accounts (
  id bigint primary key default nextval('account_id_seq'),
  login_name varchar(32) not null,
  login_name_key varchar(32) not null unique,
  created_at timestamp with time zone not null default now(),
  updated_at timestamp with time zone not null default now(),
  check (btrim(login_name) <> ''),
  check (btrim(login_name_key) <> '')
);

create table account_credentials (
  account_id bigint primary key references accounts (id) on delete cascade,
  password_hash text not null,
  password_updated_at timestamp with time zone not null default now(),
  check (btrim(password_hash) <> '')
);

create table characters (
  id bigint primary key default nextval('character_id_seq'),
  account_id bigint not null unique references accounts (id) on delete cascade,
  display_name varchar(32) not null,
  display_name_key varchar(32) not null unique,
  created_at timestamp with time zone not null default now(),
  updated_at timestamp with time zone not null default now(),
  check (btrim(display_name) <> ''),
  check (btrim(display_name_key) <> '')
);

create table character_positions (
  character_id bigint primary key references characters (id) on delete cascade,
  world_x integer not null,
  world_y integer not null,
  plane smallint not null,
  updated_at timestamp with time zone not null default now(),
  check (plane >= 0 and plane <= 3)
);

create table character_profiles (
  character_id bigint primary key references characters (id) on delete cascade,
  rights smallint not null default 0,
  is_member boolean not null default false,
  run_energy smallint not null default 100,
  last_login_day integer,
  game_time_counter bigint not null default 0,
  game_count_counter bigint not null default 0,
  updated_at timestamp with time zone not null default now(),
  check (run_energy >= 0 and run_energy <= 100)
);

create table character_appearance_slots (
  character_id bigint not null references characters (id) on delete cascade,
  slot_index smallint not null,
  look_value integer not null,
  primary key (character_id, slot_index),
  check (slot_index >= 0)
);

create table character_skills (
  character_id bigint not null references characters (id) on delete cascade,
  skill_id smallint not null,
  current_level smallint not null,
  experience integer not null,
  primary key (character_id, skill_id),
  check (skill_id >= 0),
  check (current_level >= 0),
  check (experience >= 0)
);

create table character_item_slots (
  character_id bigint not null references characters (id) on delete cascade,
  container_kind smallint not null,
  slot_index smallint not null,
  item_id integer not null,
  quantity integer not null,
  primary key (character_id, container_kind, slot_index),
  check (container_kind >= 0 and container_kind <= 2),
  check (slot_index >= 0),
  check (item_id >= 0),
  check (quantity >= 0)
);

create index character_item_slots_character_container_idx
  on character_item_slots (character_id, container_kind);

create table character_social_links (
  character_id bigint not null references characters (id) on delete cascade,
  link_kind smallint not null,
  target_value bigint not null,
  primary key (character_id, link_kind, target_value),
  check (link_kind >= 0 and link_kind <= 1),
  check (target_value >= 0)
);

create index character_social_links_character_kind_idx
  on character_social_links (character_id, link_kind);

alter sequence account_id_seq owned by accounts.id;
alter sequence character_id_seq owned by characters.id;
