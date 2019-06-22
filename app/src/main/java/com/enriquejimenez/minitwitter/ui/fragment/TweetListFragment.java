package com.enriquejimenez.minitwitter.ui.fragment;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enriquejimenez.minitwitter.data.TweetViewModel;
import com.enriquejimenez.minitwitter.retrofit.response.Tweet;
import com.enriquejimenez.minitwitter.ui.adapter.MyTweetRecyclerViewAdapter;
import com.enriquejimenez.minitwitter.R;
import com.enriquejimenez.minitwitter.utils.Constants;

import java.util.List;


public class TweetListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyTweetRecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Tweet> tweetList;
    private List<Tweet> tweetFavList;

    private TweetViewModel tweetViewModel;

    private int tweetListType = 1;

    public TweetListFragment() {
    }

    @SuppressWarnings("unused")
    public static TweetListFragment newInstance(int tweetListType) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = ViewModelProviders.of(getActivity())
                .get(TweetViewModel.class);

        if (getArguments() != null) {
            tweetListType = getArguments().getInt(Constants.TWEET_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);


        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.tweet_list);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if(tweetListType == Constants.TWEET_LIST_ALL){
                    loadNewTweetData();
                }else if(tweetListType == Constants.TWEET_LIST_FAVS){
                    loadNewFavTweetData();

                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        fillAdapter(tweetList);

        if( tweetListType == Constants.TWEET_LIST_ALL) {
            loadTweetData();
        } else if( tweetListType == Constants.TWEET_LIST_FAVS) {
            loadFavTweetData();
        }

        return view;
    }

    private void fillAdapter(List<Tweet> actualTweetList) {
        adapter = new MyTweetRecyclerViewAdapter(
                getActivity(),
                actualTweetList
        );
        recyclerView.setAdapter(adapter);
    }

    private void loadNewFavTweetData() {
        tweetViewModel.getNewFavsTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                tweetList = tweets;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(tweetList);
                tweetViewModel.getNewFavsTweets().removeObserver(this);
            }
        });

    }

    private void loadFavTweetData() {
        tweetViewModel.getFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                tweetList = tweets;
                adapter.setData(tweetList);
            }
        });
    }

    private void loadTweetData() {
        tweetViewModel.getTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                tweetList = tweets;
                adapter.setData(tweetList);
            }
        });

    }

    private void loadNewTweetData() {
        tweetViewModel.getNewTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(@Nullable List<Tweet> tweets) {
                tweetList = tweets;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(tweetList);
                tweetViewModel.getNewTweets().removeObserver(this);
            }
        });
    }
}
