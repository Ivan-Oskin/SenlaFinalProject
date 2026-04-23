create or replace function update_rating()
returns trigger as $$
declare
seller_id_int integer;
rating_value integer;
Begin
select seller_id into seller_id_int from ads where id = new.ad_id;
rating_value := new.rating;
update profiles set 
rating_count = rating_count + 1,
rating = (rating * rating_count + rating_value)::decimal / (rating_count + 1)::decimal 
where user_id = seller_id_int;
Return new;
end;
$$ language plpgsql;


Create trigger after_review_insert
after insert on reviews 
for each row
execute function update_rating()