package io.mountblue.zomato.di;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import io.mountblue.zomato.MainActivity;
import io.mountblue.zomato.di.bindViewModule.RestaurantViewModelsModule;
import io.mountblue.zomato.view.gooutfragment.GoOutFragment;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector(
            modules = {RestaurantViewModelsModule.class,}
    )
    abstract GoOutFragment goOutFragment();

    @Provides
    static String someString() {
        return "this is string";
    }
}
