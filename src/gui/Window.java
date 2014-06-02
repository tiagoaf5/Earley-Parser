package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class Window {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(50, 50, 800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnChooseGrammarFile = new JButton("Choose grammar file");
		btnChooseGrammarFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnChooseGrammarFile.setBounds(10, 11, 166, 29);
		frame.getContentPane().add(btnChooseGrammarFile);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(186, 13, 588, 181);
		frame.getContentPane().add(textArea);
		
		JButton btnChooseSentencesFile = new JButton("Choose sentences file");
		btnChooseSentencesFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnChooseSentencesFile.setBounds(10, 206, 166, 29);
		frame.getContentPane().add(btnChooseSentencesFile);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(186, 208, 588, 160);
		frame.getContentPane().add(textArea_1);
		
		JButton btnStart = new JButton("START");
		btnStart.setBounds(375, 409, 118, 42);
		frame.getContentPane().add(btnStart);
	}
}
