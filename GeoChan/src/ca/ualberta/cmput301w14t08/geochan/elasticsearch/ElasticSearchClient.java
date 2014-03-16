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

package ca.ualberta.cmput301w14t08.geochan.elasticsearch;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.ClientConfig;
import io.searchbox.core.Count;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.Update;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import ca.ualberta.cmput301w14t08.geochan.models.Comment;
import ca.ualberta.cmput301w14t08.geochan.models.ThreadComment;
import ca.ualberta.cmput301w14t08.geochan.serializers.CommentDeserializer;
import ca.ualberta.cmput301w14t08.geochan.serializers.CommentSerializer;
import ca.ualberta.cmput301w14t08.geochan.serializers.ThreadCommentDeserializer;
import ca.ualberta.cmput301w14t08.geochan.serializers.ThreadCommentSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ElasticSearchClient {
    private static ElasticSearchClient instance = null;
    private static Gson gson;
    private static JestClient client;
    private static final String TYPE_COMMENT = "geoComment";
    private static final String TYPE_THREAD = "geoThread";
    private static final String TYPE_INDEX = "geoCommentIndex";
    private static final String URL = "http://cmput301.softwareprocess.es:8080";
    private static final String URL_INDEX = "testing";

    private ElasticSearchClient() {
        ClientConfig config = new ClientConfig.Builder(URL).multiThreaded(true).build();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Comment.class, new CommentSerializer());
        builder.registerTypeAdapter(Comment.class, new CommentDeserializer());
        builder.registerTypeAdapter(ThreadComment.class, new ThreadCommentSerializer());
        builder.registerTypeAdapter(ThreadComment.class, new ThreadCommentDeserializer());
        gson = builder.create();
        JestClientFactory factory = new JestClientFactory();
        factory.setClientConfig(config);
        client = factory.getObject();
    }

    public static ElasticSearchClient getInstance() {
        return instance;
    }

    public static void generateInstance() {
        if (instance == null) {
            instance = new ElasticSearchClient();
        }
    }

    public Thread postThread(final ThreadComment thread) {
        Thread t = new Thread() {
            @Override
            public void run() {
                post(gson.toJson(thread), TYPE_THREAD, thread.getId());
            }
        };
        t.start();
        return t;
    }

    public Thread postComment(final ThreadComment thread, final Comment commentToReplyTo,
            final Comment comment) {
        Thread t = new Thread() {
            @Override
            public void run() {
                post(gson.toJson(comment), TYPE_COMMENT, thread.getId());
                String query = ElasticSearchQueries.commentListScript(comment.getId());
                update(query, TYPE_INDEX, commentToReplyTo.getId());
            }
        };
        t.start();
        return t;
    }

    public int getThreadCount() {
        return count(TYPE_THREAD);
    }

    public int getCommentCount() {
        return count(TYPE_COMMENT);
    }

    public ArrayList<ThreadComment> getThreads() {
        Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<ThreadComment>>() {
        }.getType();
        ElasticSearchSearchResponse<ThreadComment> esResponse = gson
                .fromJson(searchAll(ElasticSearchQueries.SEARCH_MATCH_ALL, TYPE_THREAD),
                elasticSearchSearchResponseType);
        ArrayList<ThreadComment> list = new ArrayList<ThreadComment>();
        for (ElasticSearchResponse<ThreadComment> r : esResponse.getHits()) {
            ThreadComment object = r.getSource();
            list.add(object);
        }
        return list;
    }
    
    public ArrayList<Comment> getComments(Comment topComment) {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        Get get = new Get.Builder(URL_INDEX, topComment.getId()).type(TYPE_INDEX).build();
        JestResult result = null;
        try {
            result = client.execute(get);
            Type type = new TypeToken<Collection<String>>() {}.getType();
            Collection<String> hits = gson.fromJson(result.getJsonString(), type);
            for (String hit : hits) {
                comments.add(get(hit));
            }
            for (Comment comment : comments) {
                comment.setParent(topComment);
                comment.setChildren(getComments(comment));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return comments;
    }
    
    private Comment get(final String id) {
        Get get = new Get.Builder(URL_INDEX, id).type(TYPE_COMMENT).build();
        JestResult result = null;
        try {
            result = client.execute(get);
            Type type = new TypeToken<Comment>() {}.getType();
            return gson.fromJson(result.getJsonString(), type);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
    private void post(final String json, final String type, final String id) {
        Index index = new Index.Builder(json).index(URL_INDEX).type(type)
                .id(id).build();
        try {
            client.execute(index);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void update(final String query, final String type, final String id) {
        Update update = new Update.Builder(query).index(URL_INDEX).type(type).id(id).build();
        try {
            client.execute(update);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private int count(final String type) {
        Count count = new Count.Builder().addIndex(URL_INDEX).addType(type).build();
        JestResult result = null;
        try {
            result = client.execute(count);
            Type elasticSearchCountResponseType = new TypeToken<ElasticSearchCountResponse>() {
            }.getType();
            ElasticSearchCountResponse esResponse = gson.fromJson(result.getJsonString(),
                    elasticSearchCountResponseType);
            return esResponse.getCount();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }
    
    private String searchAll(final String query, final String type) {
        Search search = new Search.Builder(query).addIndex(URL_INDEX).addType(type).build();
        JestResult result = null;
        try {
            result = client.execute(search);
            return result.getJsonString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}