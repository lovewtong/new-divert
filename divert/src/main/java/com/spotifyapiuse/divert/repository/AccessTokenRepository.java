package com.spotifyapiuse.divert.repository;

import com.spotifyapiuse.divert.entity.AccessToken;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessToken, Long> {
    AccessToken findByUserId(Long userId);
}
