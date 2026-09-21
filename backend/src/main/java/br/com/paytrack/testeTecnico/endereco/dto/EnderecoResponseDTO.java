package br.com.paytrack.testeTecnico.endereco.dto;

public record EnderecoResponseDTO(
        String cep,
        String logradouro,
        String bairro,
        String cidade,
        String uf
) {
}