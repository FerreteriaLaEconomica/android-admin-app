package com.smontiel.ferretera.admin.features.show_categories;

import com.smontiel.ferretera.admin.base.BasePresenter;
import com.smontiel.ferretera.admin.base.BaseView;

import java.util.List;

/**
 * Created by Salvador Montiel on 23/11/18.
 */

public interface ShowCategoriesContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean isLoading);

        void setProgressBarIndicator(boolean isLoading);

        void showError(String message);

        void showCategories(List<CategoryItem> productos);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadAllCategories();

        void createCategory(String name);
    }
}
