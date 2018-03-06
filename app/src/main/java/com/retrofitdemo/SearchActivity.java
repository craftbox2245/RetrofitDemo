package com.retrofitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;

import com.retrofitdemo.custom.RxSearchObservable;
import com.retrofitdemo.netUtils.RequestInterface;
import com.retrofitdemo.netUtils.RetrofitClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
