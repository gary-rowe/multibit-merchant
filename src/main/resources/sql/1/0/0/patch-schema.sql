DROP ALL OBJECTS;

CREATE TABLE customers (
    id long NOT NULL IDENTITY,
    openid varchar(255) NOT NULL,
    email varchar(255),
    CONSTRAINT pk_customer PRIMARY KEY (id)
);
