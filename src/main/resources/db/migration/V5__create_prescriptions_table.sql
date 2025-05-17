CREATE TABLE prescriptions
(
    id          SERIAL PRIMARY KEY,
    pharmacy_id BIGINT      NOT NULL,
    patient_id  BIGINT      NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status      VARCHAR(20) NOT NULL
);

-- INSERT INTO prescriptions (patient_id, pharmacy_id, created_at, status)
-- VALUES (1, 1, '2025-05-10', 'CREATED'),
--        (2, 2, '2025-05-12', 'CREATED'),
--        (3, 3, '2025-05-15', 'CREATED');