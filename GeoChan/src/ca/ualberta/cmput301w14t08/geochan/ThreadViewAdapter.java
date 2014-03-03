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

package ca.ualberta.cmput301w14t08.geochan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ThreadViewAdapter extends BaseAdapter {
    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_OP = 1;
    private static final int TYPE_SEPARATOR = 2;
    private static final int TYPE_MAX_COUNT = 3;
    
    private Context context;
    private Thread thread;
    private ArrayList<Comment> comments;
    
    public ThreadViewAdapter(Context context, Thread thread) {
        super();
        this.context = context;
        this.thread = thread;
        this.comments = thread.getComments();
    }

    @Override
    public int getCount() {
        /**
         * +2 is for the OP
         */
        return thread.getComments().size()+2;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return thread.getBodyComment();
        }
        if (position == 1) {
            return null;
        } 
        else {
            return comments.get(position-2);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (position== 0){
            type = TYPE_OP;
        }
        
        else if (position == 1){
            type = TYPE_SEPARATOR;
        } 
        
        else if (position > 1) {
            type = TYPE_COMMENT;
        }
        return type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override 
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch(type) {
                case TYPE_OP:
                    if(!thread.getBodyComment().hasImage()) {
                        convertView = inflater.inflate(R.layout.thread_view_op, null);
                    } else {
                        convertView = inflater.inflate(R.layout.thread_view_op_img, null);
                    }
                    // Thread title
                    TextView title = (TextView) convertView.findViewById(R.id.thread_view_op_threadTitle);
                    title.setText(thread.getTitle()); 
                    // Thread body comment
                    TextView body = (TextView) convertView.findViewById(R.id.thread_view_op_commentBody);
                    body.setText(thread.getBodyComment().getTextPost());
                    // Thread timestamp
                    TextView time = (TextView) convertView.findViewById(R.id.thread_view_op_commentDate);
                    time.setText(makeCommentTimeString(thread.getBodyComment()));
                    // Location text
                    TextView origPostLocationText = (TextView) convertView.findViewById(R.id.thread_view_op_locationText);
                    GeoLocation loc = thread.getBodyComment().getLocation();
                    if (loc != null) {
                        double origPostLat = Math.round(loc.getLatitude() * 100)/100;
                        double origPostLong = Math.round(loc.getLongitude() * 100)/100;
                        origPostLocationText.setText("Latitude: " + Double.toString(origPostLat) +
                                        " Longitude: " + Double.toString(origPostLong));
                    } else {
                        origPostLocationText.setText("Error: No location found");
                    }
                    break;
                case TYPE_COMMENT:
                    Comment comment = (Comment) getItem(position);
                    convertView = inflater.inflate(R.layout.thread_view_top_comment, null);
                    TextView commentBody = (TextView) convertView.findViewById(R.id.thread_view_top_comment_commentBody);
                    commentBody.setText(comment.getTextPost());    
                    TextView commentLocationText = (TextView) convertView.findViewById(R.id.thread_view_top_comment_locationText);
                    GeoLocation locCom = comment.getLocation();
                    if (locCom != null) {
                        double commentLat = Math.round(locCom.getLatitude() * 100)/100;
                        double commentLong = Math.round(locCom.getLongitude() * 100)/100;
                        commentLocationText.setText("Latitude: " + Double.toString(commentLat) +
                                        " Longitude: " + Double.toString(commentLong));
                    } else {
                        commentLocationText.setText("Error: No location found");
                    }
                    break;
                case TYPE_SEPARATOR:
                    convertView = inflater.inflate(R.layout.thread_view_separator, null);
                    TextView numComments = (TextView) convertView.findViewById(R.id.textSeparator);
                    numComments.setText(Integer.toString(comments.size()) + " Comments:");
                    break;
            }
        }
        return convertView;
    }
    
    public String makeCommentTimeString(Comment comment) {
        Date date = comment.getCommentDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        String ret = " | on " 
                + cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.CANADA) 
                + "." + cal.get(Calendar.DATE)
                + "," + cal.get(Calendar.YEAR) 
                + " at " + cal.get(Calendar.HOUR_OF_DAY)
                + ":" + cal.get(Calendar.MINUTE);
        return ret;
    }
    
    // TODO 
    public void addTopComment() {
        
        notifyDataSetChanged();
    }
}
