package br.com.paytrack.testeTecnico.cartao.service;

import br.com.paytrack.testeTecnico.cartao.dto.CartaoRequestDTO;
import br.com.paytrack.testeTecnico.cartao.dto.CartaoResponseDTO;
import br.com.paytrack.testeTecnico.cartao.dto.DetalhesCartaoResponseDTO;
import br.com.paytrack.testeTecnico.cartao.dto.ListaCartoesResponseDTO;
import br.com.paytrack.testeTecnico.cartao.entity.CartaoEntity;
import br.com.paytrack.testeTecnico.cartao.hash.HashService;
import br.com.paytrack.testeTecnico.cartao.repository.ICartaoRepository;
import br.com.paytrack.testeTecnico.cartao.crypto.CryptoService;
import br.com.paytrack.testeTecnico.exception.BadRequestException;
import br.com.paytrack.testeTecnico.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {

    @Mock
    private ICartaoRepository cartaoRepository;

    @Mock
    private CryptoService cryptoService;

    @Mock
    private HashService hashService;

    @InjectMocks
    private CartaoService cartaoService;

    @Test
    void consultaCartaoPorId() {
        Long id = 1L;

        CartaoEntity cartao = CartaoEntity.builder()
                .id(id)
                .ativo(true)
                .identificador("Cartão pessoal")
                .numeroCartao("numeroCriptografado")
                .cvv("cvvCriptografado")
                .bandeira("VISA")
                .build();

        when(cartaoRepository.findById(id))
                .thenReturn(Optional.of(cartao));

        when(cryptoService.descriptografar("numeroCriptografado"))
                .thenReturn("1234567890123456");

        when(cryptoService.descriptografar("cvvCriptografado"))
                .thenReturn("358");

        DetalhesCartaoResponseDTO resultado =
                cartaoService.consultaCartaoPorId(id);

        assertEquals(id, resultado.getId());
        assertTrue(resultado.isAtivo());
        assertEquals("1234567890123456", resultado.getNumeroCartao());
        assertEquals("358", resultado.getCvv());
    }

    @Test
    void deveLancarNotFoundExceptionQuandoCartaoNaoExistir() {

        Long id = 999L;

        when(cartaoRepository.findById(id))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> cartaoService.consultaCartaoPorId(id)
        );

        assertEquals(
                "Cartão não encontrado para o id" + id,
                exception.getMessage()
        );

        verify(cartaoRepository).findById(id);
    }

    @Test
    void naoDeveCadastrarCartaoVencido() {

        CartaoRequestDTO request = CartaoRequestDTO.builder()
                .numeroCartao("4532756279624064")
                .cvv("358")
                .dataValidade(OffsetDateTime.now().minusDays(1))
                .build();

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> cartaoService.cadastrarCartao(request)
        );

        assertTrue(
                exception.getMessage().contains("Cartão vencido")
        );

        verify(cartaoRepository, never())
                .save(any(CartaoEntity.class));
    }

    @Test
    void naoDeveCadastrarCartaoComCvvImpar() {

        CartaoRequestDTO request = CartaoRequestDTO.builder()
                .numeroCartao("4532756279624064")
                .cvv("437")
                .dataValidade(OffsetDateTime.now().plusYears(1))
                .build();

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> cartaoService.cadastrarCartao(request)
        );

        assertEquals(
                "CVV inválido: CVV com numero impar não é permitido cadastrar",
                exception.getMessage()
        );

        verify(cartaoRepository, never())
                .save(any(CartaoEntity.class));
    }

    @Test
    void deveCadastrarCartaoValido() {

        CartaoRequestDTO request = CartaoRequestDTO.builder()
                .numeroCartao("4111111111111111")
                .cvv("358")
                .dataValidade(OffsetDateTime.now().plusYears(1))
                .descricao("Cartão corporativo")
                .identificador("CORPORATIVO")
                .bandeira("Visa")
                .build();

        when(hashService.gerarHash(anyString()))
                .thenReturn("hashCartao");

        when(cartaoRepository.existsByNumeroCartaoHash("hashCartao"))
                .thenReturn(false);

        when(cryptoService.criptografar("4111111111111111"))
                .thenReturn("numeroCriptografado");

        when(cryptoService.criptografar("358"))
                .thenReturn("cvvCriptografado");

        CartaoEntity salvo = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("numeroCriptografado")
                .cvv("cvvCriptografado")
                .ativo(true)
                .build();

        when(cartaoRepository.save(any(CartaoEntity.class)))
                .thenReturn(salvo);

        when(cryptoService.descriptografar("numeroCriptografado"))
                .thenReturn("4111111111111111");

        CartaoResponseDTO resultado =
                cartaoService.cadastrarCartao(request);

        assertNotNull(resultado);

        verify(cartaoRepository)
                .save(any(CartaoEntity.class));
    }
}