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

package ca.ualberta.cmput301w14t08.geochan.serializers;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import ca.ualberta.cmput301w14t08.geochan.models.Comment;
import ca.ualberta.cmput301w14t08.geochan.models.GeoLocation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Handles the deserialization of Comments from JSON.
 * 
 */
public class CommentDeserializer implements JsonDeserializer<Comment> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement,
     * java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    /**
     * Deserializes a Comment object from JSON format.
     */
    @Override
    public Comment deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        long commentDate = object.get("commentDate").getAsLong();
        boolean hasImage = object.get("hasImage").getAsBoolean();
        String locationString = object.get("location").getAsString();
        List<String> locationEntries = Arrays.asList(locationString.split(","));
        double latitude = Double.parseDouble(locationEntries.get(0));
        double longitude = Double.parseDouble(locationEntries.get(1));
        String user = object.get("user").getAsString();
        String hash = object.get("hash").getAsString();
        String textPost = object.get("textPost").getAsString();
        String id = object.get("id").getAsString();
        String locationDescription = null;
        if (object.get("locationDescription") != null) {
            locationDescription = object.get("locationDescription").getAsString();
        }
        Bitmap image = null;
        Bitmap thumbnail = null;
        if (hasImage) {
            /*
             * http://stackoverflow.com/questions/20594833/convert-byte-array-or-
             * bitmap-to-picture
             */
            String encodedImage = object.get("image").getAsString();
            byte[] byteArray = Base64.decode(encodedImage, Base64.NO_WRAP);

            // http://stackoverflow.com/a/5878773
            // Sando's workaround for running out of memory on decoding bitmaps.
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inDither = false; // Disable Dithering mode
            opts.inPurgeable = true; // Tell to gc that whether it needs free
                                     // memory, the Bitmap can be cleared
            opts.inInputShareable = true; // Which kind of reference will be
                                          // used to recover the Bitmap data
                                          // after being clear, when it will be
                                          // used in the future
            opts.inTempStorage = new byte[32 * 1024];

            image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, opts);

            String encodedThumb = object.get("imageThumbnail").getAsString();
            byte[] thumbArray = Base64.decode(encodedThumb, Base64.NO_WRAP);
            thumbnail = BitmapFactory.decodeByteArray(thumbArray, 0, thumbArray.length, opts);
        }
        int depth = object.get("depth").getAsInt();
        // String parent = object.get("parent").getAsString();
        GeoLocation location = new GeoLocation(latitude, longitude);
        location.setLocationDescription(locationDescription);
        final Comment comment = new Comment(textPost, location);
        comment.getCommentDate().setTime(commentDate);
        comment.setUser(user);
        comment.setHash(hash);
        comment.setDepth(depth);
        comment.setId(Long.parseLong(id));
        if (hasImage) {
            comment.setImage(image);
            comment.setImageThumb(thumbnail);
        }
        return comment;
    }
}
