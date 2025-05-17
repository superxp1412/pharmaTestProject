CREATE TABLE pharmacies
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL
);

INSERT INTO pharmacies (name, address)
VALUES ('Central Pharmacy', '123 Main St'),
       ('Downtown Drugs', '456 Oak Ave');