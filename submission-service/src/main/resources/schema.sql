CREATE TABLE IF NOT EXISTS code_submission_request (
    id INT PRIMARY KEY,
    code VARCHAR(255),
    language VARCHAR(50),
    user_id INT
);

CREATE TABLE IF NOT EXISTS file_submission (
    id SERIAL PRIMARY KEY,
    code_content BYTEA NOT NULL,
    language VARCHAR(255) NOT NULL,
    user_id INT NOT NULL
);
