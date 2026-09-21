package br.com.paytrack.testeTecnico.cartao.hash;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class HashService {

    private static final String ALGORITHM = "HmacSHA256";

    private final SecretKeySpec secretKey;

    public HashService(@Value("${app.hash.key}") String chave) {
        byte[] chaveBytes = Base64.getDecoder().decode(chave);

        this.secretKey = new SecretKeySpec(
                chaveBytes,
                ALGORITHM
        );
    }

    public String gerarHash(String valor) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKey);

            byte[] hash = mac.doFinal(
                    valor.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Erro ao gerar hash do número do cartão",
                    e
            );
        }
    }
}