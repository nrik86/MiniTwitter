package com.enriquejimenez.minitwitter.ui.fragment;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enriquejimenez.minitwitter.R;

public class NewTweetDialogFragment extends DialogFragment {


    public NewTweetDialogFragment() {
        // Required empty public constructor
    }


    public static NewTweetDialogFragment newInstance(String param1, String param2) {
        NewTweetDialogFragment fragment = new NewTweetDialogFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_new_tweet_dialog, container, false);
        return view;
    }

}
