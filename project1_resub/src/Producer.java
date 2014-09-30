import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Class Producer provides a thread that reads words from one particular file
 * and passes each word on to the multichannel monitor.
 *
 * @author  Alan Kaminsky
 * @author  Michael Yachanin (mry1294)
 * @version 21-Sep-2014
 */
public class Producer
	extends Thread
	{
	private File file;
	private MultiChannel multichannel;
	private Scanner fin;
	
	/**
	 * Construct a new producer thread.
	 *
	 * @param  file          File from which to read words.
	 * @param  multichannel  Multichannel into which to put words.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public Producer
		(File file,
		 MultiChannel multichannel)
		throws IOException
		{
		this.file = file;
		this.multichannel = multichannel;
		this.fin = new Scanner(file);
		}

	/**
	 * Run this producer thread.
	 */
	public void run()
		{
		
		// read entire file line by line
		while (fin.hasNextLine())
			{
			
			// get next line
			String nextLine = fin.nextLine();
			
			// split line on non-letters
			String[] nextWords = nextLine.split("[^a-zA-Z]");
			
			// loop over words
			for (String s : nextWords)
				{
				
				// per specifications, transform all words to lower-case
				s = s.toLowerCase();
					
				// queue up the word
				try
					{
					multichannel.put(new WordAndFile(s, file));
					} catch (InterruptedException e)
						{
						// should not happen
						}
				}
			}
		
		// signal to multichannel that the producer is done
		try
			{
			multichannel.producerFinished();
			} catch (InterruptedException e)
				{
				// shouldn't happen
				}
		
		// close scanner
		fin.close();
		
		}
	}
