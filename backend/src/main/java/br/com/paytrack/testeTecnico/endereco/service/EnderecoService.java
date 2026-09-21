package br.com.paytrack.testeTecnico.endereco.service;
import br.com.paytrack.testeTecnico.endereco.dto.EnderecoResponseDTO;
import br.com.paytrack.testeTecnico.endereco.dto.ViaCepResponseDTO;
import br.com.paytrack.testeTecnico.exception.BadRequestException;
import br.com.paytrack.testeTecnico.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class EnderecoService {

    private final RestClient restClient;

    @Value("${app.endereco.mock}")
    private boolean mockHabilitado;

    public EnderecoService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://viacep.com.br")
                .build();
    }

    public EnderecoResponseDTO consultarEndereco(String cep) {

        System.out.println("Mock habilitado: " + mockHabilitado);

        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("TrustStore: " + System.getProperty("javax.net.ssl.trustStore"));

        String cepFormatado = cep.replaceAll("\\D", "");

        if (!cepFormatado.matches("\\d{8}")) {
            throw new BadRequestException(
                    "CEP inválido: informe 8 dígitos"
            );
        }

        if (mockHabilitado) {
            return consultarEnderecoMock(cepFormatado);
        }

        return consultarViaCep(cepFormatado);
    }

    private EnderecoResponseDTO consultarEnderecoMock(String cep) {

        if ("89080314".equals(cep)) {
            return new EnderecoResponseDTO(
                    "89080-314",
                    "Rua João Pessoa",
                    "Tapajós",
                    "Indaial",
                    "SC"
            );
        }

        if ("01001000".equals(cep)) {
            return new EnderecoResponseDTO(
                    "01001-000",
                    "Praça da Sé",
                    "Sé",
                    "São Paulo",
                    "SP"
            );
        }

        throw new NotFoundException("CEP não encontrado no ambiente mock");
    }

    private EnderecoResponseDTO consultarViaCep(String cep) {

        ViaCepResponseDTO responseCep = restClient
                .get()
                .uri("/ws/{cep}/json/", cep)
                .retrieve()
                .body(ViaCepResponseDTO.class);

        if (responseCep == null) {
            throw new NotFoundException(
                    "Não foi possível consultar o CEP"
            );
        }

        if (Boolean.TRUE.equals(responseCep.erro())) {
            throw new NotFoundException("CEP não encontrado");
        }

        return new EnderecoResponseDTO(
                responseCep.cep(),
                responseCep.logradouro(),
                responseCep.bairro(),
                responseCep.localidade(),
                responseCep.uf()
        );
    }
}