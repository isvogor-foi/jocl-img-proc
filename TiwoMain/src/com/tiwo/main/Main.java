package com.tiwo.main;

import java.awt.EventQueue;

import com.tiwo.communication.Serial;
import com.tiwo.forms.MainForm;

public class Main {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		Serial sr = new Serial();
		
		try 
		{
			sr.connect(sr.ports.get(0), 9600);
			//sr.sendMessage("Hello");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
