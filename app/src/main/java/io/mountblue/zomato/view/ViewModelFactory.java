package io.mountblue.zomato.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.mountblue.zomato.data.RestaurantRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private RestaurantRepository repository;

    static ViewModelFactory getInstance(RestaurantRepository repository) {
        return new ViewModelFactory(repository);
    }

    private ViewModelFactory(RestaurantRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantViewModel.class)) {
            //noinspection unchecked
            return (T) new RestaurantViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
