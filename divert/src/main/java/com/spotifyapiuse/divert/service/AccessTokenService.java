package com.spotifyapiuse.divert.service;

import com.spotifyapiuse.divert.entity.AccessToken;
import com.spotifyapiuse.divert.repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    public AccessTokenService(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    public void saveAccessToken(String token, String userName) {
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(token);
        accessToken.setUserName(userName);
        accessTokenRepository.save(accessToken);
    }
}
