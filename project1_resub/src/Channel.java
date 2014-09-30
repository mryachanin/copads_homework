/**
 * Class Channel provides a monitor for synchronizing producers with one
 * consumer.
 *
 * @author  Alan Kaminsky
 * @author  Michael Yachanin (mry1294)
 * @version 21-Sep-2014
 */
public class Channel
	{
	
	private WordAndFile newWord, oldWord;
	
	/**
	 * Put the given word/file into this channel. Blocks if this channel is
	 * already holding a word/file.
	 *
	 * @param  v  Word/file object; may be null.
	 *
	 * @exception  InterruptedException
	 *     Thrown if the calling thread is interrupted while blocked in this
	 *     method.
	 */
	public synchronized void put
		(WordAndFile v)
		throws InterruptedException
		{
		while (this.newWord != this.oldWord)
			{
			this.wait();
			}
		
		this.newWord = v;
		this.notifyAll();
		}

	/**
	 * Get a word/file out of this channel. Blocks if this channel is not
	 * holding a word/file.
	 *
	 * @return  Word/file object; may be null.
	 *
	 * @exception  InterruptedException
	 *     Thrown if the calling thread is interrupted while blocked in this
	 *     method.
	 */
	public synchronized WordAndFile get()
		throws InterruptedException
		{
		while (this.newWord == this.oldWord)
			{
			this.wait();
			}
		
		this.oldWord = this.newWord;
		this.notifyAll();
		return this.newWord;
		}
	}
