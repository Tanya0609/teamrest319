//package com.example.demo;
//
//import modules.Portfolio;
//import net.minidev.json.JSONObject;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.BDDMockito;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//public class DemoApplicationTests {
//
//	@Test
//	public void contextLoads() {
//	}
//
//	@Autowired
//	private TestRestTemplate restTemplate;
//
//	@Test
//	public void homeResponse() {
//		String body = this.restTemplate.getForObject("/", String.class);
//		assertThat(body).isEqualTo("Spring is here!");
//	}
//
//	@Test
//	public void GETPortfolioResponse() {
//		String body = this.restTemplate.getForObject("/portfolio/1", String.class);
//		assertThat(body).isEqualTo("{\"id\":\"1\",\"deviation\":\"5\",\"type\":\"fund\",\"allocations\":{\"25\":70,\"70\":30}}");
//	}
//
//	@Test
//	public void POSTportofolioResponse() {
//		Map<String, Integer> allocations = new HashMap<>();
//		allocations.put("70", 30);
//		allocations.put("25", 70);
//		Map<String,String> portfolio = new HashMap<>();
//		portfolio.put("id", "1");
//		portfolio.put("deviation", "5");
//		portfolio.put("type", "fund");
//		portfolio.put("allocations",allocations.toString());
//
//		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//		Map map = new HashMap<String, String>();
//		map.put("Content-Type", "application/json");
//
//		headers.setAll(map);
//
//		Map mapp = new HashMap();
//		mapp.put("id", "1");
//		mapp.put("deviation", "5");
//		mapp.put("type", "fund");
//		mapp.put("allocations", new JSONObject(allocations));
//
//		HttpEntity<?> request = new HttpEntity<>(mapp, headers);
//		ResponseEntity<Portfolio> response = this.restTemplate.postForEntity("/portfolio/1", request, Portfolio.class);
//		assertThat(response.getStatusCode().value()).isEqualTo(200);
//	}
//
//	@Test
//	public void deviationPUTResponse() {
//		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//		Map map = new HashMap<String, String>();
//		map.put("Content-Type", "application/json");
//		headers.setAll(map);
//
//		Map requestBody = new HashMap();
//		requestBody.put("deviation", "5");
//
//
//		HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
//		this.restTemplate.put("/portfolio/1/deviation", request);
//
//		ResponseEntity<Portfolio> response = this.restTemplate.getForEntity("/portfolio/1", Portfolio.class);
//		assertThat(response.getStatusCode().value()).isEqualTo(200);
//		assertThat(response.getBody().getDeviation()).isEqualTo("5");
//	}
//}
