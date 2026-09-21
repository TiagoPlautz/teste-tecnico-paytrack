package br.com.paytrack.testeTecnico.endereco.controller;

import br.com.paytrack.testeTecnico.endereco.dto.EnderecoResponseDTO;
import br.com.paytrack.testeTecnico.endereco.service.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/endereco")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @GetMapping("/{cep}")
    public ResponseEntity<EnderecoResponseDTO> consultarEndereco(@PathVariable String cep) {
        EnderecoResponseDTO response = enderecoService.consultarEndereco(cep);

        return ResponseEntity.ok(response);
    }
}
