//*************************
//
// File:    WordPrinter.java
// Package: ---
// Unit:    Class WordPrinter
//
//*************************

/**
 *  The WordPrinter Thread will continuously poll the PrintMonitor for
 *  a word/file to print out (and print it out) until it encounters an 
 *  empty string. At this point, the WordPrinter Thread will terminate.
 *  
 *  @author   Michael Yachanin (mry1294)
 *  @version  Sep 16, 2014
 *
 */
public class WordPrinter implements Runnable {

	// hidden variables
	private PrintMonitor printMonitor;
	
	/**
	 *  WordPrinter Constructor
	 */
	public WordPrinter() {
		this.printMonitor = PrintMonitor.getInstance();
	}
	
	@Override
	public void run() {
		
		// nextWord[0] = word to print
		// nextWord[1] = filename word is from
		String[] nextWord;
		try {
			// loops until an empty string is encountered
			while (!(nextWord = printMonitor.get())[0].equals("")) {
				// make sure only 1 thing is printing at a time
				synchronized(System.out) {
					System.out.println(nextWord[0] + " " + nextWord[1]);
				}
			}
		} catch (InterruptedException e) {
			// this should never be reached
			e.printStackTrace();
		}
	}
}
