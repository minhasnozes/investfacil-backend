-- V1__create_initial_schema.sql
-- Estrutura inicial do InvestFácil: usuarios, contas, produtos, investimentos, transacoes

CREATE TABLE usuarios (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome                VARCHAR(150) NOT NULL,
    email               VARCHAR(150) NOT NULL UNIQUE,
    senha_hash          VARCHAR(255) NOT NULL,
    cpf                 VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento     DATE NOT NULL,
    telefone            VARCHAR(20),
    perfil_investidor   VARCHAR(20) CHECK (perfil_investidor IN ('CONSERVADOR', 'MODERADO', 'ARROJADO')),
    data_cadastro       TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE contas (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id          UUID NOT NULL UNIQUE REFERENCES usuarios(id),
    saldo_disponivel    NUMERIC(15, 2) NOT NULL DEFAULT 0,
    saldo_investido     NUMERIC(15, 2) NOT NULL DEFAULT 0,
    data_criacao        TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE produtos (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome                VARCHAR(150) NOT NULL,
    tipo                VARCHAR(30) NOT NULL CHECK (tipo IN ('CDB', 'TESOURO_SELIC', 'TESOURO_PREFIXADO', 'TESOURO_IPCA', 'FUNDO')),
    taxa_rentabilidade  NUMERIC(7, 4) NOT NULL,
    indexador           VARCHAR(20) CHECK (indexador IN ('CDI', 'SELIC', 'IPCA', 'PREFIXADO')),
    prazo_meses         INTEGER,
    liquidez            VARCHAR(20) NOT NULL CHECK (liquidez IN ('DIARIA', 'NO_VENCIMENTO')),
    risco               VARCHAR(10) NOT NULL CHECK (risco IN ('BAIXO', 'MEDIO', 'ALTO')),
    valor_minimo        NUMERIC(15, 2) NOT NULL,
    data_cadastro       TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE investimentos (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conta_id            UUID NOT NULL REFERENCES contas(id),
    produto_id          UUID NOT NULL REFERENCES produtos(id),
    valor_aplicado      NUMERIC(15, 2) NOT NULL,
    valor_atual         NUMERIC(15, 2) NOT NULL,
    data_aplicacao      DATE NOT NULL,
    data_vencimento     DATE,
    status              VARCHAR(20) NOT NULL DEFAULT 'ATIVO' CHECK (status IN ('ATIVO', 'RESGATADO', 'VENCIDO'))
);

CREATE TABLE transacoes (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conta_id            UUID NOT NULL REFERENCES contas(id),
    investimento_id     UUID REFERENCES investimentos(id),
    tipo                VARCHAR(20) NOT NULL CHECK (tipo IN ('DEPOSITO', 'APORTE', 'RESGATE', 'RENDIMENTO')),
    valor               NUMERIC(15, 2) NOT NULL,
    data_hora           TIMESTAMP NOT NULL DEFAULT now()
);

-- Índices para as chaves estrangeiras mais consultadas
CREATE INDEX idx_investimentos_conta_id ON investimentos(conta_id);
CREATE INDEX idx_investimentos_produto_id ON investimentos(produto_id);
CREATE INDEX idx_transacoes_conta_id ON transacoes(conta_id);
CREATE INDEX idx_transacoes_investimento_id ON transacoes(investimento_id);