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

import java.util.Comparator;

public class SortComparators {

        /*
         * Comparator for pushing old comments to the top.
         */
        static Comparator<Comment> sortCommentsByDateOldest() {
            return new Comparator<Comment>() {
                public int compare(Comment c1, Comment c2) {
                    int val = c1.getCommentDate().compareTo(c2.getCommentDate());
                    if (val < 0) {
                        return -1;
                    } else if (val > 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
           };
        }
        
        /*
         * Comparator for pushing new comments to the top.
         */
        static Comparator<Comment> sortCommentsByDateNewest() {
            return new Comparator<Comment>() {
                public int compare(Comment c1, Comment c2) {
                    int val = c1.getCommentDate().compareTo(c2.getCommentDate());
                    if (val < 0) {
                        return 1;
                    } else if (val > 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
           };
        }
        
        //sort by nearest location **comments will need to calculate distance from user
        /*
        static Comparator<Comment> sortByLocation() {
            return new Comparator<Comment>() {
                public int compare(Comment c1, Comment c2) {
                    int val = c1.getDistFromTop() - c2.getDistFromTop();
                    if (val < 0) {
                        return -1;
                    } else if (val > 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            };
        }
        */
        
        //sort by combination of date and location
}
