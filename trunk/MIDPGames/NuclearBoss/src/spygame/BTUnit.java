package spygame;
/*
 * BTUnit.java
 *
 * Created on 25. èerven 2006, 20:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author PC
 */
public class BTUnit extends Canvas {
        
    // Bluetooth Service name
    private static final String myServiceName = "MyBtService";
    // Bluetooth Service UUID of interest
    private static final String myServiceUUID = "2d26618601fb47c28d9f10b8ec891363";
    private javax.bluetooth.UUID MYSERVICEUUID_UUID = new javax.bluetooth.UUID(myServiceUUID, false);
    
    /** Creates a new instance of BTUnit */
    public BTUnit() {
        try {
            initBluetooth();
            println("initialization completed");
        } catch (BluetoothStateException ex) {
            ex.printStackTrace();
        }

    }
    
    Form status = new Form("Bluetooth process");
    
    String text="initing";
    
    int line = 0;
    
    public void println(String text) {
        line++;
        this.text = text;
        repaint();
        System.out.println("Kotuc.bluetooh: "+text);
        status.append(text);
    }
    
    public void paint(Graphics g) {
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawString("1 server", 30, 20, g.LEFT|g.TOP);
        g.drawString("2 client", 30, 35, g.LEFT|g.TOP);
        g.drawString("3 automaticaly", 30, 50, g.LEFT|g.TOP);
        g.drawString(text, 30, 55+line*16, g.LEFT|g.TOP);
    }
        
    public void keyPressed(int key) {
        switch (key) {
            case KEY_NUM1: 
                createServerConnection(MYSERVICEUUID_UUID);
            break;
            case KEY_NUM2:
                createClientConnection(MYSERVICEUUID_UUID);
            break;
            case KEY_NUM3:

                try {
                    createConnection(MYSERVICEUUID_UUID);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            break;
            case KEY_NUM4:
                listen();
            break;
            default:

                try {
                    dataout.write(key);
                    dataout.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
            break;
        }
    }
    

    
    public void listen() {
        Thread th = new Thread(new InputThread());
        th.start();
        println("listening");
    }
    
   class InputThread implements Runnable {       
       public void run() {
            while (true) {
                try {
                    println(" recieved: "+datain.read());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
       }
   }
       
   private LocalDevice localDevice; // local Bluetooth Manager
   private DiscoveryAgent discoveryAgent; // discovery agent
    
   /**
    * Initialize
    */
   public void initBluetooth() throws BluetoothStateException {
        localDevice = null;
        discoveryAgent = null;
        // Retrieve the local device to get to the Bluetooth Manager
        localDevice = LocalDevice.getLocalDevice();
        // Servers set the discoverable mode to GIAC
        localDevice.setDiscoverable(DiscoveryAgent.GIAC);
        // Clients retrieve the discovery agent
        discoveryAgent = localDevice.getDiscoveryAgent();
    }

    public StreamConnection conn;
 
    public void closeConnection() {
        try {
            if (conn!=null) conn.close();
            conn = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
   
//    Vector discoveredServices = new Vector();
//    
//    void searchServices() {
//        // Given a service of interest, get its service record
//        ServiceRecord sr = (ServiceRecord)discoveredServices.elementAt(0/*i*/);
//        // Then get the service's connection URL
//        String connURL = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
//        try {
//            // Open connection
//            StreamConnection sc = (StreamConnection) Connector.open(connURL);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
    
    
    public DataOutputStream dataout;
    public DataInputStream datain;

    
    public void createClientConnection (final UUID uuid) {
        new Thread() {
            public void run() {
                openClientConnection(uuid);
            }
        }.start();
    }

    public void createServerConnection(final UUID uuid) {
        new Thread() {
            public void run() {
                openServerConnection(uuid);
            }
        }.start();
    }

    public void createConnection(final UUID uuid) throws Exception {
        new Thread() {
            public void run () {
                try {
                    openConnection(uuid);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    
    public void/*StreamConnection*/ openClientConnection(final UUID uuid) {
      try {
         // Select the service. Indicate no
         // authentication or encryption is required.
         String connectionURL = 
            discoveryAgent.selectService(uuid, 
                ServiceRecord.NOAUTHENTICATE_NOENCRYPT, 
                false);

        println("Connecting to " + uuid.toString());
        conn = 
            (StreamConnection) 
            Connector.open(connectionURL);


        dataout = 
            conn.openDataOutputStream();
        datain = 
            conn.openDataInputStream();

//                        dataout.writeUTF(messageOut);
//  todo                  System.out.println("Closing");
//  todo                  streamConnection.close();

        println("Client ready");

        connectionReady();
        
    } catch (BluetoothStateException bse) {
        System.out.println("BTMIDlet.btConnect2, exception " + bse);
    } catch (IOException ioe) {
        System.out.println("BTMIDlet.btConnect2, exception " + ioe);
    }
      
    }

    public /*StreamConnection*/ void openServerConnection(final UUID uuid) {        
        StreamConnectionNotifier scn = null;

        try {
            // Define the server connection URL
            String connURL = "btspp://localhost:"+MYSERVICEUUID_UUID.toString()+";"+"name="+myServiceName;

            // Create a server connection (a notifier)
            scn = (StreamConnectionNotifier) Connector.open(connURL);

            println("waiting for client");

            // Accept a new client connection
            conn = scn.acceptAndOpen();

                        // New client connection accepted; get a handle on it
            RemoteDevice rd = RemoteDevice.getRemoteDevice(conn);
            println("New client connection... " + 
                rd.getFriendlyName(false));
            // Read input message, in this example a String
            dataout = conn.openDataOutputStream();
            datain = conn.openDataInputStream();
    //                String s = dataIn.readUTF();
            // Pass received message to incoming message listener

            println("Server ready");
            
            connectionReady();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }
      
    public void connectionReady() {
        
    }

    /**
     * creates connection
     * joins to server vit specified UUID
     * if no server found. creates server UUID
     */
    public void openConnection(final UUID uuid) throws Exception {   
       
        try {
// tryies to connect to server
            
            // Select the service. Indicate no
            // authentication or encryption is required.
            String connectionURL = 
               discoveryAgent.selectService(uuid, 
                   ServiceRecord.NOAUTHENTICATE_NOENCRYPT, 
                   false);

            println("Connecting to " + uuid.toString());
            conn = 
                (StreamConnection) 
                Connector .open(connectionURL);


            println("Client ready");
        
        } catch (Exception ex) {
//     no server found -> create

            println("no server available "+ex);                
//                ex.printStackTrace();

            println("creating server");

            StreamConnectionNotifier scn = null;

            // Define the server connection URL
            String connURL = "btspp://localhost:"+MYSERVICEUUID_UUID.toString()+";"+"name="+myServiceName;

            // Create a server connection (a notifier)
            scn = (StreamConnectionNotifier) Connector.open(connURL);

            println("waiting for client");

            // Accept a new client connection
            conn = scn.acceptAndOpen();

                        // New client connection accepted; get a handle on it
            RemoteDevice rd = RemoteDevice.getRemoteDevice(conn);
            println("New client connection... " + 
                rd.getFriendlyName(false));

            println("Server ready");


        }
//      conn ection created            
//     open streams for reading/writing        
            
        dataout = conn.openDataOutputStream();
        datain = conn.openDataInputStream();
         
// informs that evrything is ready for use        
        connectionReady();
            
            
    }
    
    

}
