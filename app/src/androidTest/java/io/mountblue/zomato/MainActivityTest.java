package io.mountblue.zomato;

import android.content.ComponentName;
import android.os.SystemClock;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.mountblue.zomato.view.activity.SearchActivity;
import io.mountblue.zomato.view.fragment.SearchFragment;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotEquals;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    public ActivityTestRule<SearchActivity> searchActivityRule = new ActivityTestRule<>(SearchActivity.class);

    private MainActivity mActivity = null;
    private SearchActivity searchActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        Intents.init();
        mActivity.getSupportFragmentManager().beginTransaction();
        searchActivity = searchActivityRule.getActivity();
    }

    /*@Test
    public void isMainActivityLaunched() {
        SystemClock.sleep(2000);
        intended(hasComponent(MainActivity.class.getName()));
    }*/

    @Test
    public void searchAddress() {
        onView(withId(R.id.address_heading)).perform(click());
        onView(withId(R.id.location_query)).perform(clearText(), typeText("kora"));
        SystemClock.sleep(2000);
        onView(withId(R.id.location_suggestions)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        intended(hasComponent(new ComponentName(getTargetContext(), MainActivity.class)));
    }

    @Test
    public void checkDetailActivity() {
        SystemClock.sleep(2000);
        onView(withId(R.id.restaurantList)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        intended(hasComponent(new ComponentName(getTargetContext(), RestaurantDetails.class)));
    }

    @Test
    public void bookmarkTest() {
        SystemClock.sleep(1000);
        onView(withId(R.id.image_bookmark)).perform(click());
        RecyclerView recyclerView = mActivityTestRule.getActivity().findViewById(R.id.restaurantList_bookmark);
        int itemCount = recyclerView.getAdapter().getItemCount();
        onView(withId(R.id.back_icon)).perform(click());
        SystemClock.sleep(2000);
        onView(withId(R.id.restaurantList)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.action_bookmark)).perform(click());
        SystemClock.sleep(2000);
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(withId(R.id.image_bookmark)).perform(click());
        RecyclerView recyclerViewAgain = mActivityTestRule.getActivity().findViewById(R.id.restaurantList_bookmark);
        int itemCountAgain = recyclerViewAgain.getAdapter().getItemCount();
        assertNotEquals(itemCountAgain,itemCount);
    }

    @Test
    public void searchRestaurant() {
        onView(withId(R.id.layout_search)).perform(click());
        onView(withId(R.id.search_on_fragment)).perform(clearText(), typeText("shawarama"));
        SystemClock.sleep(2000);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
        searchActivity = null;
        Intents.release();
    }
}