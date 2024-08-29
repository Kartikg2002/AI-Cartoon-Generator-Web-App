package com.incapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class MyController {

	private static final Logger logger = LoggerFactory.getLogger(MyController.class);
	private RestTemplate restTemplate = new RestTemplate();;

	private static final String API_KEY = "QpK8ciP346fGX-zJOUxdZYF-xF99qF-buL9mdfJ1LRDAmXTLNS";
	private static final String API_HOST = "AI-Cartoon-Generator.allthingsdev.co";
	private static final String API_ENDPOINT_1 = "b8d9da51-8404-4936-bd3d-f4a91fe232ae";
	private static final String BASE_URL = "https://AI-Cartoon-Generator.proxy-production.allthingsdev.co";

	@ModelAttribute
	public void commonValue(ModelMap m) {
		m.addAttribute("appName", "AI Generator Web App");
	}

	// Home
	@RequestMapping(value = { "/", "home" })
	public String home(ModelMap mm) {
		return "home";
	}

	@PostMapping("generate1")
	public String generateCartoonImage(@RequestPart("image") MultipartFile image, ModelMap mm) {

		if (image.getSize() == 0) {
			mm.addAttribute("result", "Please upload an image");
			return "home";
		}

		String URL = BASE_URL + "/image/effects/generate_cartoonized_image";

		try {
			HttpHeaders headers = createHeaders(API_KEY, API_HOST, API_ENDPOINT_1);
			LinkedMultiValueMap<String, Object> data = new LinkedMultiValueMap<>();

			data.add("image", convert(image));
			data.add("task_type", "async");

			HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data, headers);
			ResponseEntity<String> result = restTemplate.postForEntity(URL, requestEntity, String.class);

			JSONObject jo = new JSONObject(result.getBody());
			String taskID = jo.getString("task_id");

			if (jo.getJSONObject("error_detail").getInt("status_code") == 200) {
				mm.addAttribute("result", "Success");
				String resultUrl = getResultUrl(taskID);
				mm.addAttribute("result_url", resultUrl);
			} else {
				mm.addAttribute("result", "Status Failed");
			}
		} catch (Exception e) {
			mm.addAttribute("result", "An error occurred while processing your request. "+ e.getMessage() );
		}

		return "home";
	}

	// Convert MultipartFile to FileSystemResource
	private static FileSystemResource convert(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convFile)) {
			convFile.createNewFile();
			fos.write(file.getBytes());
		} catch (IOException e) {
			logger.error("An error occurred while converting file: {}", e.getMessage());
		}
		return new FileSystemResource(convFile);
	}

	// Create HTTP headers
	private HttpHeaders createHeaders(String apiKey, String apiHost, String apiEndpoint) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("x-apihub-key", apiKey);
		headers.add("x-apihub-host", apiHost);
		headers.add("x-apihub-endpoint", apiEndpoint);
		return headers;
	}

	// Get result URL with polling
	private String getResultUrl(String taskID) throws Exception {
		String URL = BASE_URL + "/api/allthingsdev/query-async-task-result?task_id=" + taskID;
		HttpHeaders headers = createHeaders(API_KEY, API_HOST, "679a2896-4f0e-49f3-80ba-ef5443ec6261");
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);

		int maxAttempts = 10; // Maximum number of polling attempts
		int attempt = 0;
		int delayBetweenAttempts = 15000; // Delay in milliseconds between polling attempts

		while (attempt < maxAttempts) {
			
				// Poll the API for task status
				ResponseEntity<String> result = restTemplate.exchange(URL, HttpMethod.GET, requestEntity, String.class);
				
				// Wait before the next attempt
				Thread.sleep(delayBetweenAttempts);

				JSONObject jo = new JSONObject(result.getBody());

				// Check if the 'data' key exists and has 'result_url'
				if (jo.has("data") && jo.getJSONObject("data").has("result_url")) {

					return jo.getJSONObject("data").getString("result_url");

				} else {
					// Log an informative message if 'data' or 'result_url' is missing
					logger.info("Result URL not found in response. Attempt: {}", attempt + 1);
				}

			attempt++;
		}

		logger.warn("Task did not complete after {} attempts.", maxAttempts);
		return null;
	}

}
