insert into messages (dialog_id, user_id, message, send_date_time)
values
(1, 2, 'Здравствуйте, а есть игры в комплекте?', NOW() - INTERVAL '5 days'),
(1, 3, 'Привет! Да 100 игр есть на пс', NOW() - INTERVAL '4 days'),
(1, 2, 'О я смогу приехать в Москву в выходные, смогу посмотреть?', NOW() - INTERVAL '3 days'),
(1, 3, 'Да, давайте!', NOW() - INTERVAL '3 days')
