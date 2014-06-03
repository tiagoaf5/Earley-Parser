package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

public class Lines {

	private ArrayList<Sentence> lines=new ArrayList<Sentence>();
	private int count=0;

	public Lines(String filename){
		try {
			readFile(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Lines(String text, boolean interf){
		reader(new StringReader(text));
	}

	private void readFile(String filename) throws FileNotFoundException {
		File f = new File(filename);

		if (!f.exists())
			throw new FileNotFoundException("File " + filename + " doesn't exist!");

		try {
			reader(new FileReader(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("File " + filename + " doesn't exist!");
		}
	}

	private void reader(Reader in) {
		try {

			BufferedReader reader = new BufferedReader(in);
			String lineRead;

			while ((lineRead = reader.readLine()) != null)   {

				String line=lineRead.trim();
				String[] splits=line.split("\\s+");
				Sentence newLine=new Sentence(splits);
				lines.add(newLine);

				/*System.out.println(line);

				for(int i=0; i<splits.length; i++) {
					System.out.println(i + "'" + splits[i]+"'");
				}*/
			}
			reader.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Sentence> getLines() {
		return lines;
	}

	public ArrayList<String> getNextLine() {

		if(count>=lines.size())
			return null;

		ArrayList<String> tmp=lines.get(count).getSentence();
		count++;
		return tmp;
	}

	public void resetCount() {
		count=0;
	}

	/*public static void main(String[] args) {
		Lines s=new Lines("oi.txt");
	}*/
}
