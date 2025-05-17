CREATE TABLE pharmacy_drugs
(
    pharmacy_id      BIGINT  NOT NULL,
    drug_id          BIGINT  NOT NULL,
    allocated_amount INTEGER NOT NULL,
    PRIMARY KEY (pharmacy_id, drug_id)
);

INSERT INTO pharmacy_drugs (pharmacy_id, drug_id, allocated_amount)
VALUES (1, 1, 50),
       (2, 1, 75),
       (2, 3, 50);