package com.duoc.PlataformaEducativa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://cloud002g07.b2clogin.com/fa5fcb94-72ba-4592-aa1a-1631856639c8/v2.0/",
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://cloud002g07.b2clogin.com/cloud002g07.onmicrosoft.com/b2c_1_cloud002a/discovery/v2.0/keys",
    "aws.accessKey=test",
    "aws.secretKey=test",
    "aws.sessionToken=test",
    "aws.region=us-east-1",
    "aws.s3.bucketName=test-bucket"
})
class PlataformaEducativaApplicationTests {

    @Test
    void contextLoads() {
    }
}
