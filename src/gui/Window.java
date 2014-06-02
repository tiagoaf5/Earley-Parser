package gui;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JTextArea;

public class Window {

	private JFrame frame;
	static final JFileChooser grammarChooser = new JFileChooser();
	static private File grammarFile;
	static final JFileChooser sentenceChooser = new JFileChooser();
	static private File sentenceFile;

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

				if (grammarChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					grammarFile = grammarChooser.getSelectedFile();
	                System.out.println("Opening grammar file: " + grammarFile.getName() + ".\n");
				} else {
					
				}
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
				
				if (sentenceChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					sentenceFile = sentenceChooser.getSelectedFile();
	                System.out.println("Opening sentences file: " + sentenceFile.getName() + ".\n");
				} else {
					
				}
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
