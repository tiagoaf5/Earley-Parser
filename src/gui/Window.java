package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import main.EarleyParser;
import main.Grammar;
import main.GrammarErrorException;
import main.Lines;
import main.MyTree;
import main.Sentence;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class Window {

	private JFrame frmEarleyParser;
	protected JTextArea textArea;
	protected JTextArea textArea1;
	protected JTextPane textArea2;
	static final JFileChooser grammarChooser = new JFileChooser(".");
	static private File grammarFile;
	static final JFileChooser sentenceChooser = new JFileChooser(".");
	static private File sentenceFile;
	
	private StringBuilder log;
	public Graph<String, Integer> g;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frmEarleyParser.setVisible(true);
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

	public void addToLog(String txt, String color) {
		log.append("<div style=\"color:" + color + ";font-family:sans-serif;font-size:10px;\">" + txt + "</div>");
		textArea2.setText(log.toString());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		log = new StringBuilder();
		log.append("<html>");

		frmEarleyParser = new JFrame();
		frmEarleyParser.setTitle("Earley Parser");
		frmEarleyParser.setResizable(false);
		frmEarleyParser.setBounds(50, 50, 800, 500);
		frmEarleyParser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEarleyParser.getContentPane().setLayout(null);

		JButton btnChooseGrammarFile = new JButton("Choose grammar file");
		btnChooseGrammarFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (grammarChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					grammarFile = grammarChooser.getSelectedFile();
					System.out.println("Opening grammar file: " + grammarFile.getName() + ".\n");
					readContent(grammarFile, 1);
				}
			}
		});
		btnChooseGrammarFile.setBounds(10, 11, 166, 29);
		frmEarleyParser.getContentPane().add(btnChooseGrammarFile);

		textArea = new JTextArea();
		//textArea.setBounds(186, 13, 588, 181);
		textArea.setBorder(BorderFactory.createLineBorder(Color.black));
		//frame.getContentPane().add(textArea);

		JScrollPane scroll = new JScrollPane ( textArea );
		scroll.setBounds(186, 13, 588, 181);
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		frmEarleyParser.getContentPane().add ( scroll );


		textArea2 = new JTextPane();
		textArea2.setEditable(false);
		textArea2.setBorder(BorderFactory.createLineBorder(Color.gray));
		textArea2.setContentType("text/html");
		textArea2.setFont(new Font("Arial", Font.PLAIN, 13));


		JScrollPane scroll2 = new JScrollPane ( textArea2 );
		scroll2.setBounds(186, 341, 588, 57);
		scroll2.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		frmEarleyParser.getContentPane().add ( scroll2 );


		JLabel lblGrammarErrorLog = new JLabel("LOG");
		lblGrammarErrorLog.setEnabled(false);
		lblGrammarErrorLog.setForeground(Color.red);
		lblGrammarErrorLog.setBounds(134, 341, 42, 14);
		frmEarleyParser.getContentPane().add(lblGrammarErrorLog);

		JButton btnChooseSentencesFile = new JButton("Choose sentences file");
		btnChooseSentencesFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (sentenceChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					sentenceFile = sentenceChooser.getSelectedFile();
					System.out.println("Opening sentences file: " + sentenceFile.getName() + ".\n");
					readContent(sentenceFile, 2);
					//updateSentencesText();
				}
			}
		});
		btnChooseSentencesFile.setBounds(10, 210, 166, 29);
		frmEarleyParser.getContentPane().add(btnChooseSentencesFile);


		textArea1 = new JTextArea();
		textArea1.setBorder(BorderFactory.createLineBorder(Color.black));

		JScrollPane scroll1 = new JScrollPane ( textArea1 );
		scroll1.setBounds(186, 212, 588, 113);
		scroll1.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		frmEarleyParser.getContentPane().add ( scroll1 );

		JButton btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(textArea.getText().trim().equals("") || textArea1.getText().trim().equals(""))
					JOptionPane.showMessageDialog(null,"You must select a grammar and at least a sentence!", "Warning",JOptionPane.WARNING_MESSAGE);
				System.out.println("- Grammar: \n"+ textArea.getText());
				System.out.println("- Sentences: \n"+ textArea1.getText());

				startEarleyParser();
			}
		});
		btnStart.setBounds(375, 409, 107, 29);
		frmEarleyParser.getContentPane().add(btnStart);

		JButton btnSaveGrammar = new JButton("Save grammar");
		btnSaveGrammar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				if (chooser.showSaveDialog(chooser) != JFileChooser.APPROVE_OPTION)
					return;
				File file = chooser.getSelectedFile();
				if (file == null)
					return;

				FileWriter writer = null;
				try {

					if(file.getName().endsWith(".txt"))
						writer = new FileWriter(file);
					else
						writer = new FileWriter(file+".txt");

					textArea.write(writer);
				} catch (IOException ex) {
					addToLog("Grammar file not saved. Try again", "grey");
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException x) {
						}
					}
				}
			}
		});
		btnSaveGrammar.setBounds(39, 51, 137, 23);
		frmEarleyParser.getContentPane().add(btnSaveGrammar);

		JButton btnSaveSentences = new JButton("Save sentences");
		btnSaveSentences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				if (chooser.showSaveDialog(chooser) != JFileChooser.APPROVE_OPTION)
					return;
				File file = chooser.getSelectedFile();
				if (file == null)
					return;

				FileWriter writer = null;
				try {
					if(file.getName().endsWith(".txt"))
						writer = new FileWriter(file);
					else
						writer = new FileWriter(file+".txt");
					textArea1.write(writer);
				} catch (IOException ex) {
					addToLog("Sentences file not saved. Try again", "grey");
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException x) {
						}
					}
				}
			}
		});
		btnSaveSentences.setBounds(39, 250, 137, 23);
		frmEarleyParser.getContentPane().add(btnSaveSentences);
	}


	protected void startEarleyParser() {
		g = new DelegateForest<String, Integer>(new DirectedOrderedSparseMultigraph<String, Integer>());
		try {
			Grammar grammar =new Grammar(textArea.getText().trim(), true);
			Lines lines = new Lines(textArea1.getText().trim(), true);
			EarleyParser ep;

			ArrayList<Sentence> sentences = lines.getLines();

			boolean atleastone = false;
			
			for(int i = 0; i < sentences.size(); i++) {
				ep = new EarleyParser(sentences.get(i), grammar);
				int result = ep.run();
				if(result > 0)
				{
					String error = "Error near Token " + (result-1) + " of Sentence " + (i + 1) + "!";
					addToLog(error, "maroon");
					System.err.println(error);	
				} else if(result == -1)
				{
					String error = "Sentence " + (i + 1) + " is invalid!";
					addToLog(error, "maroon");
					System.err.println(error);	
				} else if(result == 0)
				{
						String res = "Sentence " + (i + 1) + " is valid!";
						addToLog(res, "green");
						System.out.println(res);
						atleastone = true;
				}

				if (result==0) {
					ArrayList<MyTree> trees = new ArrayList<MyTree>();

					for(int k = 0; k < ep.getTrees().size(); k++)	{
						trees.add(new MyTree(ep.getTrees().get(k),g,"Sentence "+(i+1)+" - tree "+(k+1)));
					};

					for(MyTree tree : trees) {
						tree.show();
					}  
				}
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			Layout<String, String> layout = new TreeLayout<String, String>((Forest) g);
			//layout.setSize(new Dimension(300,300)); // sets the initial size of the layout space
			// The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types

			Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
				public Paint transform(String s) {
					if(s.startsWith("\""))
					{
						if(s.startsWith("\"\""))
						{
							return Color.LIGHT_GRAY;
						} else
							return Color.CYAN;
					}
					else
						return Color.BLUE;
				}
			};
			if(atleastone)
			{
				BasicVisualizationServer<String,String> vv = new BasicVisualizationServer<String,String>(layout);
				vv.setGraphLayout(layout);
				vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
				vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
				vv.setPreferredSize(layout.getSize());
				JFrame frame = new JFrame("Tree Visualization");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				JScrollPane panel = new JScrollPane(vv);
				frame.setContentPane(panel);
				frame.setSize(new Dimension(800, (int) layout.getSize().getHeight() + 100));
				frame.revalidate();
				frame.repaint();
				frame.setVisible(true);
			}

		} catch (GrammarErrorException e) {
			addToLog("<b>"+ e.getMessage() + "</b>", "red");
			//e.printStackTrace();
		}
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
				if(text==1) {
					textArea.append(line+"\n");
					//grammarFileLines+=line+"\n";
				}
				else {
					textArea1.append(line+"\n");
					//sentenceFileLines+=line+"\n";
				}
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
