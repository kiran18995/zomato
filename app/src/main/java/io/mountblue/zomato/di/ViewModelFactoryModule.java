package io.mountblue.zomato.di;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import dagger.Binds;
import dagger.Module;
import io.mountblue.zomato.viewmodel.ViewModelProviderFactory;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModuleFactory(ViewModelProviderFactory viewModelProviderFactory);
}
