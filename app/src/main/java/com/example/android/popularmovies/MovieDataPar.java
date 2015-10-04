package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MovieDataPar implements Parcelable {

    private boolean adult;
    private String backdrop_path;
    private ArrayList<Integer> genre_ids;
    private long movieId;
    private String original_language;
    private String original_title;
    private String overview;
    private String release_date;
    private String poster_path;
    private double popularity;
    private String title;
    private boolean video;
    private double vote_average;
    private int vote_count;
    private String YouTube;
    private String review;




    public MovieDataPar(){
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

    public long getMovieId(){
        return movieId;
    }

    public void setMovieId(long data){
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

    public double getVoteAvg(){
        return vote_average;
    }

    public void setVoteAvg(double data){
        vote_average = data;
    }

    public int getVoteCount(){
        return vote_count;
    }

    public void setVoteCount(int data){
        vote_count = data;
    }

    public String getYouTube(){
        return YouTube;
    }

    public void setYouTube(String data){
        YouTube = data;
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

        this.adult = Boolean.parseBoolean(data[0]);
        this.backdrop_path = data[1];
        this.movieId = Long.parseLong(data[2]);
        this.original_language = data[3];
        this.original_title = data[4];
        this.overview = data[5];
        this.release_date = data[6];
        this.poster_path = data[7];
        this.popularity = Double.parseDouble(data[8]);
        this.title = data[9];
        this.video = Boolean.parseBoolean(data[10]);
        this.vote_average = Double.parseDouble(data[11]);
        this.vote_count = Integer.parseInt(data[12]);
        this.YouTube = data[13];
        this.review = data[14];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {Boolean.toString(this.adult),
                                            this.backdrop_path,
                                            Long.toString(this.movieId),
                                            this.original_language,
                                            this.original_title,
                                            this.overview,
                                            this.release_date,
                                            this.poster_path,
                                            Double.toString(this.popularity),
                                            this.title,
                                            Boolean.toString(this.video),
                                            Double.toString(this.vote_average),
                                            Integer.toString(this.vote_count),
                                            this.YouTube,
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