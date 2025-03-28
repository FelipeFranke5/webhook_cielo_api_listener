CREATE TABLE IF NOT EXISTS authentication (
    id SMALLINT PRIMARY KEY NOT NULL,
    name VARCHAR(40) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS acquirer_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id VARCHAR(100) DEFAULT '1234',
    acquirer_transaction_id VARCHAR(20) NOT NULL,
    payment_type VARCHAR(12) NOT NULL,
    payment_id VARCHAR(36) NOT NULL,
    payment_status SMALLINT NOT NULL
);