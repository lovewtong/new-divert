package com.spotifyapiuse.divert.controller;

import com.spotifyapiuse.divert.configure.CustomizationProperties;
import com.spotifyapiuse.divert.configure.RestTemplateConfig;
import com.spotifyapiuse.divert.service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/")
public class SpotifyController {

    private final AccessTokenService accessTokenService;
    private final RestTemplate restTemplate;

    final CustomizationProperties customizationProperties;

    @Autowired
    public SpotifyController(AccessTokenService accessTokenService, RestTemplateConfig restTemplate, CustomizationProperties customizationProperties) {
        this.accessTokenService = accessTokenService;
        this.restTemplate = restTemplate.restTemplate();
        this.customizationProperties = customizationProperties;
    }

    @GetMapping("/callback")
    public ResponseEntity<String> spotifyCallback(@RequestParam("code") String code, UriComponentsBuilder uriBuilder) {

        String tokenUrl = "https://accounts.spotify.com/api/token"; // Spotify Token URL

        // 先假设
        String userName = "123";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", code);
        requestBody.add("redirect_uri", customizationProperties.getRedirectUri());
        requestBody.add("client_id", customizationProperties.getClientId());
        requestBody.add("client_secret", customizationProperties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String accessToken = response.getBody(); // 从响应中获取 Access Token
            accessTokenService.saveAccessToken(accessToken, userName); // 将 Access Token 存储到数据库
            return response;
        } else {
            return new ResponseEntity<>("Failed to get access token", HttpStatus.BAD_REQUEST);
        }
    }
}

