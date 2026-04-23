create table reviews (
id serial primary key,
author_id integer not null,
ads_id integer not null, 
rating integer not null,
comment text,
created_date_time timestamp not null
)