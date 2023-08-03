CREATE TABLE users (
    id BINARY(16) PRIMARY KEY NOT NULL,
    email VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255)
);