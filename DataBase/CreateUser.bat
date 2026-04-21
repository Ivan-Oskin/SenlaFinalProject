@echo off
echo Creating new_user...
psql -U postgres -c "CREATE USER ad_board_admin WITH PASSWORD 'Admin';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE ad_board_db TO ad_board_admin;"
psql -U postgres -d ad_board_db -c "GRANT CREATE ON SCHEMA public TO ad_board_admin;"
psql -U postgres -d ad_board_db -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ad_board_admin;"
psql -U postgres -d ad_board_db -c "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO ad_board_admin;"
psql -U postgres -d ad_board_db -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO ad_board_admin;"
psql -U postgres -d ad_board_db -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO ad_board_admin;"

pause
