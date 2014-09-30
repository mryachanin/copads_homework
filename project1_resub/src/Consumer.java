import java.util.Hashtable;

/**
 * Class Consumer provides a thread that gets words from the multichannel
 * monitor, looks for one particular target word, and prints the target word
 * and file when found.
 *
 * @author  Alan Kaminsky
 * @author  Michael Yachanin (mry1294)
 * @version 21-Sep-2014
 */
public class Consumer
	extends Thread
	{
	
	private String target;
	private Channel channel;
	private Hashtable<String, String> wordsSeen;
	
	/**
	 * Construct a new consumer thread.
	 *
	 * @param  target        Target word to look for.
	 * @param  multichannel  Multichannel from which to get words.
	 */
	public Consumer
		(String target,
		 MultiChannel multichannel)
		{
		this.target = target;
		this.channel = multichannel.getChannel();
		this.wordsSeen = new Hashtable<String, String>();
		}

	/**
	 * Run this consumer thread.
	 */
	public void run()
		{
		WordAndFile nextWordObj;
		try {
			// loops until a null is encountered
			while (!((nextWordObj = channel.get()) == null))
				{
				String nextWord = nextWordObj.toString().split(" ")[0];
				String nextFile = nextWordObj.toString().split(" ")[1];
				// target word AND file not in hashtable
				if (nextWord.equals(target) && !wordsSeen.containsKey(nextFile))
					{
					// make sure only 1 thing is printing at a time
					synchronized(System.out)
						{
						System.out.println(nextWordObj.toString());
						}
					wordsSeen.put(nextFile, nextWord);
					}
				}
			} catch (InterruptedException e)
				{
				// should not happen
				}
			}
	}
