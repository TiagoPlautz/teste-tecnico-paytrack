# Teste Técnico Paytrack - Backend

## Sobre o projeto

O projeto consiste no desenvolvimento de uma aplicação backend para gerenciamento de cartões corporativos, disponibilizando uma API REST para cadastro, listagem e consulta de cartões.

A aplicação disponibiliza os seguintes recursos:

* Cadastro de novos cartões corporativos;
* Listagem dos cartões cadastrados, retornando apenas os dados necessários para exibição;
* Consulta detalhada de um cartão por ID, disponibilizada pela API, mas não utilizada atualmente pelo frontend;
* Consulta de endereço a partir de um CEP;
* Validação das principais regras de negócio relacionadas ao cadastro de cartões;
* Inativação automática de cartões vencidos;
* Proteção de dados sensíveis por meio de criptografia e hash.

A consulta de CEP foi estruturada para ser realizada pelo backend por meio da integração com o serviço ViaCEP. Entretanto, devido a uma restrição da rede corporativa utilizada durante o desenvolvimento, o acesso HTTPS ao serviço externo ficou indisponível.

Por esse motivo, foi implementado um modo mockado no backend, permitindo validar integralmente o fluxo entre frontend e backend sem alterar o contrato da API.

---

## Tecnologias utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* Lombok
* SpringDoc / Swagger
* PostgreSQL
* JUnit
* Mockito
* Maven

---

## Pré-requisitos

Para executar a aplicação, é necessário possuir:

* Java 21
* PostgreSQL
* Maven

---

## Banco de dados

A aplicação utiliza PostgreSQL para persistência dos dados.

Crie um banco de dados com o seguinte nome:

```text
teste_tecnico_pay
```

As credenciais de acesso ao banco devem ser configuradas por meio das seguintes variáveis de ambiente:

```text
BD_USERNAME
BD_PASSWORD
```

---

## Variáveis de ambiente

A aplicação utiliza variáveis de ambiente para evitar que informações sensíveis, como credenciais e chaves criptográficas, sejam armazenadas diretamente no código-fonte.

As seguintes variáveis devem ser configuradas:

```text
CRYPTO_KEY
HASH_KEY
BD_USERNAME
BD_PASSWORD
```

* `CRYPTO_KEY`: chave utilizada no processo de criptografia e descriptografia dos dados sensíveis dos cartões;
* `HASH_KEY`: chave utilizada para geração do hash do número do cartão;
* `BD_USERNAME`: usuário de acesso ao PostgreSQL;
* `BD_PASSWORD`: senha de acesso ao PostgreSQL.

> As chaves e credenciais utilizadas localmente não devem ser versionadas no repositório.

---

## Configuração da consulta de CEP

A aplicação permite alternar entre o modo mockado e a integração real com o ViaCEP por meio da seguinte configuração no `application.yaml`:

```yaml
app:
  endereco:
    mock: true
```

### Modo mockado

Quando configurado como:

```yaml
mock: true
```

a aplicação não realiza chamadas externas ao ViaCEP.

O backend utiliza respostas mockadas para determinados CEPs previamente definidos, permitindo validar o fluxo de consulta sem depender da disponibilidade do serviço externo.

Esse modo foi utilizado durante o desenvolvimento devido a uma restrição da rede corporativa, que impede a validação adequada do certificado HTTPS durante a comunicação com o ViaCEP.

### Integração real

Quando configurado como:

```yaml
mock: false
```

o backend realiza a consulta diretamente no ViaCEP.

Nesse cenário, o ambiente de execução deve possuir acesso HTTPS ao serviço:

```text
https://viacep.com.br
```

O frontend não precisa sofrer nenhuma alteração ao alternar entre os modos, pois continua consumindo o mesmo endpoint disponibilizado pelo backend.

---

## Swagger

A documentação dos endpoints da API pode ser acessada por meio do Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Principais endpoints

### POST `/cartoes`

Realiza o cadastro de um novo cartão.

### GET `/cartoes`

Retorna a lista de cartões cadastrados.

### GET `/cartoes/{id}`

Retorna os dados detalhados de um cartão a partir do seu ID.

### GET `/enderecos/{cep}`

Realiza a consulta de um endereço a partir do CEP informado.

---

## Regras de negócio

O serviço de cartões concentra as principais regras de negócio relacionadas ao cadastro, consulta, segurança e manutenção dos cartões.

### Validação de cartão duplicado

Antes de realizar o cadastro, a aplicação gera um hash a partir do número original do cartão e verifica se já existe um registro associado a esse hash.

Caso o cartão já esteja cadastrado, a operação é rejeitada e a API retorna uma mensagem informando que o número do cartão já existe.

Essa validação permite identificar duplicidades sem a necessidade de descriptografar os números dos cartões armazenados no banco de dados.

### Validação da data de validade

A data de validade é obrigatória e validada durante o cadastro.

Para que o cartão seja cadastrado, sua data de validade deve ser posterior à data e hora atuais.

Caso a data seja nula, igual ou anterior ao momento atual, o cadastro é rejeitado e a API informa que o cartão está vencido.

### Validação do CVV

Durante o cadastro, o CVV é convertido para um valor numérico para aplicação da regra de negócio.

Não é permitido cadastrar um cartão cujo valor numérico completo do CVV seja par. A validação é realizada verificando o resto da divisão do valor por `2`.

Dessa forma:

* `357` é aceito, pois seu valor numérico é ímpar;
* `438` é rejeitado, pois seu valor numérico é par.

Caso o CVV seja considerado inválido, o cadastro é rejeitado e a API retorna a mensagem correspondente.

### Criptografia dos dados sensíveis

Após a execução das validações de negócio, o número do cartão e o CVV são criptografados antes da persistência no banco de dados.

A aplicação utiliza criptografia AES para evitar que esses dados sejam armazenados diretamente em texto puro.

A descriptografia ocorre somente nos fluxos em que a aplicação precisa recuperar os valores originais.

### Hash do número do cartão

Além da versão criptografada, a aplicação armazena um hash gerado a partir do número original do cartão.

O hash é gerado utilizando HMAC-SHA-256 e é utilizado para auxiliar na identificação de cartões já cadastrados.

Utilizando a mesma chave e o mesmo número de cartão como entrada, o processo gera o mesmo resultado, permitindo consultar a existência de um cartão sem precisar descriptografar os registros existentes.

### Mascaramento do número do cartão

Na listagem geral de cartões, o número completo não é retornado ao consumidor da API.

Após recuperar e descriptografar o número, a aplicação aplica uma máscara e disponibiliza somente os quatro últimos dígitos.

Exemplo:

```text
**** **** **** 1234
```

Essa abordagem reduz a exposição do número completo do cartão nos fluxos em que essa informação não é necessária.

### Consulta detalhada do cartão

A consulta individual por ID retorna os dados detalhados do cartão.

Antes de retornar os dados, a aplicação verifica se existe um cartão correspondente ao ID informado.

Caso nenhum registro seja encontrado, é retornado um erro informando que o cartão solicitado não existe.

Nesse fluxo, os dados criptografados necessários são descriptografados para composição da resposta detalhada.

> O endpoint de consulta detalhada está disponível na API, porém não é utilizado atualmente pelo frontend da aplicação.

### Status do cartão

No momento do cadastro, o status do cartão é definido de acordo com sua data de validade.

Como cartões vencidos são rejeitados pela validação anterior, os cartões cadastrados com sucesso são inicialmente persistidos como ativos.

### Inativação automática de cartões vencidos

Como um cartão válido no momento do cadastro poderá vencer posteriormente, a aplicação possui uma rotina responsável pela atualização de seu status.

A rotina consulta os cartões que ainda estão ativos e cuja data de validade já tenha sido ultrapassada.

Os cartões encontrados têm seu status alterado automaticamente para inativo.

Essa operação é executada de forma transacional e em conjunto com um scheduler da aplicação, garantindo que cartões vencidos não permaneçam ativos indefinidamente.

### Consulta de CEP

Antes da consulta, o CEP é normalizado e validado para garantir que possua exatamente 8 dígitos.

Quando o modo mock está habilitado, a aplicação consulta os dados simulados configurados no backend.

Quando o modo mock está desabilitado, a aplicação realiza a consulta diretamente no ViaCEP.

Caso o CEP informado não seja encontrado, a API retorna uma resposta de erro informando que o endereço solicitado não existe.

---

## Estrutura do projeto

O backend foi organizado separando as responsabilidades da aplicação em pacotes específicos, facilitando a manutenção, leitura e evolução do código.

```text
src/
├── main/
│   ├── java/
│   │   └── br/com/paytrack/testeTecnico/
│   │       │
│   │       ├── cartao/
│   │       │   ├── controller/
│   │       │   ├── crypto/
│   │       │   ├── dto/
│   │       │   ├── entity/
│   │       │   ├── hash/
│   │       │   ├── repository/
│   │       │   ├── scheduler/
│   │       │   └── service/
│   │       │
│   │       ├── endereco/
│   │       │   ├── controller/
│   │       │   ├── dto/
│   │       │   └── service/
│   │       │
│   │       ├── exception/
│   │       │ 
│   │       ├── handler/
│   │       │
│   │       └── TesteTecnicoApplication.java
│   │
│   └── resources/
│       └── application.yaml
│
└── test/
    └── java/
        └── br/com/paytrack/testeTecnico/
            └── cartao/
                └── service/
                    └── CartaoServiceTest.java
```
---

## Decisões técnicas

### Armazenamento seguro do número do cartão

O número do cartão não é armazenado em texto puro no banco de dados.

Por se tratar de uma informação sensível, foi adotada a criptografia antes da persistência, reduzindo a exposição do valor original no banco de dados.

O número é criptografado antes de ser persistido e descriptografado somente nos fluxos em que o dado original precisa ser recuperado.

Além da criptografia, é gerado um hash do número original para permitir a identificação de cartões duplicados sem a necessidade de descriptografar os registros existentes.

### Utilização de hash para controle de duplicidade

Foi criado um campo específico para armazenar o hash do número do cartão.

Como o número é armazenado de forma criptografada, a utilização de um hash determinístico permite realizar a verificação de duplicidade sem utilizar o dado sensível diretamente.

Durante o cadastro, o backend gera o hash do número informado e consulta o repositório para verificar se já existe um registro correspondente.

Caso exista, o cadastro é rejeitado.

### Consulta de CEP pelo backend

A integração com o ViaCEP é realizada pelo backend, em vez de o frontend consumir diretamente o serviço externo.

Essa abordagem centraliza a comunicação com serviços externos no backend e reduz o acoplamento do frontend com a API do ViaCEP.

O frontend envia o CEP para a API da aplicação. O backend é responsável por consultar a fonte de dados correspondente, tratar a resposta e retornar somente as informações necessárias para apresentação.

### Utilização de mock na consulta de CEP

Durante o desenvolvimento foi identificado um bloqueio relacionado à comunicação HTTPS com o ViaCEP na rede corporativa utilizada.

Para evitar que essa limitação de infraestrutura impedisse o desenvolvimento e a demonstração da funcionalidade, foi criado um modo mockado no backend.

A decisão de manter o mock no backend permite que o frontend continue consumindo exatamente o mesmo endpoint e o mesmo contrato de resposta, independentemente da origem dos dados.

O fluxo permanece:

```text
Frontend
   ↓
Backend
   ↓
Mock ou ViaCEP
   ↓
Resposta padronizada
```

Dessa forma, a integração real pode ser habilitada apenas por configuração, sem necessidade de alterações no frontend.

### Inativação automática de cartões vencidos

Foi criada uma rotina agendada para inativar cartões cuja data de validade tenha sido ultrapassada.

Essa abordagem foi adotada porque o status de um cartão não deve depender somente do estado definido no momento do cadastro.

Um cartão inicialmente válido poderá se tornar vencido posteriormente.

O scheduler consulta os cartões que permanecem ativos mesmo após o vencimento e atualiza automaticamente o status desses registros para inativo.

### Utilização de DTOs

As entidades responsáveis pela persistência não são expostas diretamente pelos endpoints da API.

A utilização de DTOs permite separar o modelo de persistência do contrato disponibilizado pela API, além de controlar quais informações são recebidas e retornadas em cada operação.

Foram definidos DTOs específicos para entrada de dados, listagem, consulta detalhada de cartões e retorno das consultas de endereço.

### Testes unitários com JUnit e Mockito

Os testes das regras de negócio do serviço de cartões foram implementados utilizando JUnit e Mockito.

O Mockito é utilizado para simular as dependências do `CartaoService`, como repositório, serviço de criptografia e serviço de geração de hash.

Essa abordagem permite validar as regras de negócio de forma isolada, sem depender de uma conexão com o banco de dados ou da inicialização completa do contexto do Spring.

Os testes contemplam principalmente as regras obrigatórias relacionadas à validade do cartão e à validação do CVV, além de outros comportamentos do serviço.

---

## Executando os testes

Para executar os testes automatizados do projeto:

```bash
mvn test
```

Utilizando o Maven Wrapper no Windows:

```bash
mvnw.cmd test
```

Para executar somente os testes do serviço de cartões:

```bash
mvnw.cmd -Dtest=CartaoServiceTest test
```

Um resultado bem-sucedido deverá apresentar:

```text
Failures: 0
Errors: 0
```
