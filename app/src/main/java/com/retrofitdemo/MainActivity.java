package com.retrofitdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.retrofitdemo.adapter.StateAdapter;
import com.retrofitdemo.custom.Toaster;
import com.retrofitdemo.model.StateModel;
import com.retrofitdemo.netUtils.RequestInterface;
import com.retrofitdemo.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    ArrayList<StateModel> data=new ArrayList<>();
    StateAdapter adapter;
    @BindView(R.id.recycleview) RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        if(GlobalElements.isConnectingToInternet(MainActivity.this))
        {
            getState();
        }
        else
        {
            GlobalElements.showDialog(MainActivity.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getState(){
        try {
            final ProgressDialog pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getState();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONArray json=new JSONArray(json_response);
                        data.clear();
                        for(int i=0;i<json.length();i++)
                        {
                            JSONObject c=json.getJSONObject(i);
                            StateModel da=new StateModel();
                            da.setCountry(c.getString("name"));
                            da.setCapital(c.getString("capital"));
                            da.setRegion(c.getString("region"));
                            data.add(da);
                        }

                        adapter = new StateAdapter(MainActivity.this,data);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayout.VERTICAL,false));

                    } catch (Exception e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                    System.out.print("error"+t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
