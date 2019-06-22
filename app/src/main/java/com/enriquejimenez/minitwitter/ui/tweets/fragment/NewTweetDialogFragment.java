package com.enriquejimenez.minitwitter.ui.tweets.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.enriquejimenez.minitwitter.R;
import com.enriquejimenez.minitwitter.mvvm.tweet.TweetViewModel;
import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;

public class NewTweetDialogFragment extends DialogFragment implements View.OnClickListener {

    private ImageView avatarImageView;
    private ImageView closeDialogImageView;
    private Button sendTweetButton;
    private EditText messageTweetEditText;

    private Dialog dialogNewTweet;

    public NewTweetDialogFragment() {
        // Required empty public constructor
    }


    public static NewTweetDialogFragment newInstance() {
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
        dialogNewTweet = getDialog();

        setViews(view);
        eventViews();
        setAvatarImage();

        return view;
    }

    public void setViews(View view){
        sendTweetButton = view.findViewById(R.id.buttonSendTweet);
        avatarImageView = view.findViewById(R.id.imageViewAvatarNewTweet);
        closeDialogImageView = view.findViewById(R.id.imageViewCloseDialog);
        messageTweetEditText = view.findViewById(R.id.editTextMessageNewTweet);
    }

    public void eventViews(){
        sendTweetButton.setOnClickListener(this);
        closeDialogImageView.setOnClickListener(this);
    }

    public void setAvatarImage(){
        if(!SharedPreferencesManager.getString(Constants.PREF_URL_PHOTO).isEmpty()) {
            String photoUser = Constants.PHOTO_URL + SharedPreferencesManager.getString(Constants.PREF_URL_PHOTO);
            Glide.with(getActivity()).load(photoUser).into(avatarImageView);
        }else{
            Glide.with(getActivity())
                    .load(getActivity().getResources().getDrawable(R.drawable.ic_mini_twitter_perfil))
                    .into(avatarImageView);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String message = messageTweetEditText.getText().toString();
        switch (id){
            case R.id.buttonSendTweet:
               sendNewTweet(message);
                break;
            case R.id.imageViewCloseDialog:
                closeDialog(message);
                break;
        }
    }

    private void sendNewTweet(String message) {
        if(!message.isEmpty()) {
            Toast.makeText(getActivity(),"Publicando...", Toast.LENGTH_SHORT).show();
            TweetViewModel tweetViewModel = ViewModelProviders.of(getActivity())
                    .get(TweetViewModel.class);
            tweetViewModel.insertTweet(message);
            dialogNewTweet.dismiss();
        }else{
            Toast.makeText(getActivity(),"Debe escribir un mensaje de texto para enviar", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeDialog(String message) {
        if(!message.isEmpty()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Vas a borrar el Tweet");
            alertDialog.setMessage("¿Seguro que quieres cerrar?. Tu mensaje se perderá");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getActivity().getResources().getString(R.string.delete),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            dialogNewTweet.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getActivity().getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else{
            getDialog().dismiss();
        }
    }
}
