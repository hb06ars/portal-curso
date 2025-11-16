CREATE TABLE IF NOT EXISTS aluno (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);


DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE tablename = 'aluno' AND indexname = 'idx_username') THEN
        CREATE UNIQUE INDEX idx_username ON aluno (username);
    END IF;
END $$;

