package br.com.paytrack.testeTecnico.cartao.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "\\d{16}", message = "O número do cartão deve conter exatamente 16 números")
    private String numeroCartao;

    @NotNull(message = "Data de validade é obrigatório")
    @Future(message = "Cartão vencido: a data de validade é menor que a data atual")
    private OffsetDateTime dataValidade;

    private String bandeira;

    @NotBlank(message = "O campo CVV é obrigatório")
    @Pattern(regexp = "\\d{3,4}", message = "O campo CVV deve conter somente números e entre 3 a 4 caracteres")
    private String cvv;

}
