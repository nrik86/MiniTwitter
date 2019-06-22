package com.enriquejimenez.minitwitter.ui.tweets.adapter;

import android.content.Context;
import android.graphics.Typeface;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.enriquejimenez.minitwitter.R;
import com.enriquejimenez.minitwitter.mvvm.tweet.TweetViewModel;
import com.enriquejimenez.minitwitter.retrofit.response.Like;
import com.enriquejimenez.minitwitter.retrofit.response.Tweet;
import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;

import java.util.List;

public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Tweet> mValues;
    private String ownUserName;
    private TweetViewModel tweetViewModel;
    public MyTweetRecyclerViewAdapter(Context ctx, List<Tweet> items) {
       context = ctx;
       mValues = items;
       ownUserName = SharedPreferencesManager.getString(Constants.PREF_USER_NAME);
       tweetViewModel = ViewModelProviders.of((FragmentActivity) context).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int likesSize = holder.mItem.getLikes().size();
        setTextViews(holder, likesSize);
        setPhotoImageView(holder);
        onClickListenersView(holder);
        setLikeImageView(holder);
        setImageOpenDialog(holder);
        Log.e("LOG", holder.toString());
    }

    private void setTextViews(ViewHolder holder, int likesSize) {
        holder.userNameTextView.setText("@" + holder.mItem.getUser().getUsername());
        holder.messageTextView.setText(holder.mItem.getMensaje());
        if(likesSize>0){
            holder.likesCountTextView.setText(String.valueOf(likesSize));
        }else{
            holder.likesCountTextView.setText("");
        }
    }

    private void setPhotoImageView(ViewHolder holder) {
        String photo = holder.mItem.getUser().getPhotoUrl();
        if(!photo.isEmpty()) {
            Glide.with(context)
                    .load(Constants.PHOTO_URL + photo)
                    .into(holder.avatarImageView);
        }else {
            Glide.with(context)
                    .load(context.getResources().getDrawable(R.drawable.ic_mini_twitter_perfil))
                    .into(holder.avatarImageView);
        }
    }

    private void onClickListenersView(final ViewHolder holder){
       holder.likeImageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               tweetViewModel.likeTweet(holder.mItem.getId());
           }
       });
       holder.showDialogImageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               tweetViewModel.openDialogTweetMenu(context,holder.mItem.getId());
           }
       });
    }

    private void setLikeImageView(final ViewHolder holder) {
        Glide.with(context)
                .load(R.drawable.ic_like_unfill_black)
                .into(holder.likeImageView);
        holder.likesCountTextView.setTextColor(context.getResources().getColor(android.R.color.black));
        holder.likesCountTextView.setTypeface(null, Typeface.NORMAL);
        for (Like like : holder.mItem.getLikes()){
            if(like.getUsername().equals(ownUserName)){
                Glide.with(context)
                        .load(R.drawable.ic_like_fill_pink)
                        .into(holder.likeImageView);
                holder.likesCountTextView.setTextColor(context.getResources().getColor(R.color.pink));
                holder.likesCountTextView.setTypeface(null, Typeface.BOLD);
                break;
            }
        }
    }

    private void setImageOpenDialog(final ViewHolder holder){
        holder.showDialogImageView.setVisibility(View.GONE);
        if(holder.mItem.getUser().getUsername().equals(ownUserName)){
            holder.showDialogImageView.setVisibility(View.VISIBLE);
        }
    }

    public void setData(List<Tweet> tweetList){
        this.mValues = tweetList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mValues != null){
            return mValues.size();
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView avatarImageView;
        public final ImageView likeImageView;
        public final ImageView showDialogImageView;
        public final TextView userNameTextView;
        public final TextView messageTextView;
        public final TextView likesCountTextView;
        public Tweet mItem;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            avatarImageView = (ImageView) view.findViewById(R.id.imageViewAvatar);
            likeImageView = (ImageView) view.findViewById(R.id.imageViewLikes);
            showDialogImageView = (ImageView) view.findViewById(R.id.imageViewShowDialog);
            userNameTextView = (TextView) view.findViewById(R.id.textViewUserName);
            messageTextView = (TextView) view.findViewById(R.id.textViewMessage);
            likesCountTextView = (TextView) view.findViewById(R.id.textViewLikes);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.getId() + "'";
        }
    }
}
