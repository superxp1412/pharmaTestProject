CREATE TABLE audit_logs
(
    id              BIGSERIAL PRIMARY KEY,
    prescription_id BIGINT      NOT NULL,
    patient_id      BIGINT      NOT NULL,
    pharmacy_id     BIGINT      NOT NULL,
    drugs_requested JSONB       NOT NULL,
    drugs_dispensed JSONB,
    failure_reason  TEXT,
    status          VARCHAR(20) NOT NULL CHECK (status IN ('SUCCESS', 'FAILURE')),
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
