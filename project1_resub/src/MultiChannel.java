import java.util.ArrayList;

/**
 * Class MultiChannel provides a monitor for synchronizing multiple producers
 * with multiple consumers.
 *
 * @author  Alan Kaminsky
 * @author  Michael Yachanin (mry1294)
 * @version 21-Sep-2014
 */
public class MultiChannel
	{
	
	private int numProducers;
	private ArrayList<Channel> channels;
	
	/**
	 * Construct a new multichannel that supports the given numbers of producers
	 * and consumers.
	 *
	 * @param  numProducers  Number of producers.
	 * @param  numConsumers  Number of consumers.
	 */
	public MultiChannel
		(int numProducers,
		 int numConsumers)
		{
		this.numProducers = numProducers;
		channels = new ArrayList<Channel>();
		}

	/**
	 * Get a channel for use by a consumer. Each consumer gets its own separate
	 * channel.
	 *
	 * @return  Channel.
	 */
	public synchronized Channel getChannel()
		{
		Channel newChannel = new Channel();
		channels.add(newChannel);
		return newChannel;
		}

	/**
	 * Put the given word/file into all the consumers' channels.
	 *
	 * @param  v  Word/file object.
	 *
	 * @exception  InterruptedException
	 *     Thrown if the calling thread is interrupted while blocked in this
	 *     method.
	 */
	public synchronized void put
		(WordAndFile v)
		throws InterruptedException
		{
		for (Channel c : channels)
			{
			c.put(v);
			}
		}

	/**
	 * Notify this multichannel that a producer is finished.
	 *
	 * @exception  InterruptedException
	 *     Thrown if the calling thread is interrupted while blocked in this
	 *     method.
	 */
	public synchronized void producerFinished()
		throws InterruptedException
		{
		if ((--this.numProducers) == 0)
			{
			this.put(null);
			}
		}
	}
