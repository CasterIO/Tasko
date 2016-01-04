package com.donnfelker.tasko;

import android.support.annotation.Nullable;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.util.HumanReadables;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

/**
 * {@link ViewAction}s to interact {@link RealmRecyclerView}. RealmRecyclerView works differently than
 * {@link AdapterView} and {@link RecyclerView}. In fact, RealmRecyclerView is not a RecyclerView
 * hence it can't be used as you would normally use {@link RecyclerViewActions} as the
 * {@link RecyclerView} is managed internally in {@link RealmRecyclerView}
 * <p/>
 * <p>
 * To use {@link ViewAction}s in this class use {@link Espresso#onView(Matcher)} with a
 * <a href="http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html">
 * <code>Matcher</code></a> that matches your {@link RecyclerView}, then perform a
 * {@link ViewAction} from this class.
 * </p>
 **/
public final class RealmRecyclerViewActions {
    private static final int NO_POSITION = -1;

    private RealmRecyclerViewActions() {
        // no instance
    }

    /**
     * Returns a {@link ViewAction} which scrolls {@link RecyclerView} to the view matched by
     * viewHolderMatcher.
     * <p/>
     * <p>
     * This approach uses {@link ViewHolder}s to find the target view. It will create one ViewHolder
     * per item type and bind adapter data to the ViewHolder. If the itemViewMatcher matches a
     * ViewHolder the current position of the View is used to perform a
     * {@link RecyclerView#scrollToPosition(int)}. Note: scrollTo method is not overloaded, method
     * overloading with generic parameters is not possible.
     * </p>
     *
     * @param viewHolderMatcher a
     *                          <a href="http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html">
     *                          <code>Matcher</code></a> that matches an item view holder in {@link RecyclerView}
     * @throws PerformException if there are more than one items matching given viewHolderMatcher.
     */
    public static <VH extends ViewHolder> PositionableRecyclerViewAction scrollToHolder(
            final Matcher<VH> viewHolderMatcher) {
        return new ScrollToViewAction<VH>(viewHolderMatcher);
    }

    /**
     * Returns a {@link ViewAction} which scrolls {@link RecyclerView} to the view matched by
     * itemViewMatcher.
     * <p/>
     * <p>
     * This approach uses {@link ViewHolder}s to find the target view. It will create one ViewHolder
     * per item type and bind adapter data to the ViewHolder. If the itemViewMatcher matches a
     * ViewHolder the current position of the View is used to perform a
     * {@link RecyclerView#scrollToPosition(int)}.
     * </p>
     *
     * @param itemViewMatcher a
     *                        <a href="http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html">
     *                        <code>Matcher</code></a> that matches an item view in {@link RecyclerView}
     * @throws PerformException if there are more than one items matching given viewHolderMatcher.
     */
    public static <VH extends ViewHolder> PositionableRecyclerViewAction scrollTo(
            final Matcher<View> itemViewMatcher) {
        Matcher<VH> viewHolderMatcher = viewHolderMatcher(itemViewMatcher);
        return new ScrollToViewAction<VH>(viewHolderMatcher);
    }

    /**
     * Returns a {@link ViewAction} which scrolls {@link RecyclerView} to a position.
     *
     * @param position the position of the view to scroll to
     */
    public static <VH extends ViewHolder> ViewAction scrollToPosition(final int position) {
        return new ScrollToPositionViewAction(position);
    }

    /**
     * Performs a {@link ViewAction} on a view matched by viewHolderMatcher.
     * <p/>
     * <ol>
     * <li>Scroll Recycler View to the view matched by itemViewMatcher</li>
     * <li>Perform an action on the matched view</li>
     * </ol>
     *
     * @param itemViewMatcher a
     *                        <a href="http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html">
     *                        <code>Matcher</code></a> that matches an item view in {@link RecyclerView}
     * @param viewAction      the action that is performed on the view matched by itemViewMatcher
     * @throws PerformException if there are more than one items matching given viewHolderMatcher.
     */
    public static <VH extends ViewHolder> PositionableRecyclerViewAction actionOnItem(
            final Matcher<View> itemViewMatcher, final ViewAction viewAction) {
        Matcher<VH> viewHolderMatcher = viewHolderMatcher(itemViewMatcher);
        return new ActionOnItemViewAction<VH>(viewHolderMatcher, viewAction);
    }

    /**
     * Performs a {@link ViewAction} on a view matched by viewHolderMatcher.
     * <p/>
     * <ol>
     * <li>Scroll Recycler View to the view matched by itemViewMatcher</li>
     * <li>Perform an action on the matched view</li>
     * </ol>
     * Note: actionOnItem method is not overloaded, method overloading with
     * generic parameters is not possible.
     *
     * @param viewHolderMatcher a
     *                          <a href="http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html">
     *                          <code>Matcher</code></a> that matchesan item view holder in {@link RecyclerView}
     * @param viewAction        the action that is performed on the view matched by viewHolderMatcher
     * @throws PerformException if there are more than one items matching given viewHolderMatcher.
     */
    public static <VH extends ViewHolder> PositionableRecyclerViewAction actionOnHolderItem(
            final Matcher<VH> viewHolderMatcher, final ViewAction viewAction) {
        return new ActionOnItemViewAction<VH>(viewHolderMatcher, viewAction);
    }

    /**
     * Performs a {@link ViewAction} on a view at position.
     * <p/>
     * <ol>
     * <li>Scroll Recycler View to position</li>
     * <li>Perform an action on the view at position</li>
     * </ol>
     *
     * @param position   position of a view in {@link RecyclerView}
     * @param viewAction the action that is performed on the view matched by itemViewMatcher
     */
    public static <VH extends ViewHolder> ViewAction actionOnItemAtPosition(final int position,
                                                                            final ViewAction viewAction) {
        return new ActionOnItemAtPositionViewAction<VH>(position, viewAction);
    }

    /**
     * Finds positions of items in {@link RecyclerView} which is matching given viewHolderMatcher.
     * This is similar to positionMatching(RecyclerView, Matcher<VH>), except that it returns list of
     * multiple positions if there are, rather than throwing Ambiguous view error exception.
     *
     * @param recyclerView      recycler view which is hosting items.
     * @param viewHolderMatcher a
     *                          <a href="http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html">
     *                          <code>Matcher</code></a> that matches an item view in {@link RecyclerView}
     * @return list of MatchedItem which contains position and description of items in recyclerView.
     * @throws RuntimeException if more than one item or item could not be found.
     */
    @SuppressWarnings("unchecked")
    private static <T extends VH, VH extends ViewHolder> List<MatchedItem> itemsMatching(
            final RecyclerView recyclerView, final Matcher<VH> viewHolderMatcher, int max) {
        final Adapter<T> adapter = recyclerView.getAdapter();
        SparseArray<VH> viewHolderCache = new SparseArray<VH>();
        List<MatchedItem> matchedItems = new ArrayList<MatchedItem>();
        for (int position = 0; position < adapter.getItemCount(); position++) {
            int itemType = adapter.getItemViewType(position);
            VH cachedViewHolder = viewHolderCache.get(itemType);
            // Create a view holder per type if not exists
            if (null == cachedViewHolder) {
                cachedViewHolder = adapter.createViewHolder(recyclerView, itemType);
                viewHolderCache.put(itemType, cachedViewHolder);
            }
            // Bind data to ViewHolder and apply matcher to view descendants.
            adapter.bindViewHolder((T) cachedViewHolder, position);
            if (viewHolderMatcher.matches(cachedViewHolder)) {
                matchedItems.add(new MatchedItem(position, HumanReadables.getViewHierarchyErrorMessage(
                        cachedViewHolder.itemView, null,
                        "\n\n*** Matched ViewHolder item at position: " + position + " ***", null)));
                if (matchedItems.size() == max) {
                    break;
                }
            }
        }
        return matchedItems;
    }

    /**
     * Creates matcher for view holder with given item view matcher.
     *
     * @param itemViewMatcher a item view matcher which is used to match item.
     * @return a matcher which matches a view holder containing item matching itemViewMatcher.
     */
    private static <VH extends ViewHolder> Matcher<VH> viewHolderMatcher(
            final Matcher<View> itemViewMatcher) {
        return new TypeSafeMatcher<VH>() {
            @Override
            public boolean matchesSafely(RecyclerView.ViewHolder viewHolder) {
                return itemViewMatcher.matches(viewHolder.itemView);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("holder with view: ");
                itemViewMatcher.describeTo(description);
            }
        };
    }

    public static void checkArgument(boolean expression,
                                     @Nullable String errorMessageTemplate,
                                     @Nullable Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    private static String format(String template, Object... args) {
        template = String.valueOf(template); // null -> "null"
        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(
                template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));
        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }
        return builder.toString();
    }

    /**
     * Most RecyclerViewActions are given a matcher to select a particular view / viewholder within
     * the RecyclerView. In this case the default behaviour is to expect that the matcher matches 1
     * and only one item within the RecyclerView.
     * <p/>
     * <p>This interface gives users the ability to override that type of behaviour and explicitly
     * select an item in the RecyclerView at a given position. This is similar to on the
     * onData(...).atPosition() api for AdapterViews.
     */
    public interface PositionableRecyclerViewAction extends ViewAction {
        /**
         * Returns a new ViewAction which will cause the ViewAction to operate upon the position-th
         * element which the matcher has selected.
         *
         * @param position a 0-based index into the list of matching elements within the RecyclerView.
         * @return PositionableRecyclerViewAction a new ViewAction focused on a particular position.
         * @throws IllegalArgumentException if position < 0.
         */
        public PositionableRecyclerViewAction atPosition(int position);
    }

    private static final class ActionOnItemViewAction<VH extends ViewHolder> implements
            PositionableRecyclerViewAction {
        private final Matcher<VH> viewHolderMatcher;
        private final ViewAction viewAction;
        private final int atPosition;
        private final ScrollToViewAction<VH> scroller;

        private ActionOnItemViewAction(Matcher<VH> viewHolderMatcher, ViewAction viewAction) {
            this(viewHolderMatcher, viewAction, NO_POSITION);
        }

        private ActionOnItemViewAction(Matcher<VH> viewHolderMatcher, ViewAction viewAction,
                                       int atPosition) {
            this.viewHolderMatcher = checkNotNull(viewHolderMatcher);
            this.viewAction = checkNotNull(viewAction);
            this.atPosition = atPosition;
            this.scroller = new ScrollToViewAction<VH>(viewHolderMatcher, atPosition);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Matcher<View> getConstraints() {
            return allOf(isAssignableFrom(RealmRecyclerView.class), isDisplayed());
        }

        @Override
        public PositionableRecyclerViewAction atPosition(int position) {
            checkArgument(position >= 0, "%d is used as an index - must be >= 0", position);
            return new ActionOnItemViewAction<VH>(viewHolderMatcher, viewAction, position);
        }

        @Override
        public String getDescription() {
            if (atPosition == NO_POSITION) {
                return String.format("performing ViewAction: %s on item matching: %s",
                        viewAction.getDescription(), viewHolderMatcher);
            } else {
                return String.format("performing ViewAction: %s on %d-th item matching: %s",
                        viewAction.getDescription(), atPosition, viewHolderMatcher);
            }
        }

        @Override
        public void perform(UiController uiController, View root) {
            RecyclerView recyclerView = (RecyclerView) root;
            try {
                scroller.perform(uiController, root);
                uiController.loopMainThreadUntilIdle();
                // the above scroller has checked bounds, dupes (maybe) and brought the element into screen.
                int max = atPosition == NO_POSITION ? 2 : atPosition + 1;
                int selectIndex = atPosition == NO_POSITION ? 0 : atPosition;
                List<MatchedItem> matchedItems = itemsMatching(recyclerView, viewHolderMatcher, max);
                actionOnItemAtPosition(matchedItems.get(selectIndex).position, viewAction).perform(
                        uiController, root);
                uiController.loopMainThreadUntilIdle();
            } catch (RuntimeException e) {
                throw new PerformException.Builder().withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(root)).withCause(e).build();
            }
        }
    }

    private static final class ActionOnItemAtPositionViewAction<VH extends ViewHolder> implements
            ViewAction {
        private final int position;
        private final ViewAction viewAction;

        private ActionOnItemAtPositionViewAction(int position, ViewAction viewAction) {
            this.position = position;
            this.viewAction = viewAction;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Matcher<View> getConstraints() {
            return allOf(isAssignableFrom(RealmRecyclerView.class), isDisplayed());
        }

        @Override
        public String getDescription() {
            return "actionOnItemAtPosition performing ViewAction: " + viewAction.getDescription()
                    + " on item at position: " + position;
        }

        @Override
        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view;
            new ScrollToPositionViewAction(position).perform(uiController, view);
            uiController.loopMainThreadUntilIdle();
            @SuppressWarnings("unchecked")
            VH viewHolderForPosition = (VH) recyclerView.findViewHolderForPosition(position);
            if (null == viewHolderForPosition) {
                throw new PerformException.Builder().withActionDescription(this.toString())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new IllegalStateException("No view holder at position: " + position))
                        .build();
            }
            View viewAtPosition = viewHolderForPosition.itemView;
            if (null == viewAtPosition) {
                throw new PerformException.Builder().withActionDescription(this.toString())
                        .withViewDescription(HumanReadables.describe(viewAtPosition))
                        .withCause(new IllegalStateException("No view at position: " + position)).build();
            }
            viewAction.perform(uiController, viewAtPosition);
        }
    }

    // pulled from the Checks file: https://android.googlesource.com/platform/frameworks/testing/+/android-support-test/espresso/contrib/src/main/java/android/support/test/espresso/contrib/Checks.java

    /**
     * {@link ViewAction} which scrolls {@link RecyclerView} to the view matched by itemViewMatcher.
     * See {@link RecyclerViewActions#scrollTo(Matcher)} for more details.
     */
    private static final class ScrollToViewAction<VH extends ViewHolder> implements
            PositionableRecyclerViewAction {
        private final Matcher<VH> viewHolderMatcher;
        private final int atPosition;

        private ScrollToViewAction(Matcher<VH> viewHolderMatcher) {
            this(viewHolderMatcher, NO_POSITION);
        }

        private ScrollToViewAction(Matcher<VH> viewHolderMatcher, int atPosition) {
            this.viewHolderMatcher = viewHolderMatcher;
            this.atPosition = atPosition;
        }

        @Override
        public PositionableRecyclerViewAction atPosition(int position) {
            checkArgument(position >= 0, "%d is used as an index - must be >= 0", position);
            return new ScrollToViewAction<VH>(viewHolderMatcher, position);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Matcher<View> getConstraints() {
            return allOf(isAssignableFrom(RealmRecyclerView.class), isDisplayed());
        }

        @Override
        public String getDescription() {
            if (atPosition == NO_POSITION) {
                return "scroll RecyclerView to: " + viewHolderMatcher;
            } else {
                return String.format("scroll RecyclerView to the: %dth matching %s.", atPosition,
                        viewHolderMatcher);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rrv_recycler_view);
            try {
                int maxMatches = atPosition == NO_POSITION ? 2 : atPosition + 1;
                int selectIndex = atPosition == NO_POSITION ? 0 : atPosition;
                List<MatchedItem> matchedItems = itemsMatching(recyclerView, viewHolderMatcher, maxMatches);
                if (selectIndex >= matchedItems.size()) {
                    throw new RuntimeException(String.format(
                            "Found %d items matching %s, but position %d was requested.", matchedItems.size(),
                            viewHolderMatcher.toString(), atPosition));
                }
                if (atPosition == NO_POSITION && matchedItems.size() == 2) {
                    StringBuilder ambiguousViewError = new StringBuilder();
                    ambiguousViewError.append(
                            String.format("Found more than one sub-view matching %s", viewHolderMatcher));
                    for (MatchedItem item : matchedItems) {
                        ambiguousViewError.append(item + "\n");
                    }
                    throw new RuntimeException(ambiguousViewError.toString());
                }
                recyclerView.scrollToPosition(matchedItems.get(selectIndex).position);
                uiController.loopMainThreadUntilIdle();
            } catch (RuntimeException e) {
                throw new PerformException.Builder().withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view)).withCause(e).build();
            }
        }
    }

    /**
     * {@link ViewAction} which scrolls {@link RecyclerView} to a given position. See
     * {@link RecyclerViewActions#scrollToPosition(int)} for more details.
     */
    private static final class ScrollToPositionViewAction implements ViewAction {
        private final int position;

        private ScrollToPositionViewAction(int position) {
            this.position = position;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Matcher<View> getConstraints() {
            return allOf(isAssignableFrom(RealmRecyclerView.class), isDisplayed());
        }

        @Override
        public String getDescription() {
            return "scroll RecyclerView to position: " + position;
        }

        @Override
        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rrv_recycler_view);
            recyclerView.scrollToPosition(position);
        }
    }

    /**
     * Wrapper for matched items in recycler view which contains position and description of matched
     * view.
     */
    private static class MatchedItem {
        public final int position;
        public final String description;

        private MatchedItem(int position, String description) {
            this.position = position;
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}