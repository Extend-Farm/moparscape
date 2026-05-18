alter table accounts
  add column if not exists username_normalized text generated always as (lower(username)) stored;

create unique index if not exists accounts_username_normalized_uq on accounts (username_normalized);

create table if not exists character_positions (
  character_id bigint primary key references characters (id) on delete cascade,
  world_x integer not null,
  world_y integer not null,
  plane smallint not null,
  updated_at timestamp with time zone not null default now(),
  check (plane >= 0 and plane <= 3)
);

insert into character_positions (character_id, world_x, world_y, plane)
select id, world_x, world_y, plane
from characters
on conflict (character_id) do nothing;

alter table characters drop column if exists world_x;
alter table characters drop column if exists world_y;
alter table characters drop column if exists plane;

create table if not exists character_profiles (
  character_id bigint primary key references characters (id) on delete cascade,
  rights smallint not null default 0,
  is_member boolean not null default false,
  run_energy smallint not null default 100,
  last_login_day integer,
  game_time_counter bigint not null default 0,
  game_count_counter bigint not null default 0
);

create table if not exists character_appearances (
  character_id bigint primary key references characters (id) on delete cascade,
  look_values integer[] not null
);

create table if not exists character_skill_progression (
  character_id bigint primary key references characters (id) on delete cascade,
  current_levels smallint[] not null,
  experience_values integer[] not null
);

create table if not exists character_item_slots (
  character_id bigint not null references characters (id) on delete cascade,
  container_kind smallint not null,
  slot_index smallint not null,
  item_id integer not null,
  quantity integer not null,
  primary key (character_id, container_kind, slot_index),
  check (container_kind >= 0 and container_kind <= 2),
  check (quantity >= 0)
);

create index if not exists character_item_slots_character_container_idx
  on character_item_slots (character_id, container_kind);

create table if not exists character_social_links (
  character_id bigint not null references characters (id) on delete cascade,
  link_kind smallint not null,
  target_value bigint not null,
  primary key (character_id, link_kind, target_value),
  check (link_kind >= 0 and link_kind <= 1)
);
