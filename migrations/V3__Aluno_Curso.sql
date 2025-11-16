CREATE TABLE IF NOT EXISTS aluno_curso (
    aluno_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,

    CONSTRAINT fk_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id) ON DELETE CASCADE,
    CONSTRAINT fk_curso FOREIGN KEY (curso_id) REFERENCES curso(id) ON DELETE CASCADE,

    PRIMARY KEY (aluno_id, curso_id)
);
