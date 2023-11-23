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
    private Integer userId; // 自动生成的userId，用作数据库里面的记录
    private String token; // access_token

    private String userName; // 用户名


    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    //自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //逻辑删除
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "过期状态，0为未过期，1为过期，默认为0")
    private Integer deleteStatus;

}
