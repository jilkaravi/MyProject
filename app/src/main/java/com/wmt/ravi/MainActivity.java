package com.wmt.ravi;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ProgressBar pb;
    Button refresh;
    String TAG = "TAG";
    LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        pb = findViewById(R.id.pb);
        container = findViewById(R.id.container);
        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();
            }
        });
        callApi();
    }

    private void callApi(){
        pb.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://randomuser.me/api/?page=1&results=25";
        JsonObjectRequest
                jsonObjectRequest
                = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            container.removeAllViews();
                            JSONObject resp = new JSONObject(response.toString());
                            JSONArray results = resp.getJSONArray("results");
                            for (int i = 0; i < results.length();i++){
                                JSONObject single = new JSONObject(String.valueOf(results.get(i)));
                                JSONObject name = new JSONObject(String.valueOf(single.getJSONObject("name")));
                                JSONObject picture = new JSONObject(String.valueOf(single.getJSONObject("picture")));
                                JSONObject dob = new JSONObject(String.valueOf(single.getJSONObject("dob")));
                                LayoutInflater inflactor = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                View v = inflactor.inflate(R.layout.item_user,null);
                                TextView tvEmail = v.findViewById(R.id.tvEmail);
                                TextView tvName = v.findViewById(R.id.tvName);
                                ImageView ivUser = v.findViewById(R.id.ivUser);
                                TextView tvDob = v.findViewById(R.id.tvDob);
                                tvEmail.setText(single.getString("email"));
                                tvName.setText(name.getString("title")+". "+name.getString("first")+" "+name.getString("last"));
                                Picasso.get().load(picture.getString("large")).placeholder(getDrawable(R.drawable.user)).into(ivUser);
//                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
//                                String date = format.format(Date.parse(dob.getString("date")));
                                tvDob.setText(dob.getString("date"));
                                container.addView(v);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("TAG", "onResponse: "+e.toString());
                        }
                        pb.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(MainActivity.this,"Error in server, please refresh or try again later.",Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.GONE);
                    }
                });
            queue.add(jsonObjectRequest);
    }
}