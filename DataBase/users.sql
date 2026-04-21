Create table users(id serial primary key,
mail varchar(70) unique, 
password varchar(255) not null, 
role_id integer not null,
)