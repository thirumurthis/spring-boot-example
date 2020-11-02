CREATE SEQUENCE  IF NOT EXISTS student_id;
create table student (id INT not null default student_id.nextval primary key , user_name varchar(300) not null, subject varchar(150) );
  