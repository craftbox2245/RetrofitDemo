package com.retrofitdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.retrofitdemo.adapter.GridAdapter;
import com.retrofitdemo.adapter.StateAdapter;
import com.retrofitdemo.custom.RecyclerViewPositionHelper;
import com.retrofitdemo.custom.SpacesItemDecoration;
import com.retrofitdemo.custom.Toaster;
import com.retrofitdemo.model.GridModel;
import com.retrofitdemo.model.StateModel;
import com.retrofitdemo.netUtils.DBHelper;
import com.retrofitdemo.netUtils.RequestInterface;
import com.retrofitdemo.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<StateModel> data = new ArrayList<>();
    ArrayList<GridModel> Griddata = new ArrayList<>();
    StateAdapter adapter;
    DBHelper db;

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;

    int firstVisibleItem, visibleItemCount, totalItemCount, count = 0;
    protected int m_PreviousTotalCount;
    RecyclerViewPositionHelper mRecyclerViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        db = new DBHelper(MainActivity.this);
        ButterKnife.bind(this);
        if (GlobalElements.isConnectingToInternet(MainActivity.this)) {
            getState();
        } else {
            GlobalElements.showDialog(MainActivity.this);
        }


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                if (totalItemCount == 0 || adapter == null)
                    return;
                if (m_PreviousTotalCount == totalItemCount) {
                    return;
                } else {
                    boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                    if (loadMore) {
                        m_PreviousTotalCount = totalItemCount;
                        if (GlobalElements.isConnectingToInternet(MainActivity.this)) {
                            //getOnlineVisitorHistory(query);
                        }
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getState() {
        try {
            final ProgressDialog pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getState();

            /*RequestBody requestfile_image_fronted, requestfile_image_backend;
            MultipartBody.Part body_image_fronted, body_image_backend;
            Call<ResponseBody> call;
            if (imageFileFronted != null && imageFileBackend != null) {
                requestfile_image_fronted = RequestBody.create(MediaType.parse("image*//*"), imageFileFronted);
                body_image_fronted = MultipartBody.Part.createFormData("image_path", imageFileFronted.getName(), requestfile_image_fronted);
                requestfile_image_backend = RequestBody.create(MediaType.parse("image*//*"), imageFileBackend);
                body_image_backend = MultipartBody.Part.createFormData("image_path1", imageFileBackend.getName(), requestfile_image_backend);
                call = request.addvisitor(myPreferences.getPreferences(MyPreferences.UID), myPreferences.getPreferences(MyPreferences.Company_id), name, mobile_no, companyname, email, address, remark, city, state, country_name, rate, msg_type, body_image_fronted, body_image_backend);
            }*/

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        String json_response = response.body().string();
                        JSONArray json = new JSONArray(json_response);
                        data.clear();
                        db.DeleteTable(DBHelper.COUNTRY);
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject c = json.getJSONObject(i);
                            StateModel da = new StateModel();
                            da.setCountry(c.getString("name"));
                            da.setCapital(c.getString("capital"));
                            da.setRegion(c.getString("region"));
                            data.add(da);
                            db.addTest(c.getString("name"), c.getString("region"), c.getString("capital"));
                        }

                        /* todo this is list layout*/
                        adapter = new StateAdapter(MainActivity.this, data);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayout.VERTICAL, false));

                        // db.exportDB(MainActivity.this);


                        /* todo this is grid layout*/
                        /*GridModel da = new GridModel();
                        da.setId("");
                        da.setImage_path("");
                        Griddata.add(da);
                        da = new GridModel();
                        da.setId("");
                        da.setImage_path("");
                        Griddata.add(da);
                        da = new GridModel();
                        da.setId("");
                        da.setImage_path("");
                        Griddata.add(da);
                        da = new GridModel();
                        da.setId("");
                        da.setImage_path("");
                        Griddata.add(da);
                        da = new GridModel();
                        da.setId("");
                        da.setImage_path("");
                        Griddata.add(da);da = new GridModel();
                        da.setId("");
                        da.setImage_path("");
                        Griddata.add(da);
                        da = new GridModel();
                        da.setId("");
                        da.setImage_path("");
                        Griddata.add(da);da = new GridModel();
                        da.setId("");
                        da.setImage_path("");
                        Griddata.add(da);
                        da = new GridModel();
                        da.setId("");
                        da.setImage_path("");
                        Griddata.add(da);

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
                        int spanCount = 3; // 3 columns
                        int spacing = 3; // 50px
                        boolean includeEdge = false;
                        recyclerView.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, includeEdge));
                        recyclerView.setAdapter(new GridAdapter(MainActivity.this, Griddata));
                        recyclerView.setLayoutManager(layoutManager);*/

                        pd.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                    System.out.print("error" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
