package com.example.rickrolled;

import java.util.List;

public class EpisodeData {

    private final int id;
    private final String name;
    private final String air_date;
    private final String episode;
    private final String url;
    private final List<String> characters;

    public EpisodeData(int id, String name, String air_date, String episode, String url, List<String> characters) {
        this.id = id;
        this.name = name;
        this.air_date = air_date;
        this.episode = episode;
        this.url = url;
        this.characters = characters;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAir_date() {
        return air_date;
    }

    public String getEpisode() {
        return episode;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getCharacters() {
        return characters;
    }
}
