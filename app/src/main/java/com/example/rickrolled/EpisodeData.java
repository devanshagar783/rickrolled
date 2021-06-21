package com.example.rickrolled;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class EpisodeData implements Parcelable {

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

    protected EpisodeData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        air_date = in.readString();
        episode = in.readString();
        url = in.readString();
        characters = in.createStringArrayList();
    }

    public static final Creator<EpisodeData> CREATOR = new Creator<EpisodeData>() {
        @Override
        public EpisodeData createFromParcel(Parcel in) {
            return new EpisodeData(in);
        }

        @Override
        public EpisodeData[] newArray(int size) {
            return new EpisodeData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(air_date);
        parcel.writeString(episode);
        parcel.writeString(url);
        parcel.writeStringList(characters);
    }
}
