package com.smontiel.ferretera.admin.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
 * performed by the {@code fragmentManager}.
 *
 * Created by Salvador Montiel on 27/10/18.
 */
public final class ActivityUtils {

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }

    private ActivityUtils() {
        throw new RuntimeException("Can not be instantiated");
    }
}
