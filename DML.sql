-- Inserção de dados básicos
INSERT INTO cliente (nome, data_nascimento, email, senha, cpf, profissao, cep, rua, numero_rua, uf, telefone)
VALUES 
('João Silva', '1990-05-15', 'joao@email.com', 'senha123', '123.456.789-00', 'Engenheiro', '12345-678', 'Rua das Flores', '123', 'SP', '(11)99999-9999'),
('Maria Santos', '1985-08-20', 'maria@email.com', 'senha123', '987.654.321-00', 'Médica', '87654-321', 'Av Principal', '456', 'RJ', '(21)88888-8888');

INSERT INTO funcionario (nome, email, senha, cep, rua, numero_rua, cargo, login_inicial_realizado)
VALUES 
('Carlos Gerente', 'carlos@empresa.com', 'senha123', '13579-246', 'Rua Comercial', '789', 'GERENTE', TRUE),
('Ana Vendedora', 'ana@empresa.com', 'senha123', '24680-135', 'Rua do Comércio', '012', 'PADRAO', FALSE),
('Pedro GNV', 'pedro@empresa.com', 'senha123', '12345-678', 'Rua das Flores', '123', 'GERENTE_DE_NEGOCIOS_VIRTUAIS', TRUE);  

INSERT INTO cidade (nome, codigo)
VALUES 
('São Paulo', 'SPO'),
('Rio de Janeiro', 'RIO'),
('Salvador', 'SAL'),
('Recife', 'REC'),
('Manaus', 'MAO'),
('Brasília', 'BSB'),
('Porto Alegre', 'POA'),
('Curitiba', 'CWB'),
('Fortaleza', 'FOR'),
('Belo Horizonte', 'BHZ');

INSERT INTO estado (sigla, nome)
VALUES 
('SP', 'São Paulo'),
('RJ', 'Rio de Janeiro'),
('BA', 'Bahia'),
('PE', 'Pernambuco'),
('AM', 'Amazonas'),
('DF', 'Distrito Federal'),
('RS', 'Rio Grande do Sul'),
('PR', 'Paraná'),
('CE', 'Ceará'),
('MG', 'Minas Gerais');

INSERT INTO aeroporto (nome, codigo)
VALUES 
('Aeroporto de Guarulhos', 1001),
('Aeroporto Santos Dumont', 1002),
('Aeroporto Internacional de Salvador', 1003),
('Aeroporto Internacional do Recife', 1004),
('Aeroporto Internacional de Manaus', 1005),
('Aeroporto Internacional de Brasília', 1006),
('Aeroporto Internacional de Porto Alegre', 1007),
('Aeroporto Internacional de Curitiba', 1008),
('Aeroporto Internacional de Fortaleza', 1009),
('Aeroporto Internacional de Confins', 1010);

INSERT INTO estacao (nome)
VALUES 
('Estação Central'),
('Terminal Rodoviário'),
('Terminal Rodoviário de Salvador'),
('Terminal Integrado de Recife'),
('Terminal Central de Manaus'),
('Rodoviária de Brasília'),
('Estação Rodoviária de Porto Alegre'),
('Terminal Rodoviário de Curitiba'),
('Terminal Rodoviário de Fortaleza'),
('Terminal Rodoviário de Belo Horizonte');

INSERT INTO porto (nome)
VALUES 
('Porto de Santos'),
('Porto do Rio'),
('Porto de Salvador'),
('Porto do Recife'),
('Porto de Manaus'),
('Porto de Porto Alegre'),
('Porto de Paranaguá'),
('Porto do Mucuripe'),
('Terminal Portuário de Tubarão');

-- Inserção em tabelas com dependências
INSERT INTO ponto_de_venda (nome, cnpj, cep, rua, numero_rua, telefone, id_gerente)
VALUES 
('Loja Centro', '12345678901234', '11111-111', 'Rua Central', '100', '(11)1111-1111', 1),
('Loja Shopping', '56789012345678', '22222-222', 'Av Shopping', '200', '(11)2222-2222', 1);

INSERT INTO local (id_cidade, id_aeroporto, id_estacao, id_porto)
VALUES 
(1, 1, 1, 1),
(2, 2, 2, 2),
(3, 3, 3, 3),  -- Salvador
(4, 4, 4, 4),  -- Recife
(5, 5, 5, 5),  -- Manaus
(6, 6, 6, null),  -- Brasília
(7, 7, 7, 6),  -- Porto Alegre
(8, 8, 8, 7),  -- Curitiba
(9, 9, 9, 8),  -- Fortaleza
(10, 10, 10, 9);  -- Belo Horizonte

INSERT INTO modal (tipo, modelo, capacidade, ano_fabricacao, nome_empresa, esta_em_manuntencao)
VALUES 
('AVIAO', 'Boeing 737', 180, 2015, 'LATAM', FALSE),
('ONIBUS', 'Mercedes Benz', 45, 2018, 'Viação União', FALSE);

INSERT INTO viagem (num_reservas_associadas, valor, id_origem, id_destino, id_modal, horario_chegada, horario_partida)
VALUES 
(0, 500.00, 1, 2, 1, '2024-03-20 14:00:00', '2024-03-20 12:00:00'),
(0, 300.00, 2, 1, 1, '2024-03-21 16:00:00', '2024-03-21 14:00:00');

-- Inserção em tabelas associativas
INSERT INTO ponto_funcionario (id_funcionario, id_ponto_de_venda, dia_semana, horario_inicial, horario_final)
VALUES 
(2, 1, 'SEGUNDA', '08:00:00', '17:00:00'),
(2, 1, 'TERCA', '08:00:00', '17:00:00');

INSERT INTO viagem_modal (id_viagem, id_modal, tipo)
VALUES 
(1, 1, 'ORIGEM'),
(2, 2, 'ORIGEM');

INSERT INTO passageiro (nome, data_nascimento, cpf, telefone, profissao)
VALUES 
('Pedro Viajante', '1995-03-10', '111.222.333-44', '(11)77777-7777', 'Advogado'),
('Julia Turista', '1992-07-15', '444.555.666-77', '(21)66666-6666', 'Professora');

INSERT INTO reserva (data, status, valor, origem, destino, id_cliente, id_funcionario, id_viagem)
VALUES 
('2024-03-19', 'CONFIRMADA', 500.00, 'São Paulo', 'Rio de Janeiro', 1, 2, 1),
('2024-03-20', 'PENDENTE_PAGAMENTO', 300.00, 'Rio de Janeiro', 'São Paulo', 2, 2, 2);

INSERT INTO reserva_passageiro (id_reserva, id_passageiro)
VALUES 
(1, 1),
(2, 2);

INSERT INTO cartao (numero_encriptado, cvv_encriptado, validade, nome_titular, tipo, id_cliente)
VALUES 
('encrypted_123', 'encrypted_456', '12/2025', 'JOAO SILVA', 'CREDITO', 1),
('encrypted_789', 'encrypted_012', '06/2026', 'MARIA SANTOS', 'DEBITO', 2);

INSERT INTO ticket (tipo_passagem, localizador, hora_partida, hora_chegada, id_reserva, id_passageiro)
VALUES 
('EXECUTIVA', 'ABC123', '2024-03-20 12:00:00', '2024-03-20 14:00:00', 1, 1),
('ECONOMICA', 'DEF456', '2024-03-21 14:00:00', '2024-03-21 16:00:00', 2, 2);

INSERT INTO escala (id_viagem, id_origem, id_destino, id_modal, horario_partida, horario_chegada, ordem)
VALUES 
(1, 1, 2, 1, '2024-03-20 12:00:00', '2024-03-20 14:00:00', 1),
(2, 2, 1, 2, '2024-03-21 14:00:00', '2024-03-21 16:00:00', 1);