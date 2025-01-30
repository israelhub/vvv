-- Inserir dados na tabela cliente
INSERT INTO cliente (nome, data_nascimento, email, senha, cpf, profissao, cep, rua, numero_rua, uf, telefone)
VALUES 
('João Silva', '1980-05-15', 'joao.silva@example.com', 'senha123', '123.456.789-00', 'Engenheiro', '12345-678', 'Rua A', '123', 'SP', '(11) 91234-5678'),
('Maria Oliveira', '1992-07-22', 'maria.oliveira@example.com', 'senha456', '987.654.321-00', 'Médica', '98765-432', 'Rua B', '456', 'RJ', '(21) 99876-5432');

-- Inserir dados na tabela funcionario
INSERT INTO funcionario (nome, email, senha, cep, rua, numero_rua, cargo, login_inicial_realizado)
VALUES 
('Carlos Pereira', 'carlos.pereira@example.com', 'senha789', '54321-987', 'Rua C', '789', 'GERENTE', TRUE),
('Ana Costa', 'ana.costa@example.com', 'senha321', '67890-123', 'Rua D', '101', 'PADRAO', FALSE),
('Pedro Santos', 'pedro.santos@example.com', 'senha123', '09876-543', 'Rua E', '112', 'GERENTE_DE_NEGOCIOS_VIRTUAIS', TRUE);

-- Inserir dados na tabela ponto_de_venda
INSERT INTO ponto_de_venda (nome, cnpj, cep, rua, numero_rua, telefone, id_gerente)
VALUES 
('Loja 1', '12345678000123', '11223-344', 'Avenida Principal', '50', '(11) 92345-6789', 1),
('Loja 2', '98765432000198', '22334-455', 'Avenida Secundária', '100', '(21) 93456-7890', 2);

-- Inserir dados na tabela ponto_funcionario
INSERT INTO ponto_funcionario (id_funcionario, id_ponto_de_venda, dia_semana, horario_inicial, horario_final)
VALUES 
(1, 1, 'SEGUNDA', '08:00:00', '17:00:00'),
(2, 2, 'TERCA', '09:00:00', '18:00:00');

-- Inserir dados na tabela viagem
INSERT INTO viagem (num_reservas_associadas, valor, horario_partida, horario_chegada, data_partida, data_chegada)
VALUES 
(0, 1, '08:00:00', '12:00:00', '2025-02-01', '2025-02-01');

-- Inserir dados na tabela cidade
INSERT INTO cidade (nome, codigo)
VALUES 
('São Paulo', 'SP1'),
('Rio de Janeiro', 'RJ2');

-- Inserir dados na tabela estado
INSERT INTO estado (nome, sigla)
VALUES 
('São Paulo', 'SP'),
('Rio de Janeiro', 'RJ');

-- Inserir dados na tabela aeroporto
INSERT INTO aeroporto (nome, codigo)
VALUES 
('Aeroporto de Guarulhos', 1001),
('Aeroporto Santos Dumont', 2002);

-- Inserir dados na tabela estacao
INSERT INTO estacao (nome)
VALUES 
('Estação Central'),
('Estação Rodoviária');

-- Inserir dados na tabela porto
INSERT INTO porto (nome)
VALUES 
('Porto de Santos'),
('Porto do Rio');

-- Inserir dados na tabela local
INSERT INTO local (id_cidade, id_aeroporto)
VALUES 
(1, 1),
(2, 2);

-- Inserir dados na tabela viagem_local
INSERT INTO viagem_local (id_viagem, id_local, tipo)
VALUES 
(1, 1, 'ORIGEM'),
(1, 2, 'DESTINO');

-- Inserir dados na tabela modal
INSERT INTO modal (tipo, modelo, capacidade, ano_fabricacao, nome_empresa, esta_em_manuntencao)
VALUES 
('Ônibus', 'Volvo X', 50, 2019, 'Viações Unidas', FALSE),
('Avião', 'Boeing 737', 180, 2020, 'Companhia Aérea', TRUE);

-- Inserir dados na tabela viagem_modal
INSERT INTO viagem_modal (id_viagem, id_modal, tipo)
VALUES 
(1, 1, 'ORIGEM');
