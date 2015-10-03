package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MovieDataPar implements Parcelable {
    private String adult;
    private String backdrop_path;
    private String movieId;
    private String original_language;
    private String original_title;
    private String overview;
    private String release_date;
    private String poster_path;
    private String popularity;
    private String title;
    private String video;
    private String vote_average;
    private String vote_count;
    private String youtube;
    private String review;


    public MovieDataPar(){
    }

    public String getAdult(){
        return adult;
    }

    public void setAdult(String choice){
        adult = choice;
    }

    public String getBackdropPath(){
        return backdrop_path;
    }

    public void setBackdropPath(String path){
        backdrop_path = path;
    }

    public String getMovieId(){
        return movieId;
    }

    public void setMovieId(String data){
        movieId = data;
    }

    public String getOriginalLanguage(){
        return original_language;
    }

    public void setOriginalLanguage(String data){
        original_language = data;
    }

    public String getOriginalTitle(){
        return original_title;
    }

    public void setOriginalTitle(String data){
        original_title = data;
    }

    public String getOverview(){
        return overview;
    }

    public void setOverview(String data){
        overview = data;
    }

    public String getReleaseDate(){
        return release_date;
    }

    public void setReleaseDate(String data){
        release_date = data;
    }

    public String getPosterPath(){
        return poster_path;
    }

    public void setPosterPath(String data){
        poster_path = data;
    }

    public String getPopularity(){
        return popularity;
    }

    public void setPopularity(String data){
        popularity = data;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String data){
        title = data;
    }

    public String getVideo(){
        return video;
    }

    public void setVideo(String data){
        video = data;
    }

    public String getVoteAvg(){
        return vote_average;
    }

    public void setVoteAvg(String data){
        vote_average = data;
    }

    public String getVoteCount(){
        return vote_count;
    }

    public void setVoteCount(String data){
        vote_count = data;
    }

    public String getYouTube(){
        return youtube;
    }

    public void setYouTube(String data){
        youtube = data;
    }

    public String getReview(){
        return review;
    }

    public void setReview(String data){
        review = data;
    }


    // Constructor
    //public MovieDataPar(String id, String name, String grade){
    //    this.id = id;
    //    this.name = name;
    //    this.grade = grade;
    //}
    // Getter and setter methods


    // Parcelling part
    public MovieDataPar(Parcel in){
        String[] data = new String[15];


        in.readStringArray(data);
        //this.id = data[0];
        //this.name = data[1];
        //this.grade = data[2];

        this.adult = data[0];
        this.backdrop_path = data[1];
        this.movieId = data[2];
        this.original_language = data[3];
        this.original_title = data[4];
        this.overview = data[5];
        this.release_date = data[6];
        this.poster_path = data[7];
        this.popularity = data[8];
        this.title = data[9];
        this.video = data[10];
        this.vote_average = data[11];
        this.vote_count = data[12];
        this.youtube = data[13];
        this.review = data[14];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.adult,
                                            this.backdrop_path,
                                            this.movieId,
                                            this.original_language,
                                            this.original_title,
                                            this.overview,
                                            this.release_date,
                                            this.poster_path,
                                            this.popularity,
                                            this.title,
                                            this.video,
                                            this.vote_average,
                                            this.vote_count,
                                            this.youtube,
                                            this.review});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieDataPar createFromParcel(Parcel in) {
            return new MovieDataPar(in);
        }

        public MovieDataPar[] newArray(int size) {
            return new MovieDataPar[size];
        }
    };
}