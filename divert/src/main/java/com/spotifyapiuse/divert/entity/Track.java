package com.spotifyapiuse.divert.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Track {

    private String id;
    private String name;
    private String artist;
    private String album;
    private String duration;
}