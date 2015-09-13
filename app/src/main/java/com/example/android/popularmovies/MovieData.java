package com.example.android.popularmovies;

import java.util.ArrayList;

/**
 * Created by kev on 9/13/15.
 */
public class MovieData {
    private boolean adult;
    private String backdrop_path;
    private ArrayList<Integer> genre_ids;
    private long id;
    private String original_language;
    private String original_title;
    private String overview;
    private String release_date;
    private String poster_path;
    private double popularity;
    private String title;
    private boolean video;
    private String vote_average;
    private String vote_count;

    public MovieData(){
        genre_ids = new ArrayList<Integer>();
    }

    public boolean getAdult(){
        return adult;
    }

    public void setAdult(boolean choice){
        adult = choice;
    }

    public String getBackdropPath(){
        return backdrop_path;
    }

    public void setBackdropPath(String path){
        backdrop_path = path;
    }

    public ArrayList<Integer> getGenreIds(){
        return genre_ids;
    }

    public void setGenreIds(ArrayList<Integer> input){
        genre_ids = input;
    }

    public int getGenreIdsItem(int position){
        return genre_ids.get(position);
    }

    public void setGenreIdsItem(int position, int data){
        genre_ids.set(position, data);
    }

    public long getId(){
        return id;
    }

    public void setId(long data){
        id = data;
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

    public double getPopularity(){
        return popularity;
    }

    public void setPopularity(double data){
        popularity = data;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String data){
        title = data;
    }

    public boolean getVideo(){
        return video;
    }

    public void setVideo(boolean data){
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
}
