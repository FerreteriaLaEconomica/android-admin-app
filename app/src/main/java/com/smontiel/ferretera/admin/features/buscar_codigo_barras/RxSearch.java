package com.smontiel.ferretera.admin.features.buscar_codigo_barras;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Salvador Montiel on 9/12/18.
 */

public class RxSearch {

    public static Observable<String> fromView(MaterialSearchView searchView) {

        final PublishSubject<String> subject = PublishSubject.create();

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subject.onNext(newText);
                return true;
            }
        });

        return subject;
    }
}
