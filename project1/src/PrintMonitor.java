//*************************
//
// File:    PrintMonitor.java
// Package: ---
// Unit:    Class PrintMonitor
//
//*************************

import java.util.LinkedList;


/**
 *  PrintMonitor is a singleton class that acts as a monitor between WordSearchers 
 *  and WordPrinters. It maintains a LinkedList -- used as a queue -- in order to 
 *  queue up words to print for the PrintWriters to poll from. WordSearchers can
 *  add a word/file to the queue with the method put(). WordPrinters can poll for
 *  word/files to print with the method get(). If no words are present in the queue,
 *  the WordPrinters will block. Furthermore, when a word is inserted into the queue,
 *  notifyAll() is called to wake up all blocked WordPrinters. get() and put() are 
 *  both synchronized.
 *  
 *  An instance of the PrintMonitor class can be obtained with the getInstance() method
 *  
 *  @author   Michael Yachanin (mry1294)
 *  @version  Sep 16, 2014
 *
 */
public class PrintMonitor {
	
	// hidden variables
	private static PrintMonitor instance = null;
	private LinkedList<String[]> queue;
	
	/**
	 *  This is called to get an instance of the PrintMonitor class
	 *  Allows for there to only be 1 instantiation of the class
	 *  
	 *  @return  instance of PrintMonitor class
	 */
	public static PrintMonitor getInstance() {
		if (instance == null) {
			instance = new PrintMonitor();
		}
		return instance;
	}
	
	/**
	 *  Prevent external instantiation of PrintMonitor object
	 */
	private PrintMonitor() {
		this.queue = new LinkedList<String[]>();
	}
	
	/**
	 *  Queues up a word to be printed
	 *  
	 *  @param  word      word to print out
	 *  @param  filename  name of the file the word is from
	 */
	public synchronized void put(String word, String filename) {
		queue.addLast(new String[]{word, filename});
		
		// notify all blocked WordPrinters that there is something to print
		notifyAll();
	}
	
	/**
	 *  Removes and returns the next word/file pair
	 *  
	 *  @return  String[] comprising of the word to print and file it came from
	 *  
	 *  @throws InterruptedException  Thrown if thread is interrupted
	 */
	public synchronized String[] get() throws InterruptedException {
		
		// block until there is something to return
		while (queue.isEmpty()) {
			wait();
		}
		return queue.removeFirst();
	}
}
