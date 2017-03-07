package com.faladinojames.andela;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Falade James on 3/6/2017 All Rights Reserved.
 */

public class GithubUser {
    JSONObject object;
    public GithubUser(JSONObject o)
    {
        this.object=o;
    }

    public String getUsername(){
        try {
            return object.getString("login");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String getAvatarUrl(){
        try {
            return object.getString("avatar_url");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    public String getURL(){
        try {
            return object.getString("html_url");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }

    }
}
