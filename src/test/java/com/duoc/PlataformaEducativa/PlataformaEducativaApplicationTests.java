package com.duoc.PlataformaEducativa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=claveSecretaParaTestingDuoc2024xK9m",
    "jwt.expiration=86400000"
})
class PlataformaEducativaApplicationTests {

    @Test
    void contextLoads() {
    }
}
