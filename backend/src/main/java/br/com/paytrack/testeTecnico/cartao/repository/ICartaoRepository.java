package br.com.paytrack.testeTecnico.cartao.repository;

import br.com.paytrack.testeTecnico.cartao.entity.CartaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICartaoRepository extends JpaRepository<CartaoEntity, Long> {

    boolean existsByNumeroCartaoHash(String numeroCartao);

    List<CartaoEntity> findByAtivoTrueAndDataValidadeBefore(OffsetDateTime data);

}
