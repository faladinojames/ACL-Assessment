package com.faladinojames.andela;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AndelaActivity {

    @InjectView(R.id.rv_recycler)
    RecyclerView recyclerView;
    @InjectView(R.id.srl_refresh)
    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        getLagosGithubUsers();


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLagosGithubUsers();
            }
        });

    }

    private void getLagosGithubUsers()
    {
        refreshLayout.setRefreshing(true);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.github.com/search/users?q=location:lagos+language:java",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        refreshLayout.setRefreshing(false);
                        log(response);
                        try{
                            processUsers(response);}
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                shortToast("Unable to load list");
                refreshLayout.setRefreshing(false);
                error.printStackTrace();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    private void processUsers (String j)throws JSONException {
        JSONObject jsonObject = new JSONObject(j);
        JSONArray users = jsonObject.getJSONArray("items");
        List<GithubUser> githubUsers = new ArrayList<>();

        for (int i = 0; i < users.length(); i++)
        {
            githubUsers.add(new GithubUser(users.getJSONObject(i)));
        }

        recyclerView.setAdapter(new Adapter(githubUsers));
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }


    private void showUserDetails(final GithubUser user)
    {

        Dialog dialog= new Dialog(this);
        dialog.setContentView(R.layout.user_details);
        TextView tvUsername= (TextView)dialog.findViewById(R.id.tv_user_details_username);
        TextView tvGithubUrl=(TextView)dialog.findViewById(R.id.tv_user_details_github_url);
        ImageView ivPicture=(ImageView)dialog.findViewById(R.id.iv_user_details_picture);
        Button button =(Button)dialog.findViewById(R.id.bt_user_details_share);
        tvUsername.setText(user.getUsername());
        tvGithubUrl.setText(user.getURL());
        Picasso.with(this).load(user.getAvatarUrl()).into(ivPicture);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome developer @"+user.getUsername()+", "+user.getURL());
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });


        dialog.setCancelable(true);
        dialog.show();



    }
    public class Adapter extends RecyclerView.Adapter<ViewHolder>{

        List<GithubUser> lagosUsers;
         Adapter(List<GithubUser> lagosUsers)
        {
            this.lagosUsers=lagosUsers;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.user_item,parent,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            GithubUser githubUser = lagosUsers.get(position);
            holder.itemView.setTag(githubUser);
            holder.tvUsername.setText(githubUser.getUsername());
            Picasso.with(getApplicationContext()).load(githubUser.getAvatarUrl()).into(holder.ivPicture);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUserDetails((GithubUser)v.getTag());
                }
            });
        }

        @Override
        public int getItemCount() {
            return lagosUsers.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.iv_user_picture)
        ImageView ivPicture;
        @InjectView(R.id.tv_user_username)
        TextView tvUsername;


         ViewHolder(View v)
        {
            super(v);
            ButterKnife.inject(this,v);
        }
    }


}
