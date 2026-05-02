create or replace function delete_rating()
returns trigger as $$
declare
seller_id_int integer;
rating_value integer;
Begin
select seller_id into seller_id_int from ads where id = old.ad_id;
rating_value := old.rating;
update profiles set 
rating_count = rating_count - 1,
rating = CASE
WHEN rating_count -1 = 0 THEN 0
ELSE (rating * rating_count - rating_value)::decimal / (rating_count - 1)::decimal
END
where user_id = seller_id_int;
Return old;
end;
$$ language plpgsql;


Create trigger after_review_delete
after delete on reviews 
for each row
execute function delete_rating()