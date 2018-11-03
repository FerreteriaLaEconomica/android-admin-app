package com.smontiel.ferretera.admin.features.auth;

import com.smontiel.ferretera.admin.BasePresenter;
import com.smontiel.ferretera.admin.BaseView;
import com.smontiel.ferretera.admin.data.User;

/**
 * Created by Salvador Montiel on 29/10/18.
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showInfoDialog(String message);

        void showProgressDialog();

        void hideProgressDialog();

        void onLoginSuccess(User user);
    }

    interface Presenter extends BasePresenter {

        void logIn(String email, String password);
    }
}
