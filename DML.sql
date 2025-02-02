-- Inserindo Estados
INSERT INTO estado (sigla, nome) VALUES 
('SP', 'São Paulo'),
('RJ', 'Rio de Janeiro'),
('MG', 'Minas Gerais'),
('RS', 'Rio Grande do Sul'),
('BA', 'Bahia');

-- Inserindo Cidades
INSERT INTO cidade (nome, codigo) VALUES 
('São Paulo', 'SPO'),
('Rio de Janeiro', 'RIO'),
('Belo Horizonte', 'BHZ'),
('Porto Alegre', 'POA'),
('Salvador', 'SSA');

-- Inserindo Aeroportos
INSERT INTO aeroporto (nome, codigo) VALUES 
('Aeroporto de Guarulhos', 1),
('Aeroporto Santos Dumont', 2),
('Aeroporto de Confins', 3),
('Aeroporto Salgado Filho', 4),
('Aeroporto de Salvador', 5);

-- Inserindo Estações
INSERT INTO estacao (nome) VALUES 
('Estação da Luz'),
('Central do Brasil'),
('Estação Pampulha'),
('Estação Mercado'),
('Estação Campo da Pólvora');

-- Inserindo Portos
INSERT INTO porto (nome) VALUES 
('Porto de Santos'),
('Porto do Rio de Janeiro'),
('Porto de Tubarão'),
('Porto de Rio Grande'),
('Porto de Salvador');

-- Inserindo Locais
INSERT INTO local (id_cidade, id_aeroporto, id_estacao, id_porto) VALUES 
(1, 1, 1, 1),  -- São Paulo
(2, 2, 2, 2),  -- Rio de Janeiro
(3, 3, 3, 3),  -- Belo Horizonte
(4, 4, 4, 4),  -- Porto Alegre
(5, 5, 5, 5);  -- Salvador

-- Inserindo Funcionários
INSERT INTO funcionario (nome, email, senha, cargo, cep, rua, numero_rua) VALUES 
-- Gerentes de Negócios Virtuais
('Maria Silva', 'maria.silva@empresa.com', 'senha123', 'GERENTE_DE_NEGOCIOS_VIRTUAIS', '01001-000', 'Rua Augusta', '100'),
-- Gerentes
('João Santos', 'joao.santos@empresa.com', 'senha123', 'GERENTE', '20001-000', 'Av. Rio Branco', '200'),
('Ana Oliveira', 'ana.oliveira@empresa.com', 'senha123', 'GERENTE', '30001-000', 'Av. Afonso Pena', '300'),
-- Funcionários Padrão
('Pedro Costa', 'pedro.costa@empresa.com', 'senha123', 'PADRAO', '40001-000', 'Av. Sete de Setembro', '400'),
('Lucas Ferreira', 'lucas.ferreira@empresa.com', 'senha123', 'PADRAO', '50001-000', 'Rua Chile', '500');

-- Inserindo Pontos de Venda
INSERT INTO ponto_de_venda (nome, cnpj, telefone, cep, rua, numero_rua, id_gerente) VALUES 
('Loja São Paulo', '12345678000101', '11999999999', '01001-000', 'Rua Augusta', '100', 2),
('Loja Rio de Janeiro', '12345678000102', '21999999999', '20001-000', 'Av. Rio Branco', '200', 2),
('Loja Belo Horizonte', '12345678000103', '31999999999', '30001-000', 'Av. Afonso Pena', '300', 3),
('Loja Porto Alegre', '12345678000104', '51999999999', '40001-000', 'Av. Sete de Setembro', '400', 3),
('Loja Salvador', '12345678000105', '71999999999', '50001-000', 'Rua Chile', '500', 3);

-- Inserindo associações entre funcionários e pontos de venda
INSERT INTO ponto_funcionario (id_funcionario, id_ponto_de_venda, dia_semana, horario_inicial, horario_final) VALUES 
(4, 1, 'SEGUNDA', '09:00', '18:00'),
(4, 1, 'TERCA', '09:00', '18:00'),
(5, 2, 'QUARTA', '09:00', '18:00'),
(5, 2, 'QUINTA', '09:00', '18:00'),
(4, 3, 'SEXTA', '09:00', '18:00');

-- Inserindo Clientes
INSERT INTO cliente (nome, data_nascimento, email, senha, cpf, profissao, telefone) VALUES 
('Carlos Silva', '1990-05-15', 'carlos.silva@email.com', 'senha123', '123.456.789-00', 'Engenheiro', '11999888777'),
('Maria Santos', '1985-08-20', 'maria.santos@email.com', 'senha456', '987.654.321-00', 'Advogada', '11988777666'),
('Pedro Oliveira', '1995-03-10', 'pedro.oliveira@email.com', 'senha789', '456.789.123-00', 'Médico', '11977666555');

-- Inserindo Passageiros
INSERT INTO passageiro (nome, data_nascimento, cpf, telefone, profissao) VALUES 
('José Pereira', '1980-01-01', '111.222.333-44', '11966555444', 'Professor'),
('Ana Costa', '1992-06-15', '555.666.777-88', '11955444333', 'Dentista'),
('Mariana Lima', '1988-12-25', '999.888.777-66', '11944333222', 'Arquiteta');

-- Inserindo Transportadoras
INSERT INTO transportadora (nome, cnpj) VALUES
('LATAM', '12345678901234'),
('GOL', '23456789012345'),
('Viação União', '34567890123456'),
('CPTM', '45678901234567'),
('Costa', '56789012345678');

-- Inserir modais
INSERT INTO modal (tipo, modelo, capacidade, ano_fabricacao, id_transportadora) VALUES 
('AVIAO', 'Boeing 737', 180, 2018, 1),      -- LATAM
('ONIBUS', 'Mercedes Benz O500', 46, 2020, 3),  -- Viação União
('NAVIO', 'Costa Cruzeiros', 2000, 2015, 5),  -- Costa
('TREM', 'Alstom Pendolino', 400, 2019, 4),    -- CPTM
('AVIAO', 'Airbus A320', 150, 2017, 2);       -- GOL

-- Inserindo manutenções de modais
INSERT INTO manutencao_modal (id_modal, data_inicio, data_fim, descricao) VALUES
(1, '2024-01-01', '2024-01-05', 'Manutenção preventiva do Boeing 737'),
(2, '2024-02-01', '2024-02-03', 'Revisão do sistema de freios'),
(3, '2024-03-01', NULL, 'Manutenção do casco em andamento');

-- Inserindo Viagens
INSERT INTO viagem (num_reservas_associadas, valor, id_origem, id_destino, id_modal, horario_chegada, horario_partida) VALUES 
(0, 500.00, 1, 2, 1, '2024-03-20 10:00:00', '2024-03-20 08:00:00'),
(0, 300.00, 2, 3, 2, '2024-03-21 14:00:00', '2024-03-21 10:00:00'),
(0, 1500.00, 3, 4, 3, '2024-03-22 20:00:00', '2024-03-22 08:00:00');

-- Inserindo Escalas
INSERT INTO escala (id_viagem, id_origem, id_destino, id_modal, horario_partida, horario_chegada, ordem) VALUES 
(1, 1, 3, 1, '2024-03-20 08:30:00', '2024-03-20 09:00:00', 1),
(2, 2, 4, 2, '2024-03-21 11:30:00', '2024-03-21 12:30:00', 1);

-- Inserindo Reservas
INSERT INTO reserva (data, status, valor_total, origem, destino, id_cliente, id_funcionario, id_viagem) VALUES 
('2024-03-19', 'CONFIRMADA', 500.00, 'São Paulo', 'Rio de Janeiro', 1, 4, 1),
('2024-03-20', 'PENDENTE_PAGAMENTO', 300.00, 'Rio de Janeiro', 'Belo Horizonte', 2, 4, 2);

-- Inserindo Cartões
INSERT INTO cartao (numero_encriptado, cvv_encriptado, validade, nome_titular, tipo, id_cliente) VALUES 
('encrypted_4532111122223333', 'encrypted_123', '12/2025', 'Carlos Silva', 'CREDITO', 1),
('encrypted_4532444455556666', 'encrypted_456', '10/2026', 'Maria Santos', 'DEBITO', 2);

-- Inserindo Pagamentos
INSERT INTO pagamento (id_reserva, id_cartao, num_parcelas, status_pagamento) VALUES 
(1, 1, 1, 'AUTORIZADO'),
(2, 2, 2, 'AUTORIZADO');

-- Inserindo Parcelas
INSERT INTO parcela (id_pagamento, numero_parcela, valor_parcela) VALUES 
(1, 1, 500.00),
(2, 1, 150.00),
(2, 2, 150.00);

-- Inserindo Tickets
INSERT INTO ticket (tipo_passagem, localizador, origem, destino, hora_partida, hora_chegada, id_reserva, id_passageiro) VALUES 
('ADULTO', 'LOC123A', 'São Paulo', 'Belo Horizonte', '2024-03-20 08:00:00', '2024-03-20 09:00:00', 1, 1),
('ADULTO', 'LOC123B', 'Belo Horizonte', 'Rio de Janeiro', '2024-03-20 09:00:00', '2024-03-20 10:00:00', 1, 1),
('ADULTO', 'LOC456A', 'Rio de Janeiro', 'Porto Alegre', '2024-03-21 10:00:00', '2024-03-21 12:30:00', 2, 2),
('ADULTO', 'LOC456B', 'Porto Alegre', 'Belo Horizonte', '2024-03-21 12:30:00', '2024-03-21 14:00:00', 2, 2);

-- Inserindo Reserva_Passageiro
INSERT INTO reserva_passageiro (id_reserva, id_passageiro) VALUES 
(1, 1),
(2, 2);