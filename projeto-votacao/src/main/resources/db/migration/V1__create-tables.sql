CREATE TABLE tb_associados (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cpf VARCHAR(11) NOT NULL UNIQUE
);

CREATE TABLE tb_pautas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255),
    status ENUM('NOVA','EM_VOTACAO', 'FINALIZADA') DEFAULT 'NOVA',
    criado_em DATE NOT NULL
);

CREATE TABLE tb_sessoes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pauta_id BIGINT NOT NULL UNIQUE,
    inicio DATETIME NOT NULL,
    fim DATETIME NOT NULL,
    status ENUM( 'EM_ANDAMENTO', 'FINALIZADA') DEFAULT 'EM_ANDAMENTO',

    CONSTRAINT fk_sessoes__tb_pautas FOREIGN KEY (pauta_id) REFERENCES tb_pautas(id)
);

CREATE TABLE tb_votos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    associado_id BIGINT NOT NULL,
    pauta_id BIGINT NOT NULL,
    tipo_voto ENUM('SIM', 'NAO') NOT NULL,

    CONSTRAINT fk_votos__tb_associados FOREIGN KEY (associado_id) REFERENCES tb_associados(id),
    CONSTRAINT fk_votos__tb_pautas FOREIGN KEY (pauta_id) REFERENCES tb_pautas(id),
    CONSTRAINT uq_associado_pauta UNIQUE (associado_id, pauta_id)
);

CREATE INDEX idx_votos_pauta ON tb_votos (pauta_id);
CREATE INDEX idx_associado_id ON tb_associados (id);
