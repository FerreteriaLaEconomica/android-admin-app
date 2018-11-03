package com.smontiel.ferretera.admin.features.auth;

import com.smontiel.ferretera.admin.BasePresenter;
import com.smontiel.ferretera.admin.BaseView;
import com.smontiel.ferretera.admin.data.User;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public interface SignupContract {
    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showInfoDialog(String message);

        void onSignupSuccess(User user);
    }

    interface Presenter extends BasePresenter {

        void signUp(String nombre, String apellidos, String email, String password, String telefono);
    }
}
