export type Cartao = {
    id: number;
    descricao: string;
    identificador: string;
    dataValidade: string;
    numeroCartao: string;
    bandeira: string;
    ativo: boolean;
};

// export type DetalhesCartao = {
//     id: number;
//     descricao: string;
//     identificador: string;
//     dataValidade: string;
//     numeroCartao: string;
//     bandeira: string;
//     cvv: string;
//     ativo: boolean;
//     createdAt: string;
// };

export type CadastrarCartao = {
    descricao: string;
    identificador: string;
    numeroCartao: string;
    dataValidade: string;
    bandeira: string;
    cvv: string;
};