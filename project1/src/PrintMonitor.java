import java.util.LinkedList;


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
		notifyAll();
	}
	
	/**
	 *  Removes and returns the next word/file pair
	 *  
	 *  @return  String[] comprising of the word to print and file it came from
	 *  
	 *  @throws InterruptedException
	 */
	public synchronized String[] get() throws InterruptedException {
		
		// make sure there is something to return
		while (queue.isEmpty()) {
			wait();
		}
		return queue.removeFirst();
	}
}
