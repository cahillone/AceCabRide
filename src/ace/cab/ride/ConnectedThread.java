package ace.cab.ride;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConnectedThread extends Thread {
	private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private int RECEIVE_MESSAGE = 1;
    private Handler mHandler;
    public Handler SendToHandler;
    Message msg;
    
    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
 
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
 
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }
 
    public void run() {
    	Log.i("TAG", "BEGIN mConnectedThread");
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
 
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                Log.i("TAG", "Rx buffer: " + buffer.toString());
                Log.i("TAG", "Rx buffer bytes: " + bytes);
                Log.i("TAG", "Rx buffer0: " + buffer[0]);
                Log.i("TAG", "Rx buffer1: " + buffer[1]);
                Log.i("TAG", "Rx buffer2: " + buffer[2]);
                Log.i("TAG", "Rx buffer3: " + buffer[3]);
                Log.i("TAG", "Rx buffer4: " + buffer[4]);
                // Send the obtained bytes to the UI activity
                msg = mHandler.obtainMessage(RECEIVE_MESSAGE, bytes, -1, buffer);
                SendToHandler = msg.getTarget();
                msg.sendToTarget();
            } catch (Exception e) {
            	Log.e("TAG", "ConnectedThread.run() error: " + e);
                break;
            }
        }
    }
 
    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }
 
    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}
