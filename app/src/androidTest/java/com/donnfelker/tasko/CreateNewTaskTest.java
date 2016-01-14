package com.donnfelker.tasko;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
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

    @Test
    public void shouldBeAbleToCreateANewTaskAndThenEditTheTask() {
        // click on the fab
        onView(withId(R.id.fab)).perform(click());

        // Create a task
        onView(withId(R.id.new_task_task_name)).perform(click());
        onView(withId(R.id.new_task_task_name)).perform(typeText("Foo"));
        onView(withId(R.id.new_task_task_desc)).perform(click());
        onView(withId(R.id.new_task_task_desc)).perform(typeText("Foo Bar"));
        onView(withId(R.id.new_task_add)).perform(click());

        // Should be back at the main screen now
        // Click on the task to edit it.
        onView(withId(R.id.main_task_list))
                .perform(RealmRecyclerViewActions.actionOnItem(withTaskViewName("Foo"), click()));

        // Should be on the edit screen now, check to see if we have the edit title
        onView(withText("Edit")).check(matches(isDisplayed()));

        // Make sure fab is not visible
        onView(withId(R.id.fab)).check(matches(not(isDisplayed())));

        onView(withId(R.id.new_task_task_name))
                .perform(click())
                .perform(replaceText("ooF"));
        onView(withId(R.id.new_task_task_desc))
                .perform(click())
                .perform(replaceText("raB ooF"));

        // Click on save button
        onView(withId(R.id.new_task_add)).perform(click());

        // Make sure that we can see the item back in the main view
        onView(withText("ooF")).check(matches(isDisplayed()));

        onView(withText("Tasko")).check(matches(isDisplayed()));
    }
}
