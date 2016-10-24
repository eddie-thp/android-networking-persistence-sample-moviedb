package com.shrikant.themoviedb.fragments;

import com.shrikant.themoviedb.R;
import com.shrikant.themoviedb.adapters.ComplexRecyclerViewCurrentMoviesAdapter;
import com.shrikant.themoviedb.models.Movie;
import com.shrikant.themoviedb.network.RetrofitExample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by spandhare on 10/23/16.
 */

public class TopRatedMoviesFragment extends Fragment {
    //public LinearLayoutManager layoutManager;
    public ArrayList<Movie> mMovies;
    public ComplexRecyclerViewCurrentMoviesAdapter mComplexRecyclerViewCurrentMoviesAdapter;

    @BindView(R.id.rvMovies)
    RecyclerView mRecyclerViewMovies;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View v = inflater.inflate(R.layout.movies_layout, parent, false);
        ButterKnife.bind(this, v);

        mRecyclerViewMovies.setAdapter(mComplexRecyclerViewCurrentMoviesAdapter);
        mRecyclerViewMovies.setHasFixedSize(true);


//        // Setup layout manager for items
//        layoutManager = new LinearLayoutManager(getActivity());
//
//        // Control orientation of the items
//        // also supports LinearLayoutManager.HORIZONTAL
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//
//        // Optionally customize the position you want to default scroll to
//        layoutManager.scrollToPosition(0);

        // Set layout manager to position the items
        // Attach the layout manager to the recycler view
        mRecyclerViewMovies.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (!isOnline()) {
            Toast.makeText(getContext(), "Your device is not online, " +
                            "check wifi and try again!",
                    Toast.LENGTH_LONG).show();
        } else {
            mMovies.clear();
            mComplexRecyclerViewCurrentMoviesAdapter.notifyDataSetChanged();

            //kick off network query
            populateTopRatedMovies();
        }
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovies = new ArrayList<>();
        mComplexRecyclerViewCurrentMoviesAdapter =
                new ComplexRecyclerViewCurrentMoviesAdapter(getActivity(),
                        mMovies);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Home", "Home resume");
    }

    protected boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }

    void populateTopRatedMovies() {
        RetrofitExample
                .with(getContext())
                .load(mMovies)
                .into(mComplexRecyclerViewCurrentMoviesAdapter)
                .updateTopRatedMovies();
    }
}