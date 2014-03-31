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

import java.math.RoundingMode;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cmput301w14t08.geochan.R;
import ca.ualberta.cmput301w14t08.geochan.elasticsearch.ElasticSearchClient;
import ca.ualberta.cmput301w14t08.geochan.helpers.ErrorDialog;
import ca.ualberta.cmput301w14t08.geochan.helpers.LocationListenerService;
import ca.ualberta.cmput301w14t08.geochan.models.Comment;
import ca.ualberta.cmput301w14t08.geochan.models.GeoLocation;
import ca.ualberta.cmput301w14t08.geochan.models.GeoLocationLog;
import ca.ualberta.cmput301w14t08.geochan.models.ThreadComment;

/**
 * Responsible for the UI fragment that allows a user to post a new thread.
 * 
 * @author AUTHOR HERE
 */
public class PostThreadFragment extends Fragment {
    private LocationListenerService locationListenerService;
    private GeoLocation geoLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        return inflater.inflate(R.layout.fragment_post_thread, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        locationListenerService = new LocationListenerService(getActivity());
        locationListenerService.startListening();
        geoLocation = new GeoLocation(locationListenerService);
    }

    /**
     * COMMENT GOES HERE
     */
    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("LATITUDE") && args.containsKey("LONGITUDE")) {
                Button locButton = (Button) getActivity().findViewById(R.id.thread_location_button);
                if (args.getString("LocationType") == "CURRENT_LOCATION") {
                    locButton.setText("Current Location");
                } else {
                    Double lat = args.getDouble("LATITUDE");
                    Double lon = args.getDouble("LONGITUDE");
                    geoLocation.setCoordinates(lat, lon);
                    
                    String locationDescription = args.getString("locationDescription");
                    geoLocation.setLocationDescription(locationDescription);

                    DecimalFormat format = new DecimalFormat();
                    format.setRoundingMode(RoundingMode.HALF_EVEN);
                    format.setMinimumFractionDigits(0);
                    format.setMaximumFractionDigits(4);

                    if (locationDescription.equals("Unknown Location")) {
                        locButton.setText("Lat: " + format.format(lat) + ", Long: "
                                + format.format(lon));
                    } else {
                        locButton.setText("Location: " + locationDescription);
                    }
                }
            }
        }
    }

    /**
     * COMMENT GOES HERE
     * 
     * @param v
     *            WHAT DOTH V?
     */
    public void postNewThread(View v) {
        if (v.getId() == R.id.post_thread_button) {
            EditText editTitle = (EditText) this.getView().findViewById(R.id.titlePrompt);
            EditText editComment = (EditText) this.getView().findViewById(R.id.commentBody);
            String title = editTitle.getText().toString();
            String comment = editComment.getText().toString();
            if (title.equals("")) {
                ErrorDialog.show(getActivity(), "Title can not be left blank.");
            } else {
                if (geoLocation.getLocation() == null) {
                    // ErrorDialog.show(getActivity(),
                    // "Could not obtain location.");
                    // Create a new comment object and set username
                    Comment newComment = new Comment(comment, null);
                    // ThreadList.addThread(newComment, title);
                    ElasticSearchClient client = ElasticSearchClient.getInstance();
                    client.postThread(new ThreadComment(newComment, title));
                } else {
                    // Create a new comment object and set username
                    Comment newComment = new Comment(comment, geoLocation);
                    // ThreadList.addThread(newComment, title);
                    ElasticSearchClient client = ElasticSearchClient.getInstance();
                    client.postThread(new ThreadComment(newComment, title));
                    // log the thread and the geolocation
                    GeoLocationLog geoLocationLog = GeoLocationLog.getInstance(getActivity());
                    geoLocationLog.addLogEntry(title, geoLocation);
                }
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                this.getFragmentManager().popBackStackImmediate();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        locationListenerService.stopListening();
    }
}
