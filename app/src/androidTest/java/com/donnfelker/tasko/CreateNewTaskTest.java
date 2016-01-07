package com.donnfelker.tasko;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.donnfelker.tasko.models.Task;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.donnfelker.tasko.TaskoMatchers.withCompoundDrawable;
import static com.donnfelker.tasko.TaskoMatchers.withImageDrawable;
import static com.donnfelker.tasko.TaskoMatchers.withTaskViewName;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;

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
    public void testNewTaskScreenShouldHaveCorrectListIcon() {
        // Go to the new task screen
        onView(withId(R.id.menu_main_new_task)).perform(click());

        onView(withCompoundDrawable(R.drawable.ic_action_list)).check(matches(isDisplayed()));

        // verify that the icon is present and correct
        onView(withId(R.id.new_task_task_header))
                .check(matches(withCompoundDrawable(R.drawable.ic_action_list)))
                .check(matches(withText(R.string.create_a_task)));
    }

}
