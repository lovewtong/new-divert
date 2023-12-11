package com.spotifyapiuse.divert.configure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @className: CustomizationProperties
 * @description: 自定义配置属性类
 * @author: sy
 * @date: 2022-10-10
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class CustomizationProperties {

    // 自定义clientId
    @Value("${myCustomConstants.clientId}")
    String clientId;

    // 自定义clientSecret
    @Value("${myCustomConstants.clientSecret}")
    String clientSecret;

    // 自定义redirectUri
    @Value("${myCustomConstants.redirectUri}")
    String redirectUri;
}
