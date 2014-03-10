package ca.ualberta.cmput301w14t08.geochan.elasticsearch;

public class ElasticSearchQueries {
	public static final String QUERY_SEARCH_MATCH_ALL = "{\n" + 
            "   \"query\": {\n" +
            "       \"match_all\" : { } \n" +
            "   }\n" +
            "}";
	
	private static final String QUERY_SEARCH_MATCH_PARENT_BEGIN = "{\n" + 
            "   \"query\": {\n" +
            "       \"match\" : {\n" +
            "           \"parent\" : \"";
	
	private static final String QUERY_SEARCH_MATCH_PARENT_END = "\" \n" + 
            "       }\n" +
            "   }\n" +
            "}";
	
	public static String getMatchParent(String id) {
		return QUERY_SEARCH_MATCH_PARENT_BEGIN + id + QUERY_SEARCH_MATCH_PARENT_END;
	}
}
