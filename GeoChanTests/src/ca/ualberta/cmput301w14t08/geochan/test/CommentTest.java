package ca.ualberta.cmput301w14t08.geochan.test;

import java.util.Date;

import android.graphics.Picture;
import android.location.Location;
import android.location.LocationManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.cmput301w14t08.geochan.activities.MainActivity;
import ca.ualberta.cmput301w14t08.geochan.helpers.LocationListenerService;
import ca.ualberta.cmput301w14t08.geochan.helpers.SortTypes;
import ca.ualberta.cmput301w14t08.geochan.models.Comment;
import ca.ualberta.cmput301w14t08.geochan.models.GeoLocation;

public class CommentTest extends ActivityInstrumentationTestCase2<MainActivity> {
    
    private MainActivity activity;
    private LocationListenerService locationListenerService;
    
    public CommentTest(){
        super(MainActivity.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        //Shamelessly ripped from GeolocationTest to test Comments with Geolocations.
        super.setUp();
        this.activity = getActivity();
        locationListenerService = new LocationListenerService(activity);
        locationListenerService.startListening();
    }

    public void testHasImage() {
        Comment comment = new Comment("test", new Picture(), null);
        assertTrue("Comment has image", comment.hasImage());
    }

    @SuppressWarnings("unused")
    public void testAddChild() {
        Comment parent = new Comment("test", null);
        Comment reply = new Comment("test_reply", null, parent);
        assertNotNull("comment has a reply", parent.getChildren());
    }
    
    public void testConstruct() {
        Comment comment = new Comment("Hola", null);
        assertNull(comment.getParent());
    }
    
    public void testSortByDateNewest(){
        /*
         * Tests the implementation of Comment.sortChildren("DATE_NEWEST");
         */
        long extraTime = 1320000;
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        Comment c3 = new Comment();
        Comment c4 = new Comment();
        Comment c5 = new Comment();
        Date currentDate = new Date();
        c1.setCommentDate(new Date(currentDate.getTime() + 1*extraTime));
        c2.setCommentDate(new Date(currentDate.getTime() + 2*extraTime));
        c3.setCommentDate(new Date(currentDate.getTime() + 3*extraTime));
        c4.setCommentDate(new Date(currentDate.getTime() + 4*extraTime));
        c5.setCommentDate(new Date(currentDate.getTime() + 5*extraTime));
        
        c1.addChild(c5);
        c1.addChild(c3);
        c1.addChild(c4);
        c1.addChild(c2);
        
        c1.sortChildren(SortTypes.SORT_DATE_NEWEST);

        assertTrue("c5 is at index 0", (c1.getChildren().get(0)) == c5);
        assertTrue("c4 is at index 1", (c1.getChildren().get(1)) == c4);
        assertTrue("c3 is at index 2", (c1.getChildren().get(2)) == c3);
        assertTrue("c2 is at index 3", (c1.getChildren().get(3)) == c2);
    }
    
    public void testSortByDateOldest(){
        /*
         * Tests the implementation of Comment.sortChildren("DATE_OLDEST");
         */
        long extraTime = 1320000;
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        Comment c3 = new Comment();
        Comment c4 = new Comment();
        Comment c5 = new Comment();
        Date currentDate = new Date();
        c1.setCommentDate(new Date(currentDate.getTime() + 1*extraTime));
        c2.setCommentDate(new Date(currentDate.getTime() + 2*extraTime));
        c3.setCommentDate(new Date(currentDate.getTime() + 3*extraTime));
        c4.setCommentDate(new Date(currentDate.getTime() + 4*extraTime));
        c5.setCommentDate(new Date(currentDate.getTime() + 5*extraTime));
        
        c1.addChild(c5);
        c1.addChild(c3);
        c1.addChild(c4);
        c1.addChild(c2);
        
        c1.sortChildren(SortTypes.SORT_DATE_OLDEST);

        assertTrue("c2 is at index 0", (c1.getChildren().get(0)) == c2);
        assertTrue("c3 is at index 1", (c1.getChildren().get(1)) == c3);
        assertTrue("c4 is at index 2", (c1.getChildren().get(2)) == c4);
        assertTrue("c5 is at index 3", (c1.getChildren().get(3)) == c5);
    }
    
    public void testSortByParentDistance(){
        /*
         * Tests the implementation of Comment.sortChildren("LOCATION_OP");
         */
        
        Location location1 = new Location(LocationManager.GPS_PROVIDER);
        Location location2 = new Location(LocationManager.GPS_PROVIDER);
        Location location3 = new Location(LocationManager.GPS_PROVIDER);
        Location location4 = new Location(LocationManager.GPS_PROVIDER);
        Location location5 = new Location(LocationManager.GPS_PROVIDER);
        
        locationListenerService = new LocationListenerService(activity);
        
        GeoLocation geoLocation1 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation2 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation3 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation4 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation5 = new GeoLocation(locationListenerService);
        
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        Comment c3 = new Comment();
        Comment c4 = new Comment();
        Comment c5 = new Comment();
        
        c1.setLocation(geoLocation1);
        c2.setLocation(geoLocation2);
        c3.setLocation(geoLocation3);
        c4.setLocation(geoLocation4);
        c5.setLocation(geoLocation5);
        
        c1.getLocation().setLocation(location1);
        c2.getLocation().setLocation(location2);
        c3.getLocation().setLocation(location3);
        c4.getLocation().setLocation(location4);
        c5.getLocation().setLocation(location5);
        
        c2.setParent(c1);
        c3.setParent(c1);
        c4.setParent(c1);
        c5.setParent(c1);
       
        c1.getLocation().setCoordinates(0,0);
        c2.getLocation().setCoordinates(1,1);
        c3.getLocation().setCoordinates(2,2);
        c4.getLocation().setCoordinates(3,3);
        c5.getLocation().setCoordinates(4,4);

        c1.addChild(c5);
        c1.addChild(c3);
        c1.addChild(c4);
        c1.addChild(c2);
        
        c1.sortChildren(SortTypes.SORT_LOCATION_OP);

        assertTrue("c2 is at index 0", (c1.getChildren().get(0)) == c2);
        assertTrue("c3 is at index 1", (c1.getChildren().get(1)) == c3);
        assertTrue("c4 is at index 2", (c1.getChildren().get(2)) == c4);
        assertTrue("c5 is at index 3", (c1.getChildren().get(3)) == c5);
    }
    
    public void testSortByScoreHighest(){
        /*
         * Tests the implementation of Comment.sortChildren("PARENT_SCORE_HIGHEST");
         */
        long extraTime = 1320000;
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        Comment c3 = new Comment();
        Comment c4 = new Comment();
        Comment c5 = new Comment();
        
        Location location1 = new Location(LocationManager.GPS_PROVIDER);
        Location location2 = new Location(LocationManager.GPS_PROVIDER);
        Location location3 = new Location(LocationManager.GPS_PROVIDER);
        Location location4 = new Location(LocationManager.GPS_PROVIDER);
        Location location5 = new Location(LocationManager.GPS_PROVIDER);
        
        locationListenerService = new LocationListenerService(activity);
        
        GeoLocation geoLocation1 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation2 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation3 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation4 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation5 = new GeoLocation(locationListenerService);
        
        c1.setLocation(geoLocation1);
        c2.setLocation(geoLocation2);
        c3.setLocation(geoLocation3);
        c4.setLocation(geoLocation4);
        c5.setLocation(geoLocation5);
        
        c1.getLocation().setLocation(location1);
        c2.getLocation().setLocation(location2);
        c3.getLocation().setLocation(location3);
        c4.getLocation().setLocation(location4);
        c5.getLocation().setLocation(location5);
        
        c2.setParent(c1);
        c3.setParent(c1);
        c4.setParent(c1);
        c5.setParent(c1);
        
        c1.getLocation().setCoordinates(0,0);
        c2.getLocation().setCoordinates(1,1);
        c3.getLocation().setCoordinates(2,2);
        c4.getLocation().setCoordinates(3,3);
        c5.getLocation().setCoordinates(4,4);

        c1.addChild(c5);
        c1.addChild(c3);
        c1.addChild(c4);
        c1.addChild(c2);
        
        Date currentDate = new Date();
        
        c1.setCommentDate(currentDate);
        c2.setCommentDate(new Date(currentDate.getTime() + 20*extraTime));
        c3.setCommentDate(new Date(currentDate.getTime() + 30*extraTime));
        c4.setCommentDate(new Date(currentDate.getTime() + 40*extraTime));
        c5.setCommentDate(new Date(currentDate.getTime() + 50*extraTime));
        
        c1.sortChildren(SortTypes.SORT_SCORE_HIGHEST);

        assertTrue("c2 is at index 0", (c1.getChildren().get(0)) == c2);
        assertTrue("c3 is at index 1", (c1.getChildren().get(1)) == c3);
        assertTrue("c4 is at index 2", (c1.getChildren().get(2)) == c4);
        assertTrue("c5 is at index 3", (c1.getChildren().get(3)) == c5);
    }
    
    public void testSortByScoreLowest(){
        /*
         * Tests the implementation of Comment.sortChildren("SCORE_LOWEST");
         */
        long extraTime = 1320000;
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        Comment c3 = new Comment();
        Comment c4 = new Comment();
        Comment c5 = new Comment();
        
        Location location1 = new Location(LocationManager.GPS_PROVIDER);
        Location location2 = new Location(LocationManager.GPS_PROVIDER);
        Location location3 = new Location(LocationManager.GPS_PROVIDER);
        Location location4 = new Location(LocationManager.GPS_PROVIDER);
        Location location5 = new Location(LocationManager.GPS_PROVIDER);
        
        locationListenerService = new LocationListenerService(activity);
        
        GeoLocation geoLocation1 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation2 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation3 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation4 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation5 = new GeoLocation(locationListenerService);
        
        c1.setLocation(geoLocation1);
        c2.setLocation(geoLocation2);
        c3.setLocation(geoLocation3);
        c4.setLocation(geoLocation4);
        c5.setLocation(geoLocation5);
        
        c1.getLocation().setLocation(location1);
        c2.getLocation().setLocation(location2);
        c3.getLocation().setLocation(location3);
        c4.getLocation().setLocation(location4);
        c5.getLocation().setLocation(location5);
        
        c2.setParent(c1);
        c3.setParent(c1);
        c4.setParent(c1);
        c5.setParent(c1);
        
        c1.getLocation().setCoordinates(0,0);
        c2.getLocation().setCoordinates(1,1);
        c3.getLocation().setCoordinates(2,2);
        c4.getLocation().setCoordinates(3,3);
        c5.getLocation().setCoordinates(4,4);
        
        Date currentDate = new Date();
        
        c1.setCommentDate(currentDate);
        c2.setCommentDate(new Date(currentDate.getTime() + 2*extraTime));
        c3.setCommentDate(new Date(currentDate.getTime() + 3*extraTime));
        c4.setCommentDate(new Date(currentDate.getTime() + 4*extraTime));
        c5.setCommentDate(new Date(currentDate.getTime() + 5*extraTime));

        c1.addChild(c5);
        c1.addChild(c3);
        c1.addChild(c4);
        c1.addChild(c2);
        
        c1.setCommentDate(currentDate);
        c2.setCommentDate(new Date(currentDate.getTime() + 20*extraTime));
        c3.setCommentDate(new Date(currentDate.getTime() + 30*extraTime));
        c4.setCommentDate(new Date(currentDate.getTime() + 40*extraTime));
        c5.setCommentDate(new Date(currentDate.getTime() + 50*extraTime));
        
        c1.sortChildren(SortTypes.SORT_SCORE_LOWEST);

        assertTrue("c5 is at index 0", (c1.getChildren().get(0)) == c5);
        assertTrue("c4 is at index 1", (c1.getChildren().get(1)) == c4);
        assertTrue("c3 is at index 2", (c1.getChildren().get(2)) == c3);
        assertTrue("c2 is at index 3", (c1.getChildren().get(3)) == c2);
    }
    
    /**
     * Tests the calculation of comment scores in relation to their parent.
     */
    public void testGetParentScore(){
        /*
         * Test the score calculation for child comments.
         */
        long extraTime = 1320000;
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        Comment c3 = new Comment();
        Comment c4 = new Comment();
        Comment c5 = new Comment();
        
        Location location1 = new Location(LocationManager.GPS_PROVIDER);
        Location location2 = new Location(LocationManager.GPS_PROVIDER);
        Location location3 = new Location(LocationManager.GPS_PROVIDER);
        Location location4 = new Location(LocationManager.GPS_PROVIDER);
        Location location5 = new Location(LocationManager.GPS_PROVIDER);
        
        locationListenerService = new LocationListenerService(activity);
        
        GeoLocation geoLocation1 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation2 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation3 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation4 = new GeoLocation(locationListenerService);
        GeoLocation geoLocation5 = new GeoLocation(locationListenerService);
        
        c1.setLocation(geoLocation1);
        c2.setLocation(geoLocation2);
        c3.setLocation(geoLocation3);
        c4.setLocation(geoLocation4);
        c5.setLocation(geoLocation5);
        
        c1.getLocation().setLocation(location1);
        c2.getLocation().setLocation(location2);
        c3.getLocation().setLocation(location3);
        c4.getLocation().setLocation(location4);
        c5.getLocation().setLocation(location5);
        

        c1.getLocation().setCoordinates(53.526802,-113.527170);
        c2.getLocation().setCoordinates(53.523636,-113.527437);
        c3.getLocation().setCoordinates(53.527047,-113.525662);

        c2.setParent(c1);
        c3.setParent(c1);
        c4.setParent(c1);
        c5.setParent(c1);
        

        c1.addChild(c5);
        c1.addChild(c3);
        c1.addChild(c4);
        c1.addChild(c2);
        
        Date currentDate = new Date();
        
        c1.setCommentDate(currentDate);
        c2.setCommentDate(new Date(currentDate.getTime() + 20*extraTime));
        c3.setCommentDate(new Date(currentDate.getTime() + 30*extraTime));
        c4.setCommentDate(new Date(currentDate.getTime() + 40*extraTime));
        c5.setCommentDate(new Date(currentDate.getTime() + 50*extraTime));

        assertTrue("c5 is > 0", c5.getScoreFromParent() > 0);
        assertTrue("c4 is > 0", c4.getScoreFromParent() > 0);
        assertTrue("c3 is > 0", c3.getScoreFromParent() > 0);
        assertTrue("c2 is > 0", c2.getScoreFromParent() > 0);
    }
    
    public void testGetUserScore(){
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        GeoLocation g1 = new GeoLocation(0,0);
        
        c1.getLocation().setCoordinates(0, 0);
        c2.getLocation().setCoordinates(5, 5);
        
        assertTrue("Scores are correct relatively.", 
                    c1.getScoreFromUser(g1) > c2.getScoreFromUser(g1));
    }
    
    public void testGetDistanceFrom(){
        Comment c1 = new Comment();
        GeoLocation g1 = new GeoLocation(5,5);
        
        c1.getLocation().setCoordinates(0,0);
        
        double dist = c1.getDistanceFrom(g1);
        
        Log.e("Value of dist:", String.valueOf(dist));
        assertTrue("Distance calculated correctly.", dist == Math.sqrt(50));
    }
    
    public void testGetTimeFrom(){
        Comment c1 = new Comment();
        Date d1 = new Date();
        
        assertEquals("Returns minimum 0.5:", c1.getTimeFrom(d1), 0.5);
        
        d1 = new Date(c1.getCommentDate().getTime() + 3600000);
        
        assertEquals("Returns correct hour amount.", c1.getTimeFrom(d1), 1.0);
    }
}
