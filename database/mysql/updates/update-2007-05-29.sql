# Add delete "super code" function for billing.
alter table billing_on_favourite add column deleted boolean NOT NULL default false;