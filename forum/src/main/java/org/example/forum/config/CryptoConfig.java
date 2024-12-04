package org.example.forum.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;


@Configuration
public class CryptoConfig {

    @Value("${mySecretKey}")
    private String SECRET_KEY;

    @Bean
    public JWK getJWK(){
        // Créer une clé secrète avec SecretKeySpec
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), "HMACSHA256");
        // Créer un objet OctetSequenceKey pour la clé secrète
        JWK jwk = new OctetSequenceKey.Builder(key.getEncoded())
                .algorithm(JWSAlgorithm.HS256)
                .build();
        return jwk;
    }
}
