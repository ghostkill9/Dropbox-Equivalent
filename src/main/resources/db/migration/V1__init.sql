CREATE TABLE IF NOT EXISTS  file_meta_data(
id UUID PRIMARY KEY ,
file_name VARCHAR,
file_link VARCHAR,
file_type VARCHAR,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL
)