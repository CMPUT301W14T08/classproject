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

package ca.ualberta.cmput301w14t08.geochan.managers;

import ca.ualberta.cmput301w14t08.geochan.helpers.SortUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;

/**
 * Helps manage relevant preferences within the application.
 * 
 * @author Artem Herasymchuk
 */
public class PreferencesManager {
    private static Context context;
    private static SharedPreferences preferences;
    private static PreferencesManager instance = null;

    protected PreferencesManager(Context _context) {
        context = _context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Generate a Singleton instance of the PreferencesManager
     * 
     * @param _context
     *            the application context
     */
    public static void generateInstance(Context _context) {
        instance = new PreferencesManager(_context);
    }

    /**
     * Returns the Singleton instance
     * 
     * @return the instance
     */
    public static PreferencesManager getInstance() {
        return instance;
    }

    /**
     * Retrieves the username as a string
     * 
     * @return the username
     */
    public String getUser() {
        return preferences.getString("username", "Anon");
    }

    /**
     * Retrieves the Android ID as a string
     * 
     * @return the Android ID
     */
    public String getId() {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * Returns the thread sort type as an integer
     * 
     * @return the thread sort type
     */
    public int getThreadSort() {
        return preferences.getInt("thread_sort", SortUtil.SORT_DATE_NEWEST);
    }

    /**
     * Chanes the ThreadComment sort type.
     * 
     * @param sortMethod
     *            The new ThreadComment sorting method.
     */
    public void setThreadSort(int sortMethod) {
        Editor editor = preferences.edit();
        editor.putInt("thread_sort", sortMethod);
        editor.commit();
    }

    /**
     * Returns the comment sort type as an integer
     * 
     * @return the comment sort type
     */
    public int getCommentSort() {
        return preferences.getInt("comment_sort", SortUtil.SORT_DATE_NEWEST);
    }

    /**
     * Changes the comment sort type.
     * 
     * @param sortMethod
     *            The new comment sorting type.
     */
    public void setCommentSort(int sortMethod) {
        Editor editor = preferences.edit();
        editor.putInt("comment_sort", sortMethod);
        editor.commit();
    }
}
