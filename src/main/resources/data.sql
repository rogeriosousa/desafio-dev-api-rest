insert into person(id, name, document_number, birth_date) values(1, 'John Doe', '30888839322', parseDateTime('19900101','yyyyMMdd'));
insert into account(id, person_id, balance, limit_per_day, is_active, account_type, created_at) values(1, 1, 1000, 100, 'true', 1, parseDateTime('20210101','yyyyMMdd'));
insert into transaction(id, account_id, value, date) values(1, 1, 100, parseDateTime('20210101','yyyyMMdd'));

insert into person(id, name, document_number, birth_date) values(2, 'Jane Jim', '34949870833', parseDateTime('19880101','yyyyMMdd'));
insert into account(id, person_id, balance, limit_per_day, is_active, account_type, created_at) values(2, 2, 1000, 100, 'true', 1, parseDateTime('20210101','yyyyMMdd'));
insert into transaction(id, account_id, value, date) values(2, 1, 100, parseDateTime('20210101','yyyyMMdd'));

