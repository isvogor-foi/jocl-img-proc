package com.tiwo.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import com.tiwo.communication.Commands;
import com.tiwo.communication.Commands.MOVEMENT;
import com.tiwo.communication.Serial;

public class MainForm {

	public JFrame frame;
	
	private JButton btnSend;
	private JComboBox<Commands.MOVEMENT> cmbCommand;
	private JComboBox<Object> cmbPorts;
	private JButton btnConnect;

	
	/**
	 * Create the application.
	 */
	public MainForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// initialize frame
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(connectButtonAction);
		btnConnect.setBounds(10, 11, 89, 23);
		
		cmbPorts = new JComboBox<Object>(Serial.getInstance().getPorts().toArray());
		cmbPorts.setBounds(109,11,142,23);

		btnSend = new JButton("Send");
		btnSend.addActionListener(sendButtonAction);
		btnSend.setBounds(10, 45, 89, 23);
		btnSend.setEnabled(false);
		
		cmbCommand = new JComboBox<MOVEMENT>();
		cmbCommand.setBounds(109, 46, 142, 20);
		cmbCommand.setModel(new DefaultComboBoxModel<>(Commands.MOVEMENT.values()));
		cmbCommand.setEnabled(false);
		
		// add widgets to the frame 
		frame.getContentPane().add(btnSend);
		frame.getContentPane().add(cmbCommand);
		frame.getContentPane().add(cmbPorts);
		frame.getContentPane().add(btnConnect);
	}
	
	ActionListener connectButtonAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Serial serial = Serial.getInstance();
				serial.connect(serial.getPorts().get(0), 9600);
				
				btnSend.setEnabled(true);
				cmbCommand.setEnabled(true);
				
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	};
	
	ActionListener sendButtonAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Serial serial = Serial.getInstance();
				//serial.connect(serial.getPorts().get(0), 9600);	
				serial.sendMessage(cmbCommand.getSelectedItem().toString());
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	};
	

	
}
