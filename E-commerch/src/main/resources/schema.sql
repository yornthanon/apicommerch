-- Fix legacy NOT NULL constraint on orders.payment_id
-- This project creates Order before Payment, so payment_id must be nullable.

ALTER TABLE orders
  ALTER COLUMN payment_id DROP NOT NULL;

