CREATE TABLE patients
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    date_of_birth DATE         NOT NULL,
    phone_number  VARCHAR(20),
    email         VARCHAR(100)
);

INSERT INTO patients (name, date_of_birth, phone_number, email)
VALUES ('John Doe', '1980-01-15', '+15551234567', 'john.doe@example.com'),
       ('Jane Smith', '1992-05-22', '+15559876543', 'jane.smith@example.com'),
       ('Robert Johnson', '1975-10-10', '+15554567890', 'robert.j@example.com');