package ca.ualberta.cmput301w14t08.geochan.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301w14t08.geochan.activities.MainActivity;
import ca.ualberta.cmput301w14t08.geochan.helpers.ConnectivityBroadcastReceiver;

public class ConnectivityListenerServiceTest extends ActivityInstrumentationTestCase2<MainActivity> {
    
    public ConnectivityListenerServiceTest() {
        super(MainActivity.class);
    }
    /*
    public void testConstruction() {
        ConnectivityListenerService connectivityListenerService = 
                new ConnectivityListenerService();
        assertNotNull(connectivityListenerService);
        assertNotNull(connectivityListenerService.getConnectivityManager());
    }
    
    public void testIsConnectedToWifi() {
        ConnectivityListenerService connectivityListenerService = 
                new ConnectivityListenerService();
        assertEquals("Wifi should be enabled", true, connectivityListenerService.isWifi());        
    }
    
    public void testIsConnectedToMobile() {
        ConnectivityListenerService connectivityListenerService = 
                new ConnectivityListenerService();
        assertEquals("Mobile should be enabled", true, connectivityListenerService.isMobile());   
    }
    
    public void testIsConnected() {
        ConnectivityListenerService connectivityListenerService = 
                new ConnectivityListenerService();
        assertEquals("Connection should be active", true, connectivityListenerService.isConnected());
    }
    
    public void testOnReceive() {
        ConnectivityListenerService connectivityListenerService = 
                new ConnectivityListenerService();
        Intent intent = new Intent();
        connectivityListenerService.onReceive(getActivity(), intent);
    }
    */
}
