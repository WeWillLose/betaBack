insert into usr(id,username, password, is_active) values(
    1,
    'admin',
    '$2a$10$9x1fBdq.fXcCcZuF0SHKQewKbrK2RrCFOLzQctuBRdaJceIpOcmZ.',
    true
);
insert into user_role values(1,'CHAIRMAN');
insert into user_role values(1,'ADMIN');
insert into user_role values(1,'TEACHER');