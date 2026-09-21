package br.com.paytrack.testeTecnico.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ErrorResponse {

    private String message;
    private Integer status;

}
