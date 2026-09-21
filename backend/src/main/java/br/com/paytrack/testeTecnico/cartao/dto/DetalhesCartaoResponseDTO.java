package br.com.paytrack.testeTecnico.cartao.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DetalhesCartaoResponseDTO {

    private Long id;
    private String descricao;
    private String identificador;
    private OffsetDateTime dataValidade;
    private String numeroCartao;
    private String bandeira;
    private String cvv;
    private boolean ativo;
    private OffsetDateTime createdAt;

}
