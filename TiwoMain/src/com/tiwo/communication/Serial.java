package com.tiwo.communication;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;



public class Serial {

	public ArrayList<String> ports;
	private SerialPort serialPort;
	
	public Serial(){
		ports = new ArrayList<String>();
		
        @SuppressWarnings("rawtypes")
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        
        // return if nothing detected
        if(!portList.hasMoreElements()){
        	System.out.println("Error: Arduino not detected");
        	return;
        }
        
        // populate port names
        while(portList.hasMoreElements()){
            CommPortIdentifier current = (CommPortIdentifier) portList.nextElement();
            ports.add(current.getName());
        }
	}
	
    public void connect (String portName, int baudRate) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(baudRate,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                                                           
                (new Thread(new SerialReaderThread(in))).start();
                (new Thread(new SerialWriterThread(out))).start();
                
                //out.write("Hello Arduino, I'm Java!".getBytes());

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }   
    }

    public void sendMessage(String message) throws IOException{
    	OutputStream out = serialPort.getOutputStream();
		try {
			byte[] array = message.getBytes();
			out.write(array);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }


}
