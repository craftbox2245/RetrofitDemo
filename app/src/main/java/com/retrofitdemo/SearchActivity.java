package com.retrofitdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.retrofitdemo.adapter.ImageAdapter;
import com.retrofitdemo.custom.RxSearchObservable;
import com.retrofitdemo.netUtils.MyPreferences;
import com.retrofitdemo.netUtils.RequestInterface;
import com.retrofitdemo.netUtils.RetrofitClient;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    ArrayList<Image> Imagedata = new ArrayList<>();
    File file;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    RecyclerView rvImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = new Intent(SearchActivity.this, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent, Constants.REQUEST_CODE);

        rvImages = (RecyclerView) findViewById(R.id.recycleview);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Imagedata.addAll(images);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0, l = images.size(); i < l; i++) {
                bitmaps.add(BitmapFactory.decodeFile(images.get(i).path));
                file = new File(images.get(i).path);
            }
            images(bitmaps, rvImages);
        }
    }

    private void images(ArrayList<Bitmap> b, RecyclerView lv) {
        //img.setVisibility(View.GONE);
        //btnUpload.setVisibility(View.VISIBLE);
        b.add(0, null);
        ImageAdapter adapter = new ImageAdapter(b, this);
        lv.setAdapter(adapter);
        lv.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
    }

    private MultipartBody.Part prepareFilePart(String partName, String fileUri, File file1) {
        File file = new File(fileUri);
        RequestBody requestBody;
        requestBody = RequestBody.create(MediaType.parse("image/*"), file1);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }

    private void addPostImage(String postId) {
        try {

            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call;
            List<MultipartBody.Part> parts = new ArrayList<>();
            for (int i = 0; i < Imagedata.size(); i++) {
                String path = Imagedata.get(i).path;
                File f = new File(path);
                parts.add(prepareFilePart("image_path[" + i + "]", f.getName(), f));
            }
            call = request.addPostImage("", postId, Imagedata.size(), parts);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.print("error" + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpSearchObservable(SearchView search) {

        RxSearchObservable.fromView(search)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text) throws Exception {
                        if (text.isEmpty()) {
                            //textViewResult.setText("");
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .switchMap(new Function<String, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(String query) throws Exception {
                        return dataFromNetwork(query);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            System.out.print("" + responseBody.byteStream());
                            String json_response = null;
                            try {
                                json_response = responseBody.string();
                                System.out.print("" + json_response);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.print("" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * Simulation of network data
     */
    private Observable<ResponseBody> dataFromNetwork(final String query) {
        RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
        return request.searchReciverPerson(query);
    }
}
