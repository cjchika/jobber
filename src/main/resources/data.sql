-- Ensure the 'companies' table exists
CREATE TABLE IF NOT EXISTS companies
(
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    website VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert a dummy company into the 'companies' table if it does not exist
INSERT INTO companies (id, name, description, website, created_at)
SELECT '123e4567-e89b-12d3-a456-426614174999',
       'Dummy Company',
       'A dummy company for testing purposes.',
       'http://dummycompany.com',
       '2024-01-10 12:00:00'
WHERE NOT EXISTS (SELECT 1 FROM companies WHERE id = '123e4567-e89b-12d3-a456-426614174999');


-- Ensure the 'users' table exists
CREATE TABLE IF NOT EXISTS users
(
    id UUID PRIMARY KEY,
    fullName VARCHAR(100)        NOT NULL,
    email VARCHAR(100) UNIQUE     NOT NULL,
    password TEXT            NOT NULL,
    role VARCHAR(20) CHECK (role IN ('JOB_SEEKER', 'EMPLOYER', 'ADMIN')) NOT NULL,
    companyId UUID               NULL,
    created_at TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (companyId) REFERENCES companies(id) ON DELETE SET NULL
);

-- Insert two well-known UUIDs for specific users if they do not already exist
INSERT INTO users (id, fullName, email, password, role, companyId, created_at, updated_at)
SELECT '123e4567-e89b-12d3-a456-426614174000',
       'John Doe',
       'john.doe@example.com',
       'hashed_password_1',  -- Replace with an actual hashed password
       'JOB_SEEKER',
       NULL,
       '2024-01-10 12:00:00', -- Example created_at timestamp
       '2024-01-10 12:00:00'  -- Example updated_at timestamp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = '123e4567-e89b-12d3-a456-426614174000');

INSERT INTO users (id, full_name, email, password, role, companyId, created_at, updated_at)
SELECT '123e4567-e89b-12d3-a456-426614174001',
       'Jane Smith',
       'jane.smith@example.com',
       'hashed_password_2',  -- Replace with an actual hashed password
       'EMPLOYER',
       '123e4567-e89b-12d3-a456-426614174999',
       '2024-01-12 15:30:00', -- Example created_at timestamp
       '2024-01-12 15:30:00'  -- Example updated_at timestamp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = '123e4567-e89b-12d3-a456-426614174001');
