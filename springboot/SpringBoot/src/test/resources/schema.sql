-- Table for trader
CREATE TABLE IF NOT EXISTS trader (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  dob DATE NOT NULL,
  country VARCHAR NOT NULL,
  email VARCHAR NOT NULL UNIQUE
);

-- Table for account
CREATE TABLE IF NOT EXISTS account (
  id SERIAL PRIMARY KEY,
  trader_id INT NOT NULL,
  amount DOUBLE PRECISION NOT NULL,
  CONSTRAINT account_trader_fk FOREIGN KEY (trader_id) REFERENCES trader (id) ON DELETE CASCADE
);

-- Table for security orders
CREATE TABLE IF NOT EXISTS security_order (
  id SERIAL PRIMARY KEY,
  account_id INT NOT NULL,
  status VARCHAR NOT NULL,
  ticker VARCHAR NOT NULL,
  size INT NOT NULL,
  price DOUBLE PRECISION,
  notes VARCHAR,
  CONSTRAINT security_order_account_fk FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
);

-- Table for quotes
CREATE TABLE IF NOT EXISTS quote (
  ticker VARCHAR PRIMARY KEY,
  last_price DOUBLE PRECISION NOT NULL,
  bid_price DOUBLE PRECISION NOT NULL,
  ask_price DOUBLE PRECISION NOT NULL,
  bid_size INT NOT NULL,
  ask_size INT NOT NULL
);

-- View for positions
CREATE OR REPLACE VIEW position AS
SELECT
  account_id,
  ticker,
  SUM(size) AS position
FROM
  security_order
WHERE
  status = 'FILLED'
GROUP BY
  account_id, ticker;
