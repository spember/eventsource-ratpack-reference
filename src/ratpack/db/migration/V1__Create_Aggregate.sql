set timezone='UTC';

-- while we inherit on each of the tables, we also specify the id as primary key again in order to keep the unique constraint and index

create table aggregate (
  id uuid primary key,
  revision int not null,
  active boolean not null default TRUE
);


create table person (
  id uuid primary key,
  email varchar(100) not null,
  name varchar(250) not null
) INHERITS (aggregate);
create index person_idx_active on person (active);
create index person_idx_email on person (email);


create table product (
  id uuid primary key,
  sku varchar(50) not null,
  quantity INTEGER not null
) INHERITS (aggregate);
create index product_idx_active on product(active);
create index product_idx_sku on product(sku);


create table purchase_order (
  id uuid primary key,
  person_id uuid REFERENCES person(id),
  date timestamp with time zone not null
) INHERITS (aggregate);
create index order_idx_active on purchase_order(active);
create index order_idx_person on purchase_order(person_id);
create index order_idx_date on purchase_order(date);


create table cart (
  id uuid primary key,
  person_id uuid references person(id)
) INHERITS (aggregate);
create index cart_idx_active on cart(active);
create index cart_idx_person on cart(person_id);






