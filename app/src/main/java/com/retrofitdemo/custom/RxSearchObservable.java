package com.retrofitdemo.custom;

import android.support.v7.widget.SearchView;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by CRAFT BOX on 3/5/2018.
 */

public class RxSearchObservable {
    public static Observable<String> fromView(SearchView searchView) {
        final PublishSubject<String> subject = PublishSubject.create();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subject.onNext(newText.toString());
                return false;
            }
        });
        return subject;
    }
}
