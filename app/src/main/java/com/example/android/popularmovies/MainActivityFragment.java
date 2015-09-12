package com.example.android.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mMovieAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] movieArray = {
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
        };

        ArrayList<String> awesomeMovie = new ArrayList<String>(Arrays.asList(movieArray));

        mMovieAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.image_item_movie,
                R.id.image_item_movie_imageview,
                awesomeMovie);

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movie);
        //gridView.setAdapter(mMovieAdapter);
        gridView.setAdapter(new ImageAdapter(getActivity(), movieArray));
        ImageView imageView = (ImageView) rootView.findViewById(R.id.sosad);
        //Picasso.with(getActivity()).load("http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg").into(imageView);

        return rootView;
    }
}
