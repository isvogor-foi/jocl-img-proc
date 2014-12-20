package com.tiwo.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;

public class MainForm {

	public JFrame frame;
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
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "Hello java!");
			}
		});
		
		btnStart.setBounds(10, 11, 89, 23);
		frame.getContentPane().add(btnStart);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(109, 12, 78, 20);
		frame.getContentPane().add(comboBox);
	}
}
