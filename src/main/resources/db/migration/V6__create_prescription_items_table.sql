CREATE TABLE prescription_items
(
    id              SERIAL PRIMARY KEY,
    prescription_id BIGINT      NOT NULL,
    drug_id         BIGINT      NOT NULL,
    dosage          VARCHAR(20) NOT NULL,
    quantity        INTEGER     NOT NULL
);

-- INSERT INTO prescription_items (prescription_id, drug_id, dosage, quantity)
-- VALUES (1, 1, '500mg', 30),  -- Prescription 1: 30 Aspirin
--        (2, 1, '500mg', 45),  -- Prescription 2: 45 Aspirin
--        (2, 3, '1000mg', 15), -- Prescription 2: 15 Paracetamol
--        (3, 2, '400mg', 10),  -- Prescription 3: 10 Ibuprofen (expired)
--        (3, 3, '500mg', 30); -- Prescription 3: 30 Paracetamol