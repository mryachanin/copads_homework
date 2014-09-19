//*************************
//
// File:    WordSearcher.java
// Package: ---
// Unit:    Class WordSearcher
//
//*************************

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;


/**
 *  The WordSearcher Thread will continuously read words from the file WordSearcher 
 *  is passed upon instantiation. If a word matches a target word (stored as a key 
 *  in a Hashtable which it is also passed in upon instantiation), this word, along
 *  with the file it came from, will be immediately be passed to the PrintWriter to
 *  be printed. When a word is found, the value associated with the Hashtable key 
 *  will be set to zero so the word is not printed again. When all words have been 
 *  read, an empty string is passed to the PrintWriter to signal the PrintWriter to
 *  terminate.
 *  
 *  @author   Michael Yachanin (mry1294)
 *  @version  Sep 16, 2014
 *
 */
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
		this.filename = filename;
		try {
			this.fin = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			this.fileUsage();
		}
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
					words.put(s, 0);
				}
			}
		}
		
		// signal to terminate a WordPrinter thread
		printMonitor.put("", "");
	}
	
	/**
	 *  This is called when a file is not found
	 *  
	 *  Prints a usage message and exits program
	 */
	private void fileUsage() {
		System.out.println("java Search <files> <words>");
		System.out.println("File: " + this.filename + " was not found.");
		System.out.println("Please retry with a valid list of filenames.");
		System.exit(1);
	}
}
