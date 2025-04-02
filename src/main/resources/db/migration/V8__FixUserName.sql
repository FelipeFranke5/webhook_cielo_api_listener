ALTER TABLE authentication DROP COLUMN name;
ALTER TABLE authentication ADD name VARCHAR(30) NOT NULL;