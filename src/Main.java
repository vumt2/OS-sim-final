import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;

public class Main extends JFrame{

	OS myOS = new OS();
	private JTextField editField;
	private JTextArea textArea;
	private JButton buttonEnter;
	private JButton buttonClear;
	
	public Main(){
		super("OS Simulator");
		getContentPane().setLayout(null);
		
		editField = new JTextField();
		editField.setBounds(15, 16, 376, 26);
		editField.setEditable(true);
		getContentPane().add(editField);
		editField.setColumns(10);
		
		buttonEnter = new JButton("Enter");
		buttonEnter.setBounds(404, 15, 97, 29);
		getContentPane().add(buttonEnter);
		
		
		textArea = new JTextArea();
		
		textArea.setEditable(false);
		getContentPane().add(textArea);
		
		JScrollPane sp = new JScrollPane(textArea);
		sp.setBounds(15, 58, 588, 336);
		getContentPane().add(sp);
		
		PrintStream pStream = new PrintStream(new MyOutputStream(textArea));
			
		buttonClear = new JButton("Clear");
		buttonClear.setBounds(505, 15, 98, 29);
		getContentPane().add(buttonClear);
		
		System.setErr(pStream);
		System.setOut(pStream);
		
		buttonEnter.addActionListener(new ActionListener(){

			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = editField.getText().toString();
				editField.setText("");
				myOS.runCommand(command);
			}
			
		});
		
		buttonClear.addActionListener(new ActionListener(){

			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					textArea.getDocument().remove(0, textArea.getDocument().getLength());
					editField.setText("");
				}
				catch (BadLocationException e){
					System.out.println(e.getMessage());
				}
				
			}
			
		});

		//Event on Enter key press
		editField.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					String command = editField.getText().toString();
					editField.setText("");
					myOS.runCommand(command);
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 480);
		setLocationRelativeTo(null);
	}	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new Main().setVisible(true);
			}
		});
	}

	
}
