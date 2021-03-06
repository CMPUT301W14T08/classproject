/*
 * Copyright 2014 Artem Chikin
 * Copyright 2014 Artem Herasymchuk
 * Copyright 2014 Tom Krywitsky
 * Copyright 2014 Henry Pabst
 * Copyright 2014 Bradley Simons
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.ualberta.cmput301w14t08.geochan.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import ca.ualberta.cmput301w14t08.geochan.R;

/**
 * Displays user's favourited threads/comments, provides navigation to each via
 * a spinner.
 * 
 * @author Artem Chikin
 * 
 */
public class FavouritesFragment extends Fragment implements ActionBar.OnNavigationListener,
        OnBackStackChangedListener {
    private static ActionBar actionBar;
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    /**
     * Set up the fragment, action bar and the spinner.
     * 
     * @param inflater The LayoutInflater used to inflate the fragment's UI.
     * @param container The parent View that the  fragment's UI is attached to.
     * @param savedInstanceState The previously saved state of the fragment.
     * @return The View for the fragment's UI.
     * 
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Get which counter requested this activity
        // Intent intent = getIntent();

        // Set up the action bar to show a dropdown list.
        actionBar = getActivity().getActionBar();
        enableSpinner();
        // Show the Up button in the action bar.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
        // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1,
                        new String[] { getString(R.string.threads_fav),
                                getString(R.string.comments_fav), }), this);
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    /**
     * Restore spinner state from savedInstanceState.
     * @param savedInstanceState The previously saved state of the fragment.
     * 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
                actionBar.setSelectedNavigationItem(savedInstanceState
                        .getInt(STATE_SELECTED_NAVIGATION_ITEM));
            }
        }
        getChildFragmentManager().addOnBackStackChangedListener(this);
    }

    /**
     * Save spinner selection.
     * 
     * @param outState The Bundle for data to be saved to.
     * 
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, actionBar.getSelectedNavigationIndex());
        super.onSaveInstanceState(outState);
    }

    /**
     * Disables the spinner.
     */
    @Override
    public void onPause() {
        super.onPause();
        disableSpinner();
    }
    
    /**
     * Calls onActivityResult on the PostFragment previously being used by the user. A
     * workaround for onActivityResult not being called on child fragments, so instead when
     * favourites are involved we call it on the parent fragment then delegate back to
     * the child fragment.
     * 
     * @param requestCode The requestCode given in startActivityForResult.
     * @param resultCode The code returned by the activity.
     * @param data Data returned by the activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager childManager = getChildFragmentManager();
        PostFragment postFrag = (PostFragment) childManager.findFragmentByTag("postFrag");
        postFrag.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * On spinner selection, inflate correct favourites fragment.
     * @param position The position of the navigation item.
     * @param id The ID of the navigation item.
     */
    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        switch (position) {
        case 0:
            Fragment thrFragment = new FavouriteThreadsFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, thrFragment, "favThrFragment").commit();
            break;
        case 1:
            Fragment cmtFragment = new FavouriteCommentsFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, cmtFragment, "favComFragment").commit();
            break;
        }
        return true;
    }

    /**
     * Enable/disable spinner if in fragment.
     */
    @Override
    public void onBackStackChanged() {
        Fragment f = getChildFragmentManager().findFragmentByTag("thread_view_fragment");
        if (f != null) {
            disableSpinner();
        } else {
            enableSpinner();
        }
    }

    /**
     * Enable the spinner.
     */
    private void enableSpinner() {
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    }

    /**
     * Disable the spinner.
     */
    private void disableSpinner() {
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }
}