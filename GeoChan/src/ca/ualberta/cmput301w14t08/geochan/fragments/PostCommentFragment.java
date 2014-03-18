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

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.cmput301w14t08.geochan.R;
import ca.ualberta.cmput301w14t08.geochan.elasticsearch.ElasticSearchClient;
import ca.ualberta.cmput301w14t08.geochan.helpers.ImageHelper;
import ca.ualberta.cmput301w14t08.geochan.helpers.LocationListenerService;
import ca.ualberta.cmput301w14t08.geochan.helpers.UserHashManager;
import ca.ualberta.cmput301w14t08.geochan.models.Comment;
import ca.ualberta.cmput301w14t08.geochan.models.GeoLocation;
import ca.ualberta.cmput301w14t08.geochan.models.GeoLocationLog;
import ca.ualberta.cmput301w14t08.geochan.models.ThreadComment;
import ca.ualberta.cmput301w14t08.geochan.models.ThreadList;

/**
 * Responsible for the UI fragment that allows a user to post a reply to a
 * thread.
 */
public class PostCommentFragment extends Fragment {

    private ThreadComment thread;
    private LocationListenerService locationListenerService;
    private GeoLocation geoLocation;
    private ImageHelper imageHelper;
    private Picture picture;
    private Picture thumb;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        return inflater.inflate(R.layout.fragment_post_comment, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        thread = ThreadList.getThreads().get((int) bundle.getLong("id"));
        TextView titleView = (TextView) getActivity().findViewById(R.id.op_title);
        TextView bodyView = (TextView) getActivity().findViewById(R.id.op_body);
        bodyView.setMovementMethod(new ScrollingMovementMethod());
        titleView.setText(thread.getTitle());
        bodyView.setText(thread.getBodyComment().getTextPost());
        imageView = (ImageView) getActivity().findViewById(R.id.imageView);
        locationListenerService = new LocationListenerService(getActivity());
        locationListenerService.startListening();
        geoLocation = new GeoLocation(locationListenerService);
        imageHelper = new ImageHelper(getActivity());
        
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("LATITUDE") && args.containsKey("LONGITUDE")) {
                geoLocation.setCoordinates(args.getDouble("LATITUDE"),args.getDouble("LONGITUDE"));
            }
        }
    }

    public void postComment(View v) {
        if (v.getId() == R.id.post_comment_button) {
            EditText editComment = (EditText) this.getView().findViewById(R.id.commentBody);
            String comment = editComment.getText().toString();
            if (geoLocation.getLocation() == null) {
                // ErrorDialog.show(getActivity(),
                // "Could not obtain location.");
                // Create a new comment object and set username
                Comment newComment = new Comment(comment, null, thread.getBodyComment());
                ElasticSearchClient client = ElasticSearchClient.getInstance();
                client.postComment(thread, thread.getBodyComment(), newComment);
            } else {
                // Create a new comment object and set username
                Comment newComment = new Comment(comment, geoLocation, thread.getBodyComment());
                ElasticSearchClient client = ElasticSearchClient.getInstance();
                client.postComment(thread, thread.getBodyComment(), newComment);
                // log the location and thread title
                GeoLocationLog.addLogEntry(thread.getTitle(), geoLocation);
                Log.e("size of locLog:",
                        Integer.toString(GeoLocationLog.getLogEntries().size()));
            }
            
            /* RIGHT NOW THIS BLOCK CAUSES A CRASH
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);*/
            
            this.getFragmentManager().popBackStackImmediate();
        }
    }
    
    public void attachImage(View v) {
        if (v.getId() == R.id.attach_image_button) {
            imageHelper.captureImage();
            //picture = imageHelper.getPicture();
            //thumb = imageHelper.getThumbnail();
        }
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            Log.d("imagehelper","Image set successfully");
        //}
    }
    

    public String retrieveUsername() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        UserHashManager manager = UserHashManager.getInstance();
        return preferences.getString("username", "Anon") + "#" + manager.getHash();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationListenerService.stopListening();
    }
}
