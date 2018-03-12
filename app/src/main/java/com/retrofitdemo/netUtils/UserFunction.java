package com.retrofitdemo.netUtils;


import com.retrofitdemo.model.SearchModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 3/6/2018.
 */

public class UserFunction {

    public ArrayList<SearchModel> searReciverCompany(String sName) {
        ArrayList<SearchModel> ListData = new ArrayList<SearchModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "service_user.php" + "?key=1228&s=15&name=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject r = jsonArray.getJSONObject(i);
                SearchModel da = new SearchModel();
                da.setId(r.getString("id"));
                da.setName(r.getString("company_name"));
                da.setAddress(r.getString("company_address"));
                da.setCountry(r.getString("company_country"));
                da.setState(r.getString("company_state"));
                da.setCity(r.getString("company_city"));
                da.setPincode(r.getString("company_pincode"));
                da.setMobile(r.getString("company_mobile"));
                ListData.add(da);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }

    public ArrayList<SearchModel> searReciverPerson(String sName) {
        ArrayList<SearchModel> ListData = new ArrayList<SearchModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "service_user.php" + "?key=1226&s=16&name=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject r = jsonArray.getJSONObject(i);
                SearchModel da = new SearchModel();
                da.setId(r.getString("id"));
                da.setName(r.getString("person_name"));
                da.setAddress(r.getString("person_home_address"));
                da.setCountry(r.getString("person_country"));
                da.setState(r.getString("person_state"));
                da.setCity(r.getString("person_city"));
                da.setPincode(r.getString("person_pincode"));
                da.setMobile(r.getString("person_mobile"));
                ListData.add(da);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }

    public ArrayList<SearchModel> searCity(String sName) {
        ArrayList<SearchModel> ListData = new ArrayList<SearchModel>();
        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL(RetrofitClient.service_url + "service_user.php" + "?key=1226&s=26&name=" + temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject r = jsonArray.getJSONObject(i);
                SearchModel da = new SearchModel();
                da.setId(r.getString("id"));
                da.setName(r.getString("name"));
                ListData.add(da);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListData;
    }
}
