/*
-- Tabela Cliente
CREATE TABLE cliente (
        id_cliente BIGSERIAL PRIMARY KEY,
        nome VARCHAR(60) NOT NULL,
        data_nascimento DATE NOT NULL,
        email VARCHAR(320) NOT NULL UNIQUE,
        senha VARCHAR(64) NOT NULL,
        cpf VARCHAR(14) NOT NULL,
        profissao VARCHAR(30),
        cep CHAR(9),
        rua VARCHAR(255),
        numero_rua VARCHAR(10),
        uf CHAR(2),
        telefone VARCHAR(15) NOT NULL
    );

CREATE TYPE modalidade_cartao_enum AS ENUM ('credito', 'debito');

-- Tabela Cartão
CREATE TABLE cartao (
    id_cartao BIGSERIAL PRIMARY KEY,
    id_cliente BIGINT NOT NULL REFERENCES cliente(id_cliente),
    numero_cartao CHAR(16) NOT NULL,
    nome_titular VARCHAR(60) NOT NULL
);

CREATE TYPE cargo_enum AS ENUM ('gerente', 'gerente de negocios virtuais', 'padrao');

-- Tabela Funcionario
CREATE TABLE funcionario (
        id_funcionario BIGSERIAL PRIMARY KEY,
        nome VARCHAR(60) NOT NULL,
        codigo_funcionario CHAR(10) NOT NULL,
        email VARCHAR(320) NOT NULL UNIQUE,
        senha VARCHAR(64) NOT NULL,
        cep CHAR(9),
        rua VARCHAR(255),
        numero_rua VARCHAR(10),
        cargo cargo_enum NOT NULL
    );


-- Tabela Ponto de Venda
CREATE TABLE ponto_de_venda (
        id_ponto_de_venda BIGSERIAL PRIMARY KEY,
        nome VARCHAR(60) NOT NULL,
        cnpj CHAR(14) NOT NULL,
        cep CHAR(9),
        rua VARCHAR(255),
        numero_rua VARCHAR(10),
        telefone VARCHAR(15),
        id_gerente BIGINT REFERENCES funcionario (id_funcionario)
    );

CREATE TYPE dia_semana_enum AS ENUM ('segunda', 'terca', 'quarta', 'quinta', 'sexta', 'sabado', 'domingo');

-- Tabela Associativa 'ponto_funcionario'
CREATE TABLE ponto_funcionario (
    id_funcionario BIGINT NOT NULL REFERENCES funcionario (id_funcionario),
    id_ponto_de_venda BIGINT NOT NULL REFERENCES ponto_de_venda (id_ponto_de_venda),
    dia_semana dia_semana_enum NOT NULL,
    horario_inicial TIME NOT NULL,
    horario_final TIME NOT NULL,
    PRIMARY KEY (id_funcionario, id_ponto_de_venda, dia_semana)
);

-- Tabela: viagem
CREATE TABLE viagem (
        id_viagem SERIAL PRIMARY KEY,
        num_reservas_associadas INT
    );


-- Tabela: cidade
CREATE TABLE cidade (
        id_cidade SERIAL PRIMARY KEY,
        nome VARCHAR(50) NOT NULL,
        codigo CHAR(3) NOT NULL UNIQUE
    );

-- Tabela: estado
CREATE TABLE estado (
        id_estado SERIAL PRIMARY KEY,
        nome VARCHAR(50) NOT NULL,
        codigo CHAR(3) NOT NULL UNIQUE
    );



-- Tabela: aeroporto
CREATE TABLE aeroporto (
        id_aeroporto SERIAL PRIMARY KEY,
        nome VARCHAR(50) NOT NULL,
        codigo INT NOT NULL UNIQUE
    );


-- Tabela: estacao
CREATE TABLE estacao (
        id_estacao SERIAL PRIMARY KEY,
        nome VARCHAR(60) NOT NULL
    );


-- Tabela: porto
CREATE TABLE porto (
        id_porto SERIAL PRIMARY KEY,
        nome VARCHAR(60) NOT NULL
    );


-- Tabela: local (ligação com cidade e pontos específicos)
CREATE TABLE local (
        id_local SERIAL PRIMARY KEY,
        id_cidade INT NOT NULL REFERENCES cidade (id_cidade),
        id_aeroporto INT REFERENCES aeroporto (id_aeroporto),
        id_estacao INT REFERENCES estacao (id_estacao),
        id_porto INT REFERENCES porto (id_porto)
    );


CREATE TYPE tipo_local_enum AS ENUM ('origem', 'destino', 'escala');

-- Tabela: viagem_local (relação entre viagem e local)
CREATE TABLE viagem_local (
    id_viagem INT NOT NULL REFERENCES viagem(id_viagem),
    id_local INT NOT NULL REFERENCES local(id_local),
    tipo tipo_local_enum NOT NULL,
    PRIMARY KEY (id_viagem, id_local)
);


-- Tabela Modal
CREATE TABLE modal (
    id_modal BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    modelo VARCHAR(50),
    capacidade INT NOT NULL,
    ano_fabricacao INT,
    nome_empresa VARCHAR(50) NOT NULL,
    esta_em_manuntencao BOOLEAN NOT NULL
);


-- Tabela Viagem_Modal (Relacionamento entre viagem e modal)
CREATE TYPE tipo_enum AS ENUM ('ORIGEM', 'DESTINO, ESCALA');
CREATE TABLE viagem_modal (
    id_viagem BIGINT REFERENCES viagem(id_viagem),
    id_modal BIGINT REFERENCES modal(id_modal),
    tipo tipo_enum NOT NULL,
    PRIMARY KEY (id_viagem, id_modal)
);


-- Tabela Reserva
CREATE TABLE reserva (
        id_reserva BIGSERIAL PRIMARY KEY,
        data DATE,
        status VARCHAR(10),
        valor NUMERIC(10, 2),
        origem VARCHAR(50),
        destino VARCHAR(50),
        tipo_passagem VARCHAR(10),
        localizador VARCHAR(50),
        id_cliente BIGINT NOT NULL REFERENCES cliente (id_cliente),
        id_funcionario BIGINT REFERENCES funcionario (id_funcionario),
        id_viagem BIGINT REFERENCES viagem (id_viagem)
    );


-- Tabela Pagamento
CREATE TYPE metodo_pagamento_enum AS ENUM ('pix', 'boleto', 'cartao');
CREATE TABLE pagamento (
    id_pagamento SERIAL PRIMARY KEY,
    valor NUMERIC(10,2) NOT NULL,
    data_pagamento DATE NOT NULL,
    metodo_pagamento metodo_pagamento_enum,
    id_cartao  BIGINT REFERENCES cartao(id_cartao),
    id_reserva BIGINT NOT NULL REFERENCES reserva(id_reserva)
);


-- Tabela Parcelas
CREATE TABLE parcela (
    id_parcela SERIAL PRIMARY KEY,
    numero_parcela INT NOT NULL,
    valor_parcela NUMERIC(10,2),
    data_vencimento DATE NOT NULL,
    data_pagamento DATE NOT NULL,
    id_pagamento BIGINT NOT NULL REFERENCES pagamento(id_pagamento)
);


-- Tabela Ticket
CREATE TABLE ticket (
        id_ticket BIGSERIAL PRIMARY KEY,
        tipo_passagem VARCHAR(50),
        hora_de_partida TIMESTAMP NOT NULL,
        hora_de_chegada TIMESTAMP NOT NULL,
        localizador VARCHAR(50),
        id_reserva BIGINT NOT NULL REFERENCES reserva (id_reserva)
    );
    */