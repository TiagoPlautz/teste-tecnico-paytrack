package br.com.paytrack.testeTecnico.cartao.service;

import br.com.paytrack.testeTecnico.cartao.dto.CartaoRequestDTO;
import br.com.paytrack.testeTecnico.cartao.dto.DetalhesCartaoResponseDTO;
import br.com.paytrack.testeTecnico.cartao.dto.ListaCartoesResponseDTO;
import br.com.paytrack.testeTecnico.cartao.entity.CartaoEntity;
import br.com.paytrack.testeTecnico.cartao.hash.HashService;
import br.com.paytrack.testeTecnico.cartao.repository.ICartaoRepository;
import br.com.paytrack.testeTecnico.cartao.crypto.CryptoService;
import br.com.paytrack.testeTecnico.exception.BadRequestException;
import br.com.paytrack.testeTecnico.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final ICartaoRepository cartaoRepository;
    private final CryptoService cryptoService;
    private final HashService hashService;

    public DetalhesCartaoResponseDTO cadastrarCartao(CartaoRequestDTO cartaoRequestDTO) throws BadRequestException {

        String numeroOriginalCartao = cartaoRequestDTO.getNumeroCartao();
        String numeroCartaoHash = hashService.gerarHash(numeroOriginalCartao);

        if (cartaoRepository.existsByNumeroCartaoHash(numeroCartaoHash)) {
            throw new BadRequestException("O número de cartão informado já está cadastrado!");
        }

        int valorCvv = Integer.parseInt(cartaoRequestDTO.getCvv());

        if (cartaoRequestDTO.getDataValidade() == null || !cartaoRequestDTO.getDataValidade().isAfter(OffsetDateTime.now())) {
            throw new BadRequestException("Cartão vencido: a data de validade informada (" + cartaoRequestDTO.getDataValidade() + ") já passou");
        }

        if (valorCvv % 2 == 0) {
            throw new BadRequestException("CVV inválido: CVV com numero par não é permitido cadastrar");
        }

        String numeroCartaoCriptografado = cryptoService.criptografar(numeroOriginalCartao);
        String cvvCriptografado = cryptoService.criptografar(cartaoRequestDTO.getCvv());

        CartaoEntity cartaoSalvo = cartaoRepository.save(CartaoEntity.builder()
                .descricao(cartaoRequestDTO.getDescricao())
                .identificador(cartaoRequestDTO.getIdentificador())
                .numeroCartao(numeroCartaoCriptografado)
                .numeroCartaoHash(numeroCartaoHash)
                .dataValidade(cartaoRequestDTO.getDataValidade())
                .bandeira(cartaoRequestDTO.getBandeira())
                .cvv(cvvCriptografado)
                .ativo(definirStatusCartao(cartaoRequestDTO.getDataValidade()))
                .build());

        return detalhesCartaoResponseDTO(cartaoSalvo);
    }

    private ListaCartoesResponseDTO responseDTO(CartaoEntity cartaoEntity) {

        String numeroCartaoSemCriptografia = cryptoService.descriptografar(cartaoEntity.getNumeroCartao());

        return ListaCartoesResponseDTO.builder()
                .id(cartaoEntity.getId())
                .identificador(cartaoEntity.getIdentificador())
                .numeroCartao(mascararNumeroCartao(numeroCartaoSemCriptografia))
                .bandeira(cartaoEntity.getBandeira())
                .descricao(cartaoEntity.getDescricao())
                .dataValidade(cartaoEntity.getDataValidade())
                .ativo(cartaoEntity.isAtivo())
                .build();
    }

    private DetalhesCartaoResponseDTO detalhesCartaoResponseDTO(CartaoEntity cartaoEntity) {
        String numeroCartaoSemCriptografia = cryptoService.descriptografar(cartaoEntity.getNumeroCartao());
        String cvvSemCriptografia = cryptoService.descriptografar(cartaoEntity.getCvv());

        return DetalhesCartaoResponseDTO.builder()
                .id(cartaoEntity.getId())
                .identificador(cartaoEntity.getIdentificador())
                .numeroCartao(numeroCartaoSemCriptografia)
                .bandeira(cartaoEntity.getBandeira())
                .descricao(cartaoEntity.getDescricao())
                .dataValidade(cartaoEntity.getDataValidade())
                .cvv(cvvSemCriptografia)
                .ativo(cartaoEntity.isAtivo())
                .createdAt(cartaoEntity.getCreatedAt())
                .build();
    }

    public List<ListaCartoesResponseDTO> listarTodosCartoes() {
        return cartaoRepository.findAll()
                .stream()
                .map(this::responseDTO)
                .toList();
    }

    public DetalhesCartaoResponseDTO consultaCartaoPorId(Long id) {
        CartaoEntity cartaoEntity = cartaoRepository.findById(id).orElseThrow(() -> new NotFoundException("Cartão não encontrado para o id" + id));
        return detalhesCartaoResponseDTO(cartaoEntity);
    }

    private String mascararNumeroCartao(String numeroCartao) {

        if (numeroCartao == null || numeroCartao.length() < 4) {
            return numeroCartao;
        }

        String ultimosQuatroDigitos = numeroCartao.substring(numeroCartao.length() - 4);

        return "**** **** **** " + ultimosQuatroDigitos;
    }

    private boolean definirStatusCartao(OffsetDateTime dataValidade) {
        if (dataValidade.isBefore(OffsetDateTime.now())) {
            return false;
        }

        return true;
    }

    @Transactional
    public void inativarCartoesVencidos() {
         OffsetDateTime dataAtual = OffsetDateTime.now();

         List<CartaoEntity> cartoesVencidos = cartaoRepository.findByAtivoTrueAndDataValidadeBefore(dataAtual);

         cartoesVencidos.forEach(cartao -> cartao.setAtivo(false));

         cartaoRepository.saveAll(cartoesVencidos);
    }

}
