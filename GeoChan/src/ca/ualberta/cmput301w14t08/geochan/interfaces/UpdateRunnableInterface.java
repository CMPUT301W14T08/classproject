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

package ca.ualberta.cmput301w14t08.geochan.interfaces;

/**
 * Provides an interface for a Runnable that updates a list of Comments
 * on ElasticSearch.
 * @author Artem Herasymchuk
 *
 */
public interface UpdateRunnableInterface {

    /** 
     * Handles the various possible states of the
     * Runnable that update the comment list.
     * @param state the state
     */
    void handleUpdateState(int state);
    
    /* Getters and setters */
    
    void setUpdateThread(Thread thread);

}
