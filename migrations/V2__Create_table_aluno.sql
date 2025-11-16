CREATE TABLE IF NOT EXISTS aluno (
    id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
    username varchar(255) NOT NULL UNIQUE,
    nome varchar(255) NOT NULL,
    "password" varchar(255) NOT NULL,
    "role" varchar(255) NOT NULL,
    curso_id BIGINT,
    CONSTRAINT aluno_pkey PRIMARY KEY (id),
    CONSTRAINT fk_aluno_curso FOREIGN KEY (curso_id) REFERENCES curso(id)
);


DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE tablename = 'aluno' AND indexname = 'idx_username') THEN
        CREATE UNIQUE INDEX idx_username ON aluno (username);
    END IF;
END $$;

