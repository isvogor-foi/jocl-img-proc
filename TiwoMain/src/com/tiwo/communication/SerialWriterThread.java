package com.tiwo.communication;

import java.io.IOException;
import java.io.OutputStream;

public class SerialWriterThread implements Runnable {

	OutputStream out;

	public SerialWriterThread(OutputStream out) {
		this.out = out;
	}

	
	// currently not in use, just sends "test" forever
	public void run() {
		try {
			/*
			int c = 0;
			while ((c = System.in.read()) > -1) {
				this.out.write(c);
			}
			*/
			while (true) {
				this.out.write("test".getBytes());
				//this.out.flush();
				Thread.sleep(2000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
