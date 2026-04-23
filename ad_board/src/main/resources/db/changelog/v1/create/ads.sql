create table ads(
id serial primary key,
title varchar(100) not null,
price integer not null,
description text,
seller_id integer not null,
city_id integer not null,
created_date_time timestamp not null,
status varchar(20) not null,
is_paid boolean default false
)