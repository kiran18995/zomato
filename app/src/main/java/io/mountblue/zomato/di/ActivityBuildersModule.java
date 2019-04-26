package io.mountblue.zomato.di;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import io.mountblue.zomato.MainActivity;
import io.mountblue.zomato.RestaurantDetails;
import io.mountblue.zomato.di.bindViewModule.RestaurantViewModelsModule;
import io.mountblue.zomato.view.fragment.GoOutFragment;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector(
            modules = {RestaurantViewModelsModule.class,}
    )
    abstract GoOutFragment goOutFragment();

    @ContributesAndroidInjector(
            modules = {RestaurantViewModelsModule.class,}
    )
    abstract RestaurantDetails getRestaurantReviews();

    @Provides
    static String someString() {
        return "this is string";
    }
}
