-- Tabela Cliente
INSERT INTO cliente (nome, data_nascimento, email, senha, cpf, profissao, cep, rua, numero_rua, uf, telefone) VALUES
('João Silva', '1990-05-15', 'joao.silva@example.com', 'senha123', '123.456.789-00', 'Engenheiro', '12345-678', 'Rua das Flores', '10', 'SP', '(11) 98765-4321'),
('Maria Oliveira', '1985-11-20', 'maria.oliveira@example.com', 'senha456', '987.654.321-00', 'Professora', '87654-321', 'Av. Paulista', '50', 'SP', '(11) 91234-5678');

-- Tabela Funcionario
INSERT INTO funcionario (nome, email, senha, cep, rua, numero_rua, cargo, login_inicial_realizado) VALUES
('Carlos Mendes', 'carlos.mendes@example.com', 'senha789', '54321-987', 'Rua Central', '100', 'GERENTE', TRUE),
('Ana Costa', 'ana.costa@example.com', 'senha321', '65432-198', 'Av. Brasil', '200', 'PADRAO', TRUE);

-- Tabela Ponto de Venda
INSERT INTO ponto_de_venda (nome, cnpj, cep, rua, numero_rua, telefone, id_gerente) VALUES
('Loja Central', '12345678000199', '12345-678', 'Rua Principal', '300', '(11) 99876-5432', 1);

-- Tabela Cidade
INSERT INTO cidade (nome, codigo) VALUES
('São Paulo', 'SP'),
('Rio de Janeiro', 'RJ');

-- Tabela Estado
INSERT INTO estado (sigla, nome) VALUES
('SP', 'São Paulo'),
('RJ', 'Rio de Janeiro');

-- Tabela Aeroporto
INSERT INTO aeroporto (nome, codigo) VALUES
('Aeroporto de Congonhas', 123),
('Aeroporto Galeão', 456);

-- Tabela Estacao
INSERT INTO estacao (nome) VALUES
('Estação Central'),
('Estação Norte');

-- Tabela Porto
INSERT INTO porto (nome) VALUES
('Porto de Santos'),
('Porto do Rio');

-- Tabela Local
INSERT INTO local (id_cidade, id_aeroporto, id_estacao, id_porto) VALUES
(1, 1, 1, 1),
(2, 2, 2, 2);

-- Tabela Modal
INSERT INTO modal (tipo, modelo, capacidade, ano_fabricacao, nome_empresa, esta_em_manuntencao) VALUES
('Avião', 'Boeing 737', 200, 2015, 'Gol Linhas Aéreas', FALSE),
('Navio', 'Cruzeiro 3000', 3000, 2018, 'MSC Cruzeiros', TRUE);

-- Tabela Viagem
INSERT INTO viagem (num_reservas_associadas, valor) VALUES
(10, 1500.00),
(5, 750.00);

-- Tabela Viagem Local
INSERT INTO conexao (id_viagem, id_local, id_modal, tipo, horario_partida, horario_chegada, data_partida, data_chegada) VALUES
(1, 1, 1, 'ORIGEM', '08:00', null , '2025-01-22', null),
(2, 2, null, 'DESTINO', null , '18:00', null, '2025-01-23');

-- Tabela Reserva
INSERT INTO reserva (data, status, valor, origem, destino, id_cliente, id_funcionario, id_viagem) VALUES
('2025-01-20', 'PENDENTE_PAGAMENTO', 1500.00, 'São Paulo', 'Rio de Janeiro', 1, 2, 1);

-- Tabela Passageiro
INSERT INTO passageiro (nome, data_nascimento, cpf, telefone, profissao, id_responsavel) VALUES
('José Santos', '2000-12-01', '123.456.789-01', '(11) 99876-5432', 'Estudante', NULL),
('Laura Martins', '2010-08-15', '987.654.321-02', '(21) 98765-4321', NULL, 1);

-- Tabela Reserva Passageiro
INSERT INTO reserva_passageiro (id_reserva, id_passageiro) VALUES
(1, 1),
(1, 2);

-- Tabela Cartão
INSERT INTO cartao (numero_encriptado, cvv_encriptado, validade, nome_titular, tipo, id_cliente) VALUES
('encrypted_number_1', 'encrypted_cvv_1', '12/30', 'João Silva', 'CREDITO', 1),
('encrypted_number_2', 'encrypted_cvv_2', '11/29', 'Maria Oliveira', 'DEBITO', 2);