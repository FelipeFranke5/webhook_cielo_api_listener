ALTER TABLE authentication DROP COLUMN password;
ALTER TABLE authentication ADD password VARCHAR(255) NOT NULL;