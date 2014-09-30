import java.io.File;

/**
 * Class WordAndFile encapsulates a word read from a file.
 *
 * @author  Alan Kaminsky
 * @version 21-Sep-2014
 */
public class WordAndFile
	{
	public final String word;
	public final File file;

	/**
	 * Construct a new word/file object.
	 *
	 * @param  word  Word.
	 * @param  file  File.
	 */
	public WordAndFile
		(String word,
		 File file)
		{
		this.word = word;
		this.file = file;
		}

	/**
	 * Returns a string version of this word/file object.
	 *
	 * @return  String version.
	 */
	public String toString()
		{
		return word + " " + file;
		}
	}
