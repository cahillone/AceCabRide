package ace.cab.ride;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConnectThread extends Thread {
	private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private ConnectedThread mConnectedThread;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private BluetoothDevice mDevice;
    
    private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
 
    public ConnectThread(BluetoothDevice device, BluetoothAdapter btAdapter, Handler btHandler) {
    	mBluetoothAdapter = btAdapter;
    	mHandler = btHandler;
    	mDevice = device;
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
        manageConnectedSocket(mmSocket, mHandler, mDevice);
    }
    
    public void write(byte[] out) {
    	mConnectedThread.write(out);
    }
 
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
    
    public void manageConnectedSocket(BluetoothSocket mmSocket, Handler mHandler, BluetoothDevice device){
    	if (mConnectedThread != null) {
    		mConnectedThread.cancel();
    	} 
    	
    	Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_DEVICE_NAME);
    	Bundle bundle = new Bundle();
    	bundle.putString(MainActivity.DEVICE_NAME, device.getName());
    	msg.setData(bundle);
    	mHandler.sendMessage(msg);
    	
    	Log.i("TAG", "manageConnectedSocket() device name: " + device.getName());
    	
    	Log.i("TAG", "handler ConnectThread: " + mHandler);
    	mConnectedThread = new ConnectedThread(mmSocket, mHandler);
    	mConnectedThread.start();
    }

}
