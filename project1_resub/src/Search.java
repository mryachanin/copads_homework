import java.io.File;
import java.io.FileNotFoundException;

/**
 * Class Search is the main program for COPADS Project 1, Fall 2014 Semester.
 * <P>
 * Usage: <TT>java Search <I>files</I> <I>words</I></TT>
 *
 * @author  Alan Kaminsky
 * @author  Michael Yachanin (mry1294)
 * @version 21-Sep-2014
 */
public class Search
	{
	/**
	 * Main program.
	 *
	 * @param  args  Command line arguments.
	 */
	public static void main
		(String[] args)
		throws Exception
		{
		// usage check for correct number or arguments
		if (args.length != 2)
			{
			mainUsage();
			}
		
		// check and make sure file array does not end in a comma
		if (args[0].charAt(args[0].length() - 1) == ',')
			{
			mainUsage();
			}
		
		// parse input arguments into appropriate arrays
		String[] files = args[0].split(",");
		String[] words = args[1].split(",");
		
		// check for erroneous words
		for (int i=0; i < args[1].length(); i++)
			{
			char nextChar = args[1].charAt(i);
			if (nextChar == ',')
				{
				if (i == (args[1].length() - 1))
					{
					mainUsage();
					}
				continue;
				}
			if ((nextChar < 'A' || nextChar > 'Z') && (nextChar < 'a' || nextChar > 'z'))
				{
				mainUsage();
				}
			}
		
		// create the main multichannel
		MultiChannel mc = new MultiChannel(files.length, words.length);
		
		// create and start group 2 threads
		Thread[] wordPrinters = new Thread[words.length];
		for (int i=0; i < words.length; i++)
			{
			wordPrinters[i] = new Thread(new Consumer(words[i].toLowerCase(), mc));
			wordPrinters[i].start();
			}
		
		// create and start group 1 threads
		Thread[] wordSearchers = new Thread[files.length];
		for (int i=0; i < files.length; i++)
			{
			try
				{
				File file = new File(files[i]);
				wordSearchers[i] = new Thread(new Producer(file, mc));
				wordSearchers[i].start();
				} catch(FileNotFoundException e)
					{
					fileUsage(files[i]);
					}
			
			}
		
		// join on searcher threads
		for (Thread ws : wordSearchers)
			{
			ws.join();
			}
		
		// join on printer threads
		for (Thread wp : wordPrinters)
			{
			wp.join();
			}
		}
			
			
		/**
		 *  This is called when the user has not provided proper arguments to the program
		 *  
		 *  Prints a usage message and exits program
		 */
		private static void mainUsage()
			{
			System.out.println("java Search <files> <words>");
			System.out.println("<files> is a list of one or more text file names separated by commas with no whitespace");
			System.out.println("<words> is a list of one or more target words separated by commas with no whitespace");
			System.out.println("A word is defined to be a maximal length sequence of letters A through Z and a through z.");
			System.exit(1);
			}
		
		/**
		 *  This is called when a file is not found
		 *  
		 *  Prints a usage message and exits program
		 */
		private static void fileUsage(String file)
			{
			System.out.println("java Search <files> <words>");
			System.out.println("File: " + file + " was not found.");
			System.out.println("Please retry with a valid list of filenames.");
			System.exit(1);
			}
	}
