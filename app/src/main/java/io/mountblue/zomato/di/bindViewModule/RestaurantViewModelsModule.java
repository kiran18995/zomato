package io.mountblue.zomato.di.bindViewModule;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import io.mountblue.zomato.data.remote.RestaurantViewModel;
import io.mountblue.zomato.di.ViewModelKey;
import io.mountblue.zomato.view.CollectionViewModel;

@Module
public abstract class RestaurantViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(CollectionViewModel.class)
    public abstract ViewModel bindAuthViewModel(CollectionViewModel viewModel);

}
