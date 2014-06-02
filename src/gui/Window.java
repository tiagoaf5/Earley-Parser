package gui;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JLabel;
import java.awt.Color;

public class Window {

	private JFrame frame;
	protected JTextArea textArea;
	protected JTextArea textArea_1;
	protected JTextArea textArea_2;
	static final JFileChooser grammarChooser = new JFileChooser();
	static private File grammarFile;
	static protected ArrayList<String> grammarFileLines=new ArrayList<String>();
	static final JFileChooser sentenceChooser = new JFileChooser();
	static private File sentenceFile;
	static protected ArrayList<String> sentenceFileLines=new ArrayList<String>();

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
	                readContent(grammarFile, 1);
	                updateGrammarText();
				}
			}
		});
		btnChooseGrammarFile.setBounds(10, 11, 166, 29);
		frame.getContentPane().add(btnChooseGrammarFile);
		
		textArea = new JTextArea();
		textArea.setBounds(186, 13, 588, 181);
		textArea.insert("olaaa", 0);
		frame.getContentPane().add(textArea);
		
		
		
		JButton btnChooseSentencesFile = new JButton("Choose sentences file");
		btnChooseSentencesFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (sentenceChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					sentenceFile = sentenceChooser.getSelectedFile();
	                System.out.println("Opening sentences file: " + sentenceFile.getName() + ".\n");
	                readContent(sentenceFile, 2);
	                updateSentencesText();
				}
			}

			
		});
		btnChooseSentencesFile.setBounds(10, 283, 166, 29);
		frame.getContentPane().add(btnChooseSentencesFile);
		
		textArea_1 = new JTextArea();
		textArea_1.setBounds(186, 285, 588, 113);
		frame.getContentPane().add(textArea_1);
		
		JButton btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("- Grammar: \n"+ textArea.getText());
				System.out.println("- Sentences: \n"+ textArea_1.getText());
			}
		});
		btnStart.setBounds(375, 409, 118, 42);
		frame.getContentPane().add(btnStart);
		
		textArea_2 = new JTextArea();
		textArea_2.setBounds(232, 205, 541, 57);
		frame.getContentPane().add(textArea_2);
		
		JLabel lblGrammarErrorLog = new JLabel("Grammar error log:");
		lblGrammarErrorLog.setEnabled(false);
		lblGrammarErrorLog.setForeground(Color.RED);
		lblGrammarErrorLog.setBounds(102, 205, 118, 14);
		frame.getContentPane().add(lblGrammarErrorLog);
	}
	
	protected void updateGrammarText() {
		for(int i=0; i<grammarFileLines.size(); i++) {
			textArea.append(grammarFileLines.get(i)+"\n");
		}
	}

	private void updateSentencesText() {
		for(int i=0; i<sentenceFileLines.size(); i++) {
			textArea_1.append(sentenceFileLines.get(i)+"\n");
		}
		
	}
	
	protected void updateErrorLog(String text) {
		textArea.append(text + "\n");
	}
	
	protected void readContent(File file, int text) {
		
		FileInputStream fileStream;

		try {
			fileStream = new FileInputStream(file.getPath());

			DataInputStream input = new DataInputStream(fileStream);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String lineRead;

			while ((lineRead = reader.readLine()) != null)   {

				String line=lineRead.trim();
				if(text==1)
					grammarFileLines.add(line);
				else
					sentenceFileLines.add(line);
			}
			reader.close();
			input.close();
		} catch (FileNotFoundException e) {
			if(text==1)
				System.out.println("Grammar file doesn't exist!");
			else
				System.out.println("Sentences file doesn't exist!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
