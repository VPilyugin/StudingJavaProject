DROP TABLE IF EXISTS jc_student_child;
DROP TABLE IF EXISTS jc_student_order;
DROP TABLE IF EXISTS jc_register_office;
DROP TABLE IF EXISTS jc_passport_office;
DROP TABLE IF EXISTS jc_country_struct;
DROP TABLE IF EXISTS jc_university;
DROP TABLE IF EXISTS jc_streets;

CREATE TABLE jc_streets(
    street_code integer not null,
    street_name varchar(300),
    PRIMARY KEY (street_code)
);
CREATE TABLE jc_university(
    university_id integer not null,
    university_name varchar(300),
    PRIMARY KEY (university_id)
);

CREATE TABLE jc_country_struct(
    area_id char(12) not null,
    area_name varchar(200),
    PRIMARY KEY (area_id)
);

CREATE TABLE jc_passport_office(
    p_office_id integer not null,
    p_office_area_id char(12) not null,
    p_office_name varchar(200),
    PRIMARY KEY (p_office_id),
    FOREIGN KEY (p_office_area_id) REFERENCES jc_country_struct(area_id) ON DELETE RESTRICT
);
CREATE TABLE jc_register_office(
    r_office_id integer not null,
    r_office_area_id char(12) not null,
    r_office_name varchar(200),
    PRIMARY KEY (r_office_id),
    FOREIGN KEY (r_office_area_id) REFERENCES jc_country_struct(area_id) ON DELETE RESTRICT
);
CREATE TABLE jc_student_order(
    student_order_id SERIAL,
    student_order_status integer not null,
    student_order_date timestamp not null,
    h_sur_name varchar(100) not null,
    h_given_name varchar(100) not null,
    h_patronymic varchar(100) not null,
    h_date_of_birth date not null,
    h_passport_seria varchar(10) not null,
    h_passport_number varchar(10) not null,
    h_passport_date date not null,
    h_passport_office_id integer not null,
    h_post_index varchar(12),
    h_street_code integer not null,
    h_building varchar(10) not null,
    h_extension varchar(10),
    h_apartment varchar(10),
    h_university_id integer not null,
    h_student_id varchar(30),
    w_sur_name varchar(100) not null,
    w_given_name varchar(100) not null,
    w_patronymic varchar(100) not null,
    w_date_of_birth date not null,
    w_passport_seria varchar(10) not null,
    w_passport_number varchar(10) not null,
    w_passport_date date not null,
    w_passport_office_id integer not null,
    w_post_index varchar(12),
    w_street_code integer not null,
    w_building varchar(10) not null,
    w_extension varchar(10),
    w_apartment varchar(10),
    w_university_id integer not null,
    w_student_id varchar(30),
    certificate_id varchar(20) not null,
    register_office_id integer not null,
    marriage_date date not null,
    PRIMARY KEY(student_order_id),
    FOREIGN KEY(h_street_code) REFERENCES jc_streets(street_code) ON DELETE RESTRICT,
    FOREIGN KEY(h_passport_office_id) REFERENCES jc_passport_office(p_office_id) ON DELETE RESTRICT,
    FOREIGN KEY(h_university_id) REFERENCES jc_university(university_id) ON DELETE RESTRICT,
    FOREIGN KEY(w_passport_office_id) REFERENCES jc_passport_office(p_office_id) ON DELETE RESTRICT,
    FOREIGN KEY(w_university_id) REFERENCES jc_university(university_id) ON DELETE RESTRICT,
    FOREIGN KEY(w_street_code) REFERENCES jc_streets(street_code) ON DELETE RESTRICT,
    FOREIGN KEY(register_office_id) REFERENCES jc_register_office(r_office_id)
);
CREATE TABLE jc_student_child(
    student_child_id SERIAL,
    student_order_id integer not null,
    c_sur_name varchar(100) not null,
    c_given_name varchar(100) not null,
    c_patronymic varchar(100) not null,
    c_date_of_birth date not null,
    certificate_number varchar(10) not null,
    certificate_date date not null,
    c_register_office_id integer not null,
    c_post_index varchar(12),
    c_street_code integer not null,
    c_building varchar(10) not null,
    c_extension varchar(10),
    c_apartment varchar(10),
    PRIMARY KEY (student_child_id),
    FOREIGN KEY (student_order_id) REFERENCES jc_student_order(student_order_id) ON DELETE RESTRICT,
    FOREIGN KEY (c_register_office_id) REFERENCES jc_register_office(r_office_id) ON DELETE RESTRICT,
    FOREIGN KEY (c_street_code) REFERENCES jc_streets(street_code) ON DELETE RESTRICT
);
INSERT INTO jc_streets (street_code, street_name) VALUES
(1,'Невский проспект'),
(2,'Гороховая улица'),
(3,'Литейный проспект'),
(4,'Безымянный переулок'),
(5,'Третьяковский проезд');
INSERT INTO jc_university (university_id,university_name) VALUES
(1,'MMSU'),
(2,'MGSU'),
(3,'SPTU');
INSERT INTO jc_country_struct (area_id,area_name) VALUES
('010000000000','Город'),
('010010000000','Город Район 1'),
('010020000000','Город Район 2'),
('010030000000','Город Район 3'),
('010040000000','Город Район 4'),
('020000000000','Край'),
('020010000000','Край Область 1'),
('020010010000','Край Область 1 Район 1'),
('020010010001','Край Область 1 Район 1 Поселение 1'),
('020010010002','Край Область 1 Район 1 Поселение 2'),
('020010020000','Край Область 1 Район 2'),
('020010020001','Край Область 1 Район 2 Поселение 1'),
('020010020002','Край Область 1 Район 2 Поселение 2'),
('020010020003','Край Область 1 Район 2 Поселение 3'),
('020020000000','Край Область 2'),
('020020010000','Край Область 2 Район 1'),
('020020010001','Край Область 2 Район 1 Поселение 1'),
('020020010002','Край Область 2 Район 1 Поселение 2'),
('020020020000','Край Область 2 Район 2'),
('020020020001','Край Область 2 Район 2 Поселение 1'),
('020020020002','Край Область 2 Район 2 Поселение 2'),
('020020020003','Край Область 2 Район 2 Поселение 3');
INSERT INTO jc_register_office(r_office_id,r_office_area_id,r_office_name) VALUES
(1,'020010010001','ЗАГС Край Область 1 Район 1 Поселение 1'),
(2,'020010010002','ЗАГС Край Область 1 Район 1 Поселение 2'),
(3,'020010020001','ЗАГС Край Область 1 Район 2 Поселение 1'),
(4,'020010020002','ЗАГС Край Область 1 Район 2 Поселение 2'),
(5,'020010020003','ЗАГС Край Область 1 Район 2 Поселение 3'),
(6,'020020010001','ЗАГС Край Область 2 Район 1 Поселение 1'),
(7,'020020010002','ЗАГС Край Область 2 Район 1 Поселение 2');
INSERT INTO jc_passport_office (p_office_id, p_office_area_id,p_office_name) VALUES
(1,'020010010001','ПАСПОРТНЫЙ СТОЛ Край Область 1 Район 1 Поселение 1'),
(2,'020010010002','ПАСПОРТНЫЙ СТОЛ Край Область 1 Район 1 Поселение 2'),
(3,'020010020001','ПАСПОРТНЫЙ СТОЛ Край Область 1 Район 2 Поселение 1'),
(4,'020010020002','ПАСПОРТНЫЙ СТОЛ Край Область 1 Район 2 Поселение 2'),
(5,'020010020003','ПАСПОРТНЫЙ СТОЛ Край Область 1 Район 2 Поселение 3'),
(6,'020020010001','ПАСПОРТНЫЙ СТОЛ Край Область 2 Район 1 Поселение 1'),
(7,'020020010002','ПАСПОРТНЫЙ СТОЛ Край Область 2 Район 1 Поселение 2');