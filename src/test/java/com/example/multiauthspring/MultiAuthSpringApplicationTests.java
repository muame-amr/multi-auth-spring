package com.example.multiauthspring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MultiAuthSpringApplication.class)
class MultiAuthSpringApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void givenMemUsers_whenGetPingWithValidUser_thenOk() {
		ResponseEntity<String> result = makeRestCallToGetPing("memuser", "pass");

		Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(200);
		Assertions.assertThat(result.getBody()).isEqualTo("OK");
	}

	@Test
	public void givenExternalUsers_whenGetPingWithValidUser_thenOK() {
		ResponseEntity<String> result = makeRestCallToGetPing("externalusr", "P4$$w0rd");

		Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(200);
		Assertions.assertThat(result.getBody()).isEqualTo("OK");
	}

	@Test
	public void givenAuthProviders_whenGetPingWithNoCred_then401() {
		ResponseEntity<String> result = makeRestCallToGetPing();

		Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(401);
	}

	@Test
	public void givenAuthProviders_whenGetPingWithBadCred_then401() {
		ResponseEntity<String> result = makeRestCallToGetPing("user", "bad_password");

		Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(401);
	}

	private ResponseEntity<String> makeRestCallToGetPing(String username, String password) {
		return restTemplate.withBasicAuth(username, password)
				.getForEntity("/api/ping", String.class, Collections.emptyMap());
	}

	private ResponseEntity<String> makeRestCallToGetPing() {
		return restTemplate.getForEntity("/api/ping", String.class, Collections.emptyMap());
	}
}
