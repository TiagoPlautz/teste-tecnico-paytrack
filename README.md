# Teste Técnico Paytrack

## Sobre o projeto

Este projeto foi desenvolvido como parte do teste técnico para a vaga de Desenvolvedor Fullstack na Paytrack.

A solução consiste em uma aplicação Fullstack para gerenciamento de cartões corporativos, composta por um backend desenvolvido em **Java com Spring Boot** e um frontend desenvolvido em **React com TypeScript**.

A aplicação permite realizar o cadastro e a visualização de cartões, aplicando regras de negócio relacionadas à validade, CVV, duplicidade e segurança dos dados. Também foi implementada uma funcionalidade para consulta de endereços a partir de CEP.

---

## Tecnologias utilizadas

### Backend

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Lombok
* SpringDoc / Swagger
* JUnit
* Mockito
* Maven

### Frontend

* React 19
* TypeScript
* Vite
* Tailwind CSS
* React Hot Toast
* Card Validator
* React SVG Credit Card Payment Icons
* Lucide React
* Oxlint
* npm

---

## Estrutura do projeto

O projeto foi organizado em duas aplicações independentes dentro do mesmo repositório:

```text
teste-tecnico-paytrack/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── README.md
│
└── README.md
```

Cada aplicação possui seu próprio README com instruções e informações mais detalhadas sobre implementação, configuração e execução.

---

## Funcionalidades

A solução contempla as seguintes funcionalidades:

* Cadastro de cartões corporativos;
* Listagem dos cartões cadastrados;
* Consulta detalhada de cartão por ID através da API;
* Mascaramento do número do cartão na listagem;
* Identificação da bandeira durante o preenchimento;
* Validação da data de validade;
* Validação da regra de negócio do CVV;
* Prevenção de cadastro de cartões duplicados;
* Criptografia dos dados sensíveis;
* Utilização de hash para identificação de duplicidades;
* Inativação automática de cartões vencidos;
* Consulta de endereço a partir de CEP;
* Feedback visual de sucesso e erro no frontend.

---

## Arquitetura da solução

O frontend é responsável pela interface e interação com o usuário, enquanto o backend concentra as regras de negócio, persistência dos dados e integração com serviços externos.

O fluxo principal da aplicação pode ser representado da seguinte forma:

```text
┌──────────────────────────┐
│         Frontend         │
│   React + TypeScript     │
│     Tailwind CSS         │
└────────────┬─────────────┘
             │
             │ HTTP / REST
             ▼
┌──────────────────────────┐
│          Backend         │
│   Java + Spring Boot     │
│                          │
│  Regras de negócio       │
│  Criptografia / Hash     │
│  Validações              │
└───────┬──────────┬───────┘
        │          │
        ▼          ▼
┌─────────────┐  ┌─────────────┐
│ PostgreSQL  │  │   ViaCEP    │
│             │  │    / Mock   │
└─────────────┘  └─────────────┘
```

O frontend não acessa diretamente o banco de dados ou o ViaCEP. Toda comunicação é intermediada pela API desenvolvida no backend.

---

## Regras de negócio

### Validade do cartão

A data de validade informada deve ser posterior à data e hora atuais.

Cartões vencidos não podem ser cadastrados.

Além disso, uma rotina agendada no backend verifica cartões que venceram após o cadastro e altera automaticamente seu status para inativo.

### Validação do CVV

O CVV é convertido para seu valor numérico durante o cadastro.

Conforme a regra definida para o teste, cartões cujo valor numérico completo do CVV seja par não podem ser cadastrados.

Exemplos:

```text
357 → válido
438 → inválido
```

### Controle de duplicidade

O mesmo número de cartão não pode ser cadastrado mais de uma vez.

Para realizar essa verificação sem depender da descriptografia dos cartões existentes, o backend utiliza um hash HMAC-SHA-256 gerado a partir do número original.

### Proteção dos dados

O número do cartão e o CVV são criptografados utilizando AES antes de serem armazenados no banco de dados.

Na listagem geral, o número do cartão é apresentado de forma mascarada:

```text
**** **** **** 1234
```

---

## Consulta de CEP

A aplicação também permite consultar um endereço a partir de um CEP.

O frontend envia a solicitação para o backend:

```text
Frontend
   ↓
GET /enderecos/{cep}
   ↓
Backend
   ↓
ViaCEP ou Mock
   ↓
Backend
   ↓
Frontend
```

A comunicação com o serviço externo fica centralizada no backend, evitando que o frontend tenha dependência direta do ViaCEP.

Durante o desenvolvimento foi identificada uma restrição da rede corporativa relacionada à validação do certificado HTTPS do ViaCEP.

Por esse motivo, o backend possui um modo mockado configurável, permitindo demonstrar e validar o fluxo completo da funcionalidade sem alterar o contrato entre frontend e backend.

---

## Testes

As principais regras de negócio do backend possuem testes unitários desenvolvidos utilizando **JUnit e Mockito**.

As dependências do `CartaoService`, como repositório, serviço de criptografia e serviço de geração de hash, são simuladas durante os testes, permitindo validar as regras de negócio de forma isolada.

Entre os cenários testados estão as regras relacionadas à validade do cartão e à validação do CVV.

---

## Executando o projeto

Para utilizar a aplicação completa, o backend e o frontend devem ser executados separadamente.

### 1. Backend

Acesse:

```bash
cd backend
```

Execute a aplicação utilizando Maven/Maven Wrapper conforme detalhado no README do backend.

Por padrão, a API ficará disponível em:

```text
http://localhost:8080
```

A documentação Swagger pode ser acessada em:

```text
http://localhost:8080/swagger-ui/index.html
```

### 2. Frontend

Em outro terminal, acesse:

```bash
cd frontend
```

Instale as dependências:

```bash
npm install
```

Inicie a aplicação:

```bash
npm run dev
```

Por padrão, o Vite disponibiliza a aplicação em:

```text
http://localhost:5173
```

---

## Documentação detalhada

Informações específicas sobre configuração, estrutura, regras de negócio e decisões técnicas estão disponíveis nos READMEs individuais de cada aplicação:

* **Backend:** `backend/README.md`
* **Frontend:** `frontend/README.md`

---

## Resumo da solução

A solução foi estruturada buscando manter uma separação clara de responsabilidades entre frontend e backend.

O **frontend** concentra a interface, experiência do usuário, formatação dos campos e comunicação com a API.

O **backend** concentra as regras de negócio, persistência, segurança dos dados, tratamento de erros, consulta de endereços e manutenção automática do status dos cartões.

A divisão permite que cada aplicação evolua de forma independente, mantendo a comunicação por meio de uma API REST bem definida.
