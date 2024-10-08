package com.payment.service.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class RSAPublicKey {

    @Value("${app.security.jwt.public_key}")
    private String publicKeyPath;
    @Bean(name = "rsaPublicKey")
    public java.security.interfaces.RSAPublicKey getRsaPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        String publicKeyContent = publicKeyPath;
        KeyFactory kf = KeyFactory.getInstance("RSA");

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        java.security.interfaces.RSAPublicKey rsaPublicKey = (java.security.interfaces.RSAPublicKey) kf.generatePublic(keySpecX509);

        return rsaPublicKey;

    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
