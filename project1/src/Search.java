//*************************
//
// File:    Search.java
// Package: ---
// Unit:    Class Search
//
//*************************

import java.util.Hashtable;


/**
 *  Class Search is the main program for the Multiple File Multiple Word Searching program.
 *  There are two groups of threads involved and they are sync'd by the PrintMonitor class. 
 *  Group 1 contains WordSearcher threads that read words from a distinct file passed to each one. 
 *  While reading, the WordSearcher matches words against a list of target words. If a word 
 *  matches a target word, it is immediately passed to the printMonitor class which queues 
 *  that word up to be printed by the next available WordPrinter thread. Speaking of which,
 *  group 2 contains WordPrinter threads that block until a word is available to be printed 
 *  from the PrintMonitor class. Once a word is available, the PrintMonitor will get that 
 *  word and print it to stdout along with the file it came from. The program will terminate
 *  after all WordSearcher and WordPrinter threads terminate.
 *  <p>
 *  
 *  Usage: java Search <files> <words>
 *  	<files> is a list of one or more text file names, separated by commas, with no whitespace.
 *  	<words> is a list of one or more target words, separated by commas, with no whitespace.
 *  
 *  @author   Michael Yachanin (mry1294)
 *  @version  Sep 16, 2014
 *
 */
public class Search {
	
	/**
	 *  Prevent external instantiation of Search object
	 */
	private Search() {}
	
	/**
	*  Main program
	*  
	*  @param  args
	*  		args[0] is a list of one or more text file names separated by commas with no whitespace
	*       args[1] is a list of one or more target words separated by commas with no whitespace
	*       
	*  @SuppressWarnings("unchecked")  This is from casting an object to a Hashtable "without checking"
	*  								   It is clear the object can be casted to a Hashtable    
	*/
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException {
		
		// usage check for correct number or arguments
		if (args.length != 2) {
			mainUsage();
		}
		
		// check and make sure file array does not end in a comma
		if (args[0].charAt(args[0].length() - 1) == ',') {
			mainUsage();
		}
		
		// parse input arguments into appropriate arrays
		String[] files = args[0].split(",");
		Hashtable<String, Integer> words = new Hashtable<String, Integer>();
		
		// check for erroneous words
		for (int i=0; i < args[1].length(); i++) {
			char nextChar = args[1].charAt(i);
			if (nextChar == ',') {
				if (i == (args[1].length() - 1)) {
					mainUsage();
				}
				continue;
			}
			if ((nextChar < 'A' || nextChar > 'Z') && (nextChar < 'a' || nextChar > 'z')) {
				mainUsage();
			}
		}
		
		// fill in word dictionary
		for (String s: args[1].split(",")) {
			words.put(s.toLowerCase(), 1);
		}
		
		// create and start group 2 threads
		Thread[] wordPrinters = new Thread[files.length];
		for (int i=0; i < files.length; i++) {
			wordPrinters[i] = new Thread(new WordPrinter());
			wordPrinters[i].start();
		}
		
		// create and start group 1 threads
		Thread[] wordSearchers = new Thread[files.length];
		for (int i=0; i < files.length; i++) {
			wordSearchers[i] = new Thread(new WordSearcher(files[i], (Hashtable<String, Integer>) words.clone()));
			wordSearchers[i].start();
		}
		
		// join on searcher threads
		for (Thread ws : wordSearchers) {
			ws.join();
		}
		
		// join on printer threads
		for (Thread wp : wordPrinters) {
			wp.join();
		}
	}
	
	
	
	/**
	 *  This is called when the user has not provided proper arguments to the program
	 *  
	 *  Prints a usage message and exits program
	 */
	private static void mainUsage() {
		System.out.println("java Search <files> <words>");
		System.out.println("<files> is a list of one or more text file names separated by commas with no whitespace");
		System.out.println("<words> is a list of one or more target words separated by commas with no whitespace");
		System.out.println("A word is defined to be a maximal length sequence of letters A through Z and a through z.");
		System.exit(1);
	}
}
