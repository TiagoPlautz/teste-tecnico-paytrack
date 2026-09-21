package br.com.paytrack.testeTecnico.cartao.controller;

import br.com.paytrack.testeTecnico.cartao.dto.CartaoRequestDTO;
import br.com.paytrack.testeTecnico.cartao.dto.DetalhesCartaoResponseDTO;
import br.com.paytrack.testeTecnico.cartao.dto.ListaCartoesResponseDTO;
import br.com.paytrack.testeTecnico.cartao.service.CartaoService;
import br.com.paytrack.testeTecnico.exception.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
@Validated
public class CartaoController {

    private final CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<DetalhesCartaoResponseDTO> cadastrarCartao(@Valid @RequestBody CartaoRequestDTO cartaoRequestDTO) throws BadRequestException {
        DetalhesCartaoResponseDTO response = cartaoService.cadastrarCartao(cartaoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ListaCartoesResponseDTO>> listarTodosCartoes() {
        return ResponseEntity.ok(cartaoService.listarTodosCartoes());
    }

    @GetMapping("/{id}/lerCartao")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DetalhesCartaoResponseDTO> consultaCartaoPorId(@PathVariable Long id) {
        DetalhesCartaoResponseDTO detalhesCartao = cartaoService.consultaCartaoPorId(id);
        return ResponseEntity.ok().body(detalhesCartao);
    }

}
