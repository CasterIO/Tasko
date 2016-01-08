package com.donnfelker.tasko;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.donnfelker.tasko.TaskoMatchers.withTaskViewName;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class CreateNewTaskTest {
    @Rule public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToAddTasksAndHaveThemVisibleOnTheRecyclerView() {

        for(int i =0;i<12;i++) {
            onView(withId(R.id.menu_main_new_task)).perform(click());
            onView(withId(R.id.new_task_task_name)).perform(click());
            onView(withId(R.id.new_task_task_name)).perform(typeText("Foo " + i));
            onView(withId(R.id.new_task_task_desc)).perform(click());
            onView(withId(R.id.new_task_task_desc)).perform(typeText("Foo Bar " + i));
            onView(withId(R.id.new_task_add)).perform(click());
        }

        onView(withText("Foo 1")).check(matches(isDisplayed()));

        onView(withId(R.id.main_task_list)).perform(RealmRecyclerViewActions.scrollTo(withTaskViewName("Foo 10")));

    }

    @Test
    public void fabShouldOnlyBeVisibleOnMainFragment() {
        // Check that fab exists
        onView(withId(R.id.fab)).check(matches(isDisplayed()));

        // click on the fab
        onView(withId(R.id.fab)).perform(click());

        // check to make sure that the fab does not exist
        onView(withId(R.id.fab)).check(matches(not(isDisplayed())));

    }

}
