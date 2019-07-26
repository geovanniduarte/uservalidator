package co.com.geo.uservalidator.util.mvvm;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;


class ViewModelFactory implements ViewModelProvider.Factory {

        private final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels;

        @Inject
        public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels) {
                this.viewModels = viewModels;
        }
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
           return  (T) this.viewModels.get(modelClass).get();
        }
}