create table deals(
id serial primary key, 
ad_id integer not null, 
buyer_id integer not null,
status varchar(30) not null,
created_date_time timestamp not null
)