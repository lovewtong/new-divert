package com.spotifyapiuse.divert.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {

    private String id;
    private String name;
    private String owner;
    private List<Track> tracks;
}