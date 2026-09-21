# Teste Técnico Paytrack - Frontend

## Sobre o projeto

O frontend do Teste Técnico Paytrack foi desenvolvido para disponibilizar uma interface simples e objetiva para interação com as funcionalidades fornecidas pelo backend.

A aplicação permite:

* Visualizar os cartões cadastrados;
* Cadastrar novos cartões corporativos;
* Identificar a bandeira do cartão informado;
* Aplicar máscara ao número do cartão;
* Visualizar somente os dados necessários dos cartões na listagem;
* Consultar endereços a partir de um CEP;
* Exibir os dados do endereço retornados pela API;
* Apresentar mensagens de sucesso e erro durante as operações.

O frontend foi desenvolvido utilizando React com TypeScript e consome exclusivamente os endpoints disponibilizados pelo backend da aplicação.

---

## Tecnologias utilizadas

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

## Pré-requisitos

Para executar o frontend, é necessário possuir:

* Node.js
* npm
* Backend da aplicação em execução

Por padrão, o frontend espera que a API backend esteja disponível em:

```text
http://localhost:8080
```

---

## Instalação

Após clonar o projeto, acesse a pasta do frontend e instale as dependências:

```bash
npm install
```

---

## Executando a aplicação

Para iniciar o ambiente de desenvolvimento:

```bash
npm run dev
```

O Vite disponibilizará a aplicação localmente e exibirá no terminal o endereço utilizado para acesso.

Normalmente:

```text
http://localhost:5173
```

> Para utilizar todas as funcionalidades, certifique-se de que o backend esteja em execução.

---

## Funcionalidades

### Listagem de cartões

A tela principal apresenta os cartões cadastrados por meio dos dados retornados pelo backend.

A listagem utiliza somente as informações necessárias para apresentação, evitando utilizar os dados completos do cartão.

O número apresentado na listagem já é retornado pelo backend de forma mascarada, exibindo somente os quatro últimos dígitos.

Exemplo:

```text
**** **** **** 1234
```

Após o cadastro de um novo cartão, a listagem é atualizada para apresentar o novo registro.

### Cadastro de cartão

O cadastro é realizado por meio de um modal específico.

O formulário permite informar os dados necessários para criação do cartão e realiza tratamentos no frontend antes do envio da requisição ao backend.

Após o preenchimento, os dados são enviados para:

```text
POST /cartoes
```

As regras de negócio relacionadas ao cadastro são validadas pelo backend, mantendo a API como responsável pela validação definitiva das informações.

### Formatação do número do cartão

Durante a digitação, o número do cartão recebe uma máscara para melhorar sua legibilidade.

Exemplo:

```text
4111 1111 1111 1111
```

Antes do envio para o backend, os caracteres utilizados apenas para apresentação são removidos e somente o valor numérico é enviado na requisição.

### Identificação da bandeira

A aplicação utiliza a biblioteca `card-validator` para auxiliar na identificação da bandeira a partir do número informado pelo usuário.

Também é utilizada a biblioteca `react-svg-credit-card-payment-icons` para representação visual das bandeiras suportadas pela interface.

Essa abordagem evita a necessidade de manter manualmente no frontend todas as regras de identificação das diferentes bandeiras de cartão.

### Validação do CVV

O campo de CVV aceita somente caracteres numéricos e limita a quantidade de caracteres permitidos durante o preenchimento.

As regras de negócio referentes ao valor do CVV são validadas pelo backend.

Dessa forma, o frontend é responsável pelas restrições de entrada e experiência do usuário, enquanto o backend permanece responsável pela validação definitiva da regra de negócio.

### Data de validade

A data de validade é preenchida utilizando um campo de data.

Antes do envio da requisição, o valor é convertido para o formato esperado pelo backend.

A validação definitiva referente ao vencimento do cartão é realizada pela API.

### Mensagens de sucesso e erro

A aplicação utiliza `react-hot-toast` para apresentar feedback das operações realizadas.

São exibidas notificações para situações como:

* Cadastro realizado com sucesso;
* Erros durante o cadastro;
* Consulta de CEP realizada com sucesso;
* Erros durante a consulta de CEP.

As mensagens de erro retornadas pelo backend são utilizadas sempre que disponíveis, permitindo apresentar ao usuário informações relacionadas à regra que impediu a operação.

---

## Consulta de endereço por CEP

A consulta de endereço é realizada por meio de um modal específico.

O usuário informa o CEP e o frontend realiza uma requisição para o endpoint disponibilizado pelo backend:

```text
GET /enderecos/{cep}
```

Antes da requisição, caracteres utilizados apenas para formatação são removidos do CEP.

Quando a consulta é realizada com sucesso, os dados do endereço são apresentados no próprio modal, incluindo:

* CEP;
* Logradouro;
* Bairro;
* Cidade;
* UF.

Caso uma nova consulta não encontre o CEP informado ou ocorra algum erro, o endereço anteriormente apresentado é removido do modal para evitar a exibição de informações correspondentes a uma consulta anterior.

### Origem dos dados de CEP

O frontend não realiza comunicação direta com o ViaCEP.

Toda consulta é encaminhada para o backend da aplicação:

```text
Frontend
   ↓
GET /enderecos/{cep}
   ↓
Backend
   ↓
Fonte de dados do endereço
   ↓
Resposta
   ↓
Frontend
```

Dessa forma, o frontend não precisa conhecer se o backend está utilizando a integração real com o ViaCEP ou o modo mockado implementado para ambientes com restrições de acesso ao serviço externo.

---

## Integração com o backend

A comunicação com o backend foi centralizada na camada de serviços do frontend.

As operações relacionadas aos cartões são realizadas por meio de:

```text
services/cartaoService.ts
```

As operações relacionadas à consulta de endereço são realizadas por meio de:

```text
services/enderecoService.ts
```

Essa separação evita que os componentes sejam responsáveis diretamente pela implementação das requisições HTTP e facilita a manutenção do código.

---

## Estrutura do projeto

A aplicação foi organizada separando componentes visuais, páginas, serviços de comunicação e definições de tipos.

```text
src/
├── components/
│   ├── ListaCartoes.tsx
│   ├── ModalAdicionarCartao.tsx
│   └── ModalConsultarEndereco.tsx
│
├── pages/
│   └── Cartoes.tsx
│
├── services/
│   ├── cartaoService.ts
│   └── enderecoService.ts
│
├── types/
│   ├── Cartao.ts
│   └── Cep.ts
│
├── App.tsx
├── main.tsx
└── index.css
```

### `components`

Contém os componentes reutilizáveis responsáveis pela apresentação e interação com partes específicas da interface, como listagem de cartões e modais.

### `pages`

Contém a composição da página principal da aplicação.

### `services`

Centraliza a comunicação HTTP com os endpoints disponibilizados pelo backend.

### `types`

Contém as definições TypeScript utilizadas para representar cartões, endereços e objetos enviados ou recebidos pela API.

---

## Decisões técnicas

### React com TypeScript

O TypeScript foi utilizado para adicionar tipagem estática ao frontend.

Os contratos dos objetos utilizados na comunicação com o backend são representados por tipos específicos, facilitando a identificação de inconsistências durante o desenvolvimento e melhorando a manutenção do código.

### Separação da comunicação em services

As requisições HTTP foram separadas dos componentes visuais e centralizadas em arquivos de serviço.

Essa abordagem mantém os componentes focados principalmente na apresentação e interação com o usuário, enquanto os serviços concentram a comunicação com a API.

### Separação dos tipos

Os modelos utilizados pelo frontend foram definidos em arquivos específicos dentro de `types`.

Essa separação permite reutilizar os contratos em diferentes componentes e serviços sem duplicar suas definições.

### Componentização dos modais

O cadastro de cartão e a consulta de CEP foram implementados em componentes separados.

Essa abordagem evita concentrar toda a lógica da aplicação na página principal e mantém responsabilidades distintas para cada funcionalidade.

### Validação no frontend e backend

O frontend realiza validações relacionadas principalmente ao formato e à experiência de preenchimento dos campos.

As regras de negócio definitivas permanecem no backend.

Essa separação evita depender exclusivamente das validações executadas no navegador e mantém as regras centralizadas na API.

### Máscaras apenas para apresentação

As máscaras aplicadas ao número do cartão e ao CEP têm finalidade exclusivamente visual.

Antes das requisições, os caracteres de formatação são removidos, garantindo que o backend receba os valores no formato esperado.

### Consulta de CEP desacoplada do serviço externo

O frontend não possui dependência direta do ViaCEP.

A interface conhece somente o endpoint disponibilizado pelo backend, permitindo que a origem dos dados seja alterada sem necessidade de modificar o fluxo do frontend.

Essa decisão também permitiu utilizar o modo mockado implementado no backend sem realizar qualquer alteração na interface.

### Feedback visual das operações

Foi utilizado `react-hot-toast` para apresentar mensagens de sucesso e erro sem interromper a navegação do usuário.

Essa abordagem fornece feedback imediato após operações como cadastro de cartão e consulta de endereço.

---

## Scripts disponíveis

| Comando           | Descrição                                       |
| ----------------- | ----------------------------------------------- |
| `npm run dev`     | Inicia a aplicação em modo de desenvolvimento   |
| `npm run build`   | Compila o TypeScript e gera o build de produção |
| `npm run lint`    | Executa a análise estática com Oxlint           |
| `npm run preview` | Executa localmente o build gerado               |
