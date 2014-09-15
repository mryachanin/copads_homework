import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;


public class WordSearcher implements Runnable {
	
	// hidden variables
	private Scanner fin;
	private Hashtable<String, Integer> words;
	private String filename;
	private PrintMonitor printMonitor;
	
	/**
	 *  WordSearcher Constructor
	 *  
	 *  @param  filename  Full file path to file containing words to search
	 *  @param  words     Hashtable of words to search for
	 */
	public WordSearcher(String filename, Hashtable<String, Integer> words) {
		try {
			this.fin = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			Search.usage();
		}
		this.filename = filename;
		this.words = words;
		this.printMonitor = PrintMonitor.getInstance();
	}

	@Override
	public void run() {
		
		// read entire file line by line
		while (fin.hasNextLine()) {
			
			// get next line
			String nextLine = fin.nextLine();
			
			// split line on non-letters
			String[] nextWords = nextLine.split("[^a-zA-Z]");
			
			// loop over words
			for (String s : nextWords) {
				
				// per specifications, transform all words to lower-case
				s = s.toLowerCase();
				
				// check if this is a specified word / it has been seen before
				if (words.containsKey(s) && words.get(s) == 1) {
					
					// queue up the word
					printMonitor.put(s, this.filename);
					
					// set value in dictionary to 0 --> signifies we've seen the word
					words.replace(s, 0);
				}
			}
		}
		
		// signal to terminate a WordPrinter thread
		printMonitor.put("", "");
	}
	
	
}
