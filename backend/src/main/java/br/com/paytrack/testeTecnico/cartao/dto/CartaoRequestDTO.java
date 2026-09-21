package br.com.paytrack.testeTecnico.cartao.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CartaoRequestDTO {

    @NotBlank(message = "Descrição do cartão é obrigatório")
    private String descricao;

    @NotBlank(message = "O identificador do cartão é obrigatório")
    private String identificador;

    @NotBlank(message = "Número do cartão é obrigatório")
    private String numeroCartao;

    @NotNull(message = "Data de validade é obrigatório")
    @Future(message = "Cartão vencido: a data de validade é menor que a data atual")
    private OffsetDateTime dataValidade;

    private String bandeira;

    @NotNull
    private String cvv;

}
