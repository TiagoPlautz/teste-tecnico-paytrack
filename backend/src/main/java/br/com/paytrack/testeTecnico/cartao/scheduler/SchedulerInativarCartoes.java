package br.com.paytrack.testeTecnico.cartao.scheduler;

import br.com.paytrack.testeTecnico.cartao.service.CartaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerInativarCartoes {

    private final CartaoService cartaoService;

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    public void inativarCartoesVencidos() {
        cartaoService.inativarCartoesVencidos();
    }
}
