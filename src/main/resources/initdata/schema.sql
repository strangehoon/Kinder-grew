create table attendance (
                            id bigint not null auto_increment,
                            absent_reason varchar(255),
                            absented bit,
                            date date,
                            entered bit,
                            exited bit,
                            child_id bigint,
                            primary key (id)
);

create table child (
                       id bigint not null auto_increment,
                       age integer,
                       birth date,
                       daily_enter_time varchar(255),
                       daily_exit_time varchar(255),
                       gender varchar(255),
                       name varchar(255),
                       profile_image_url varchar(255),
                       significant varchar(255),
                       classroom_id bigint not null,
                       primary key (id)
);

create table classroom (
                           id bigint not null auto_increment,
                           name varchar(255),
                           primary key (id)
);

create table image (
                       id bigint not null auto_increment,
                       image_url varchar(255) not null,
                       image_post_id bigint,
                       primary key (id)
)

create table image_post (
                            id bigint not null auto_increment,
                            created_at date,
                            title varchar(255),
                            classroom_id bigint,
                            primary key (id)
)

create table teacher (
                         id bigint not null auto_increment,
                         birth date,
                         email varchar(255),
                         gender integer,
                         name varchar(255),
                         phone_number varchar(255),
                         profile_imageurl varchar(255),
                         resolution varchar(255),
                         classroom_id bigint,
                         primary key (id)
)