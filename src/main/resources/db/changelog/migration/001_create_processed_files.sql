CREATE TABLE processed_files (
    id UUID PRIMARY KEY,
    file_name VARCHAR(500) NOT NULL,
    checksum VARCHAR(64) NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX idx_processed_files_checksum ON processed_files(checksum);
