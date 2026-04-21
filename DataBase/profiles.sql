create table profiles(
id serial primary key, 
user_id integer not null unique,
name varchar(20) not null, 
surname varchar(30) not null,
age integer not null,
city_id integer not null,
rating decimal(2,1) default 0,
rating_count integer default 0,
created_date_time timestamp
)