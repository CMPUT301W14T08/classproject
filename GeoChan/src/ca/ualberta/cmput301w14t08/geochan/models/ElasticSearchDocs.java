package ca.ualberta.cmput301w14t08.geochan.models;

import java.util.Collection;

public class ElasticSearchDocs<T> {
    Collection<ElasticSearchResponse<T>> docs;
    
    public Collection<ElasticSearchResponse<T>> getDocs() {
        return docs;
    }
}
