create table if not exists accounts (
  id bigint primary key,
  username text not null unique,
  password_hash text not null
);

create table if not exists characters (
  id bigint primary key,
  account_id bigint not null references accounts (id),
  display_name text not null,
  world_x integer not null,
  world_y integer not null,
  plane integer not null
);
