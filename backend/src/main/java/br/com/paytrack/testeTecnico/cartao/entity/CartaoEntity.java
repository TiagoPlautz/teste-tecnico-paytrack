package br.com.paytrack.testeTecnico.cartao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "cartoes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false, unique = true)
    private String numeroCartaoHash;

    private String descricao;
    private String identificador;
    private OffsetDateTime dataValidade;
    private String numeroCartao;
    private String bandeira;
    private String cvv;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @PrePersist
    public void criaDatas() {
        OffsetDateTime dataAtual = OffsetDateTime.now();

        this.createdAt = dataAtual;
        this.updatedAt = dataAtual;
    }

    @PreUpdate
    public void dataAtualizacao() {
        this.updatedAt = OffsetDateTime.now();
    }

}
