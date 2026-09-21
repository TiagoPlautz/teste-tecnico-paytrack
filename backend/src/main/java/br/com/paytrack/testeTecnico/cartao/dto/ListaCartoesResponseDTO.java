package br.com.paytrack.testeTecnico.cartao.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ListaCartoesResponseDTO {

    private Long id;
    private String descricao;
    private String identificador;
    private String numeroCartao;
    private OffsetDateTime dataValidade;
    private String bandeira;
    private boolean ativo;

}
