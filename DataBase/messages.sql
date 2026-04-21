create table messages(id serial primary key, 
dialog_id integer not null, 
user_id integer not null,
message text not null,
send_date_time timestamp not null
) 