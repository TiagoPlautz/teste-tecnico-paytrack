import type { Cartao, DetalhesCartao, CadastrarCartao } from "../types/Cartao";

const API_URL = "http://localhost:8080/cartoes";

export async function listarCartoes(): Promise<Cartao[]> {
    const response = await fetch(API_URL);

    if (!response.ok) {
        throw new Error("Erro ao buscar cartões");
    }

    return response.json();
}

export async function buscarCartaoPorId(id: number): Promise<DetalhesCartao> {
    const response = await fetch(`${API_URL}/${id}/lerCartao`);

    if (!response.ok) {
        throw new Error("Erro ao consultar cartão");
    }

    return response.json();
}

export async function cadastrarCartao(cartao: CadastrarCartao) {

    const response = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(cartao)
    });

    if (!response.ok) {
        const erro = await response.json();
        throw new Error(erro.message || "Erro ao cadastrar cartão");
    }

    return response.json();

}

export default listarCartoes;