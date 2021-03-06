package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns="id", childColumns="userId"))
public class Tweet {

    @ColumnInfo
    @PrimaryKey
    public long id;

    @ColumnInfo
    public String body;
    @ColumnInfo
    public String createdAt;

    @ColumnInfo
    public Long userId;

    @ColumnInfo
    public String tweet_URL;

    @ColumnInfo
    public boolean retweeted;

    @ColumnInfo
    public boolean liked;

    @ColumnInfo
    public int numRetweets;

    @ColumnInfo
    public int numLikes;

    @Ignore
    public User user;

    //Empty constructor for Parcel
    public Tweet() { }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet=new Tweet();
        if(jsonObject.has("full_text"))
            tweet.body = jsonObject.getString("full_text");
        else
            tweet.body = jsonObject.getString("text");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.liked = jsonObject.getBoolean("favorited");
        tweet.numLikes = jsonObject.getInt("favorite_count");
        tweet.numRetweets = jsonObject.getInt("retweet_count");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.id=jsonObject.getLong("id");
        User user=User.fromJson(jsonObject.getJSONObject("user"));
        tweet.user=user;
        tweet.userId=user.id;
        if(!jsonObject.getJSONObject("entities").has("media"))
        {
            Log.d("TWEET","No pic");
            tweet.tweet_URL="none";
        }
        else
        {
            Log.d("TWEET HAS PIC",jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url"));
            tweet.tweet_URL=jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
        }

        return  tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        java.util.List<Tweet> tweets= new ArrayList<>();
        for(int i=0;i< jsonArray.length();i++)
        {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
