package com.enriquejimenez.minitwitter.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.enriquejimenez.minitwitter.R;
import com.enriquejimenez.minitwitter.retrofit.response.Like;
import com.enriquejimenez.minitwitter.retrofit.response.Tweet;
import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;

import java.util.List;

public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Tweet> mValues;
    private String ownUserName;

    public MyTweetRecyclerViewAdapter(Context ctx, List<Tweet> items) {
       context = ctx;
       mValues = items;
       ownUserName = SharedPreferencesManager.getString(Constants.PREF_USER);
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

        holder.userNameTextView.setText(holder.mItem.getUser().getUsername());
        holder.messageTextView.setText(holder.mItem.getMensaje());
        holder.likesCountTextView.setText(String.valueOf(holder.mItem.getLikes().size()));
        String photo = holder.mItem.getUser().getPhotoUrl();
        if(!photo.isEmpty()) {
            Glide.with(context)
                    .load(Constants.PHOTO_URL + photo)
                    .into(holder.avatarImageView);
        }

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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView avatarImageView;
        public final ImageView likeImageView;
        public final TextView userNameTextView;
        public final TextView messageTextView;
        public final TextView likesCountTextView;
        public Tweet mItem;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            avatarImageView = (ImageView) view.findViewById(R.id.imageViewAvatar);
            likeImageView = (ImageView) view.findViewById(R.id.imageViewLikes);
            userNameTextView = (TextView) view.findViewById(R.id.textViewUserName);
            messageTextView = (TextView) view.findViewById(R.id.textViewMessage);
            likesCountTextView = (TextView) view.findViewById(R.id.textViewLikes);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + userNameTextView.getText() + "'";
        }
    }
}
