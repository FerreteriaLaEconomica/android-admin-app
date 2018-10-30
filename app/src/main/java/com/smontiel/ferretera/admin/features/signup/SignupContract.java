package com.smontiel.ferretera.admin.features.signup;

import com.smontiel.ferretera.admin.BasePresenter;
import com.smontiel.ferretera.admin.BaseView;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public interface SignupContract {
    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showInfoDialog(String message);

        void onSuccess();
    }

    interface Presenter extends BasePresenter {

        void signUp(String nombre, String apellidos, String email, String password, String telefono);
    }
}
