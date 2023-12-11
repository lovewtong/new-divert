package com.spotifyapiuse.divert.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @className: AccessToken
 * @description: AccessToken类
 * @author: sy
 * @date: 2023-11-24
 **/

@ApiModel(value = "accessTokenInformation")
@Data
@TableName("accessToken")
public class AccessToken {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id; // 自动生成的userId，用作redis的Key

    private String token; // access_token

    public static AccessToken of(String token) {
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(token);
        return accessToken;
    }
}