package com.smontiel.ferretera.admin.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.smontiel.ferretera.admin.R;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Salvador Montiel on 15/11/18.
 */
public class RxImageView extends AppCompatImageView {
    private BehaviorSubject<Boolean> publishSubject = BehaviorSubject.create();

    public RxImageView(Context context) {
        super(context);
    }

    public RxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if(publishSubject != null)
            publishSubject.onNext(true);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        if(publishSubject != null)
            publishSubject.onNext(true);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        Drawable resDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher_background);

        if(publishSubject != null)
            publishSubject.onNext(true);
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        if(publishSubject != null)
            publishSubject.onNext(true);
    }

    public BehaviorSubject<Boolean> getPublishSubject() {
        return publishSubject;
    }
}
