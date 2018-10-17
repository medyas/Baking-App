package ml.medyas.bakingapp;


import android.content.res.Resources;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ml.medyas.bakingapp.Activities.RecipeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;


@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(DateUtils.SECOND_IN_MILLIS * 75 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(DateUtils.SECOND_IN_MILLIS * 75 * 2, TimeUnit.MILLISECONDS);

        // Now we wait
        IdlingRegistry.getInstance().register(mIdlingResource);

    }


    @Test
    public void RecyclerViewTest() {
        onView(ViewMatchers.withId(R.id.recipe_recycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.detail_title)).check(matches(withText("Nutella Pie")));

        onView(withId(R.id.feedback_icon3)).perform(click());

        onView(ViewMatchers.withId(R.id.nestedScroll)).perform(swipeUp());
        onView(ViewMatchers.withId(R.id.detail_recycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.view_step_title)).check(matches(withText(containsString("Recipe"))));

        onView(withId(R.id.view_swipe_right)).perform(click());
        onView(withId(R.id.view_step_title)).check(matches(withText(containsString("prep"))));

        onView(withId(R.id.view_swipe_left)).perform(click());
        onView(withId(R.id.view_step_title)).check(matches(withText(containsString("Steps"))));

        onView(isRoot()).perform(ViewActions.pressBack());
        onView(isRoot()).perform(ViewActions.pressBack());

        onView(ViewMatchers.withId(R.id.recipe_recycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(withId(R.id.detail_title)).check(matches(withText("Cheesecake")));
        onView(withId(R.id.feedback_icon4)).perform(click());

        onView(ViewMatchers.withId(R.id.nestedScroll)).perform(swipeUp());
        onView(ViewMatchers.withId(R.id.detail_recycleView)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.view_step_title)).check(matches(withText(containsString("cookie"))));

        onView(withId(R.id.recipe_detail_view_container)).perform(swipeToRight());
        onView(withId(R.id.view_step_title)).check(matches(withText(containsString("water"))));

        onView(withId(R.id.recipe_detail_view_container)).perform(swipeToLeft());
        onView(withId(R.id.view_step_title)).check(matches(withText(containsString("cookie"))));
    }

    @After
    public void unregisterIdlingResources() {

        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }


    /* from https://stackoverflow.com/questions/43869865/swipe-left-not-working-for-view-pager-in-android-espresso
        with modification
     */
    public static ViewAction swipeToRight() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.BOTTOM_LEFT,
                GeneralLocation.BOTTOM_RIGHT, Press.FINGER);
    }
    public static ViewAction swipeToLeft() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.BOTTOM_RIGHT,
                GeneralLocation.BOTTOM_LEFT, Press.FINGER);
    }

    /**
     * Original source from Espresso library, modified to handle spanned fields
     *
     * Returns a matcher that matches a descendant of {@link TextView} that is
     * displaying the string associated with the given resource id.
     *
     * @param text
     *            the string resource the text view is expected to hold.
     */
    public static Matcher<View> withTexts(final String text) {

        return new BoundedMatcher<View, TextView>(TextView.class) {
            private String resourceName = null;
            private String expectedText = null;

            @Override
            public void describeTo(Description description) {
                description.appendText("with string from resource id: ");
                description.appendValue(text);
                if (null != this.resourceName) {
                    description.appendText("[");
                    description.appendText(this.resourceName);
                    description.appendText("]");
                }
                if (null != this.expectedText) {
                    description.appendText(" value: ");
                    description.appendText(this.expectedText);
                }
            }

            @Override
            public boolean matchesSafely(TextView textView) {
                if (null == this.expectedText) {
                    try {
                        this.expectedText = text;
                    } catch (Resources.NotFoundException ignored) {
                        /*
                         * view could be from a context unaware of the resource
                         * id.
                         */
                    }
                }
                if (null != this.expectedText) {
                    return this.expectedText.equals(textView.getText()
                            .toString());
                } else {
                    return false;
                }
            }
        };
    }


    /** Perform action of waiting for a specific view id. https://stackoverflow.com/questions/21417954/espresso-thread-sleep */
    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    /* https://stackoverflow.com/questions/23381459/how-to-get-text-from-textview-using-espresso */
   public static  String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }
}
