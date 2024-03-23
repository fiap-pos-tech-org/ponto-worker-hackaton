CREATE TABLE IF NOT EXISTS user (
    id bigint NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100),
    creation_date timestamp NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS clock_registry (
    id bigint NOT NULL AUTO_INCREMENT,
    time_clock_id bigint NOT NULL,
    time timestamp NOT NULL,
    user_id bigint NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS report_log (
    id bigint NOT NULL AUTO_INCREMENT,
    created_at timestamp NOT NULL,
    user_id bigint NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
)