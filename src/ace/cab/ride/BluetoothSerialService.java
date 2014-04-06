package ace.cab.ride;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/*
public class BluetoothSerialService {
private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	// Member fields 
	private BluetoothAdapter mAdapter;
	private Handler mHandler;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;
	
	// Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    public synchronized void connect(BluetoothDevice device) {
    	// Cancel any thread attempting to make a connection
    	if (mState == STATE_CONNECTING) {
    		if (mConnectThread !=null) { 
    			mConnectThread.cancel();
    			mConnectThread = null;
    		}
    		
    		// Start the thread to connect with the given device
    		mConnectThread = new ConnectThread(device);
    		mConnectThread.start();
    		setState(STATE_CONNECTING);
    	}
    	
    }
    
    

    




}
*/
