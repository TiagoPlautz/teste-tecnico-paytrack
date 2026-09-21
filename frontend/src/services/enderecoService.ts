import type { ObjetoCep } from "../types/Cep"

const API_URL_CEP = "http://localhost:8080/endereco";

export async function consultaEndereco(cep: ObjetoCep) {

    const response = await fetch(`${API_URL_CEP}/${cep}`);

    if (!response.ok) {
        const erro = await response.json();
        throw new Error(erro.message || "Erro ao consultar o cep informado");
    }

    return response.json();

}

export default consultaEndereco;