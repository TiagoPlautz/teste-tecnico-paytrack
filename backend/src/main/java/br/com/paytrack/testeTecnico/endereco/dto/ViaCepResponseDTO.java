package br.com.paytrack.testeTecnico.endereco.dto;

public record ViaCepResponseDTO(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        String estado,
        String regiao,
        String ibge,
        String ddd,
        Boolean erro
) {
}