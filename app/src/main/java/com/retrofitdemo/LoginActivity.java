package com.retrofitdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.retrofitdemo.netUtils.RequestInterface;
import com.retrofitdemo.netUtils.RetrofitClient;
import com.retrofitdemo.reciver.DownloadService;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    AutoCompleteTextView addvisitor_country;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*try {
            Intent intent = new Intent(AccountActivity.this, DownloadService.class);
            intent.putExtra("file_url", "" + pdffile);
            intent.putExtra("file_name", "" + filename);
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        /*addvisitor_country.setThreshold(1);
        addvisitor_country.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                try {
                    if (addvisitor_country.isPerformingCompletion()) {
                        // An item has been selected from the list. Ignore.
                        return;
                    }
                    searchtext = addvisitor_country.getText().toString();
                    String country = "'%" + searchtext + "%'";
                    Cursor c;
                    if (searchtext != null && !searchtext.equals("")) {
                        c = db.getData("select * from " + DBHelper.COUNTRY + " where name like " + country);
                    } else {
                        c = db.getData("select * from " + DBHelper.COUNTRY + " where name like " + country);
                    }
                    if (c.getCount() > 0) {
                        cityModels.clear();
                        while (c.moveToNext()) {
                            CityModel p = new CityModel();
                            p.setName(c.getString(c.getColumnIndex("name")));
                            p.setId(c.getString(c.getColumnIndex("id")));
                            cityModels.add(p);
                        }
                        madapter = new SearchByNameAdapter(AddvisitorActivity.this, cityModels);
                        addvisitor_country.setAdapter(madapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addvisitor_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            country_id = cityModels.get(position).getId();
                            String country_name = cityModels.get(position).getName();
                            addvisitor_country.setText(country_name);
                            View view1 = getCurrentFocus();
                            if (view1 != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });*/


    }

    private void LoginUser() {
        try {
            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.LoginUser("","","","");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");


                        } else {
                           // Toaster.show(LoginActivity.this, "" + json.getString("ack_msg"), true, Toaster.DANGER);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
