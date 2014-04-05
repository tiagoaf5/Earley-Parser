package main;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Lines {

	private ArrayList<Sentence> lines=new ArrayList<Sentence>();
	private int count=0;
	
	public Lines(String filename) {
		readFile(filename);
	}

	private void readFile(String filename) {

		FileInputStream fileStream;

		try {
			fileStream = new FileInputStream(filename);

			DataInputStream input = new DataInputStream(fileStream);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
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
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("File " + filename + " doesn't exist!");
			//e.printStackTrace();
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
