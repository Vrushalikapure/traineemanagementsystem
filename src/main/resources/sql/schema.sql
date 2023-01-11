drop table if exists test CASCADE;
drop table if exists user_details CASCADE;
drop table if exists user_login_history CASCADE;
drop table if exists work_history CASCADE;
create table test (id bigint not null, label varchar(255), primary key (id));
create table user_details (user_name varchar(255) not null, created_timestamp timestamp, email varchar(255) not null, first_name varchar(255) not null, last_name varchar(255) not null, last_updated_timestamp timestamp, location varchar(255) not null, mobile_no varchar(255) not null, password varchar(255) not null, primary key (user_name));
create table user_login_history (user_name varchar(255) not null, failure_attempts integer, is_locked varchar(2), lock_time timestamp, login_status varchar(2), login_time timestamp, primary key (user_name));
create table work_history (user_name varchar(255) not null, comments varchar(255), created_timestamp timestamp, last_updated_timestamp timestamp, working_area varchar(255), primary key (user_name));
