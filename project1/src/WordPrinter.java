
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
		String[] nextWord;
		try {
			while (!(nextWord = printMonitor.get())[0].equals("")) {
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
