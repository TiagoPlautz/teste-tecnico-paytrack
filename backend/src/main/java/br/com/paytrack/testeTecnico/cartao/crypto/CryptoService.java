package br.com.paytrack.testeTecnico.cartao.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class CryptoService {

    private static final String ALGORITHM = "AES";
    private final SecretKeySpec secretKey;

    public CryptoService(
            @Value("${app.crypto.key}") String chave
    ) {
        byte[] chaveBytes = Base64.getDecoder().decode(chave);

        this.secretKey = new SecretKeySpec(
                chaveBytes,
                "AES"
        );
    }

    public String criptografar(String valor) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] valorCriptografado =
                    cipher.doFinal(valor.getBytes());

            return Base64.getEncoder()
                    .encodeToString(valorCriptografado);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar dado", e);
        }
    }

    public String descriptografar(String valor) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] valorDecodificado =
                    Base64.getDecoder().decode(valor);

            return new String(
                    cipher.doFinal(valorDecodificado)
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar dado", e);
        }
    }
}
