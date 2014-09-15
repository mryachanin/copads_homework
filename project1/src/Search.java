import java.util.Hashtable;


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
	*  @throws InterruptedException 
	*/
	public static void main(String[] args) throws InterruptedException {
		if (args.length < 2) {
			usage();
		}
		
		// parse input arguments into appropriate arrays
		String[] files = args[0].split(",");
		Hashtable<String, Integer> words = new Hashtable<String, Integer>();
		
		// check for erroneous words
		for (int i=0; i < args[1].length(); i++) {
			char nextChar = args[1].charAt(i);
			if (nextChar == ',') {
				continue;
			}
			if ((nextChar < 'A' || nextChar > 'Z') && (nextChar < 'a' || nextChar > 'z')) {
				usage();
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
	 *  Prints a usage message and exits program
	 */
	protected static void usage() {
		System.out.println("java Search <files> <words>");
		System.out.println("<files> is a list of one or more text file names separated by commas with no whitespace");
		System.out.println("<words> is a list of one or more target words separated by commas with no whitespace");
		System.exit(1);
	}
}
