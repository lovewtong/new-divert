package com.spotifyapiuse.divert.controller;

import com.spotifyapiuse.divert.configure.CustomizationProperties;
import com.spotifyapiuse.divert.service.AccessTokenService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/")
public class SpotifyController {

    private final StringRedisTemplate stringRedisTemplate;
    private final AccessTokenService accessTokenService;
    private final RestTemplate restTemplate;
    private final CustomizationProperties customizationProperties;

    @Autowired
    public SpotifyController(StringRedisTemplate stringRedisTemplate, AccessTokenService accessTokenService, RestTemplate restTemplate, CustomizationProperties customizationProperties) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.accessTokenService = accessTokenService;
        this.restTemplate = restTemplate; // 正确的注入方式
        this.customizationProperties = customizationProperties;
    }


    @GetMapping("/login")
    public ResponseEntity<String> spotifyLogin() {
        String scope = "user-library-read user-library-modify playlist-read-private playlist-read-collaborative playlist-modify-public playlist-modify-private user-follow-read user-follow-modify";
        String spotifyUrl = String.format("https://accounts.spotify.com/authorize?show_dialog=true&response_type=code&client_id=%s&scope=%s&redirect_uri=%s",
                customizationProperties.getClientId(), scope, customizationProperties.getRedirectUri());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, spotifyUrl);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


    @GetMapping("/callback")
    public ResponseEntity<String> spotifyCallback(@RequestParam("code") String code) {
        // 请求 Spotify API 获取访问令牌
        String tokenUrl = "https://accounts.spotify.com/api/token";

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

            // 从响应中获取 Access Token的JSON，有access_token，token_type，expires_in，refresh_token，scope
            String accessToken = response.getBody();

            System.out.println(accessToken);
            JSONObject jsonObjectObject = new JSONObject(accessToken);

            // 提出access_token值
            String accessTokenStr = jsonObjectObject.getString("access_token");

            System.out.println(accessTokenStr);
            // 获取用户id的返回请求体
            ResponseEntity<String> userInfo = getUserInfo(accessTokenStr);

            // 从 ResponseEntity 中获取响应体中的 JSON 字符串
            String responseBody = userInfo.getBody();

            // 将 JSON 字符串解析为 JSONObject
            JSONObject jsonObject = new JSONObject(responseBody);

            // 提取 id 字段的值
            String id = jsonObject.getString("id");

            // 将 Access Token 存储到 Redis
            stringRedisTemplate.opsForValue().set(id, accessTokenStr);


            return new ResponseEntity<>("Successfully obtained access token and stored in Redis", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to get access token", HttpStatus.BAD_REQUEST);
        }
    }


    // 获取当前用户歌曲库
    @GetMapping("/playlist")
    public ResponseEntity<String> getPlaylist(@RequestParam("userId") String userId){

        // 通过userId获取到accessToken
        String accessToken = stringRedisTemplate.opsForValue().get(userId);

        // 设置请求头，包含 Bearer 认证的 Access Token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+accessToken);

        // 创建请求实体
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 发送 GET 请求到 Spotify API 获取用户信息
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/playlists", HttpMethod.GET, entity, String.class);

        // 返回从 Spotify API 获取到的用户信息或错误信息
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }



    public ResponseEntity<String> getUserInfo(String aToken) {
        // 设置请求头，包含 Bearer 认证的 Access Token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+aToken);

        // 创建请求实体
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 发送 GET 请求到 Spotify API 获取用户信息
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me", HttpMethod.GET, entity, String.class);

        // 返回从 Spotify API 获取到的用户信息或错误信息
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }
}