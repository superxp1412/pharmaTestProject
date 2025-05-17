CREATE TABLE drugs
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(100) NOT NULL,
    batch_number VARCHAR(50)  NOT NULL,
    expiry_date  DATE         NOT NULL,
    stock        INTEGER      NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE
);

INSERT INTO drugs (name, manufacturer, batch_number, expiry_date, stock, created_at)
VALUES ('Aspirin', 'Bayer', 'ASP123', '2025-12-31', 100, NOW()),
       ('Ibuprofen', 'Pfizer', 'IBU456', '2024-11-30', 250, NOW()),
       ('Paracetamol', 'GSK', 'PAR789', '2026-01-15', 150, NOW());