package comprehensive;

/**
 * This class contains the main method for our text generating program. It
 * takes the first four arguments from args and creates a fileName, 
 * seedWord, number of words to be generated, and either 'all', 'none',
 * or 'one' (which change the behavior of the text generator).
 * 
 * @author Nate LeMonnier and Noah Shewell
 * @version April 20, 2024
 */
public class TextGenerator {

	/**
	 * This main method acts as the leading force behind the sentence generator. It passes
	 * in args[0] as the fileName, args[1] as the seedWord, args[2] as the numWords, and 
	 * args[3] as the desired text generation method, if it exists. 
	 * @param args
	 */
	public static void main(String[] args) {

		String fileName = args[0]; // name of input file
		String seed = args[1]; // seed word
		int numWords = Integer.parseInt(args[2]); // number of words to be printed
		String all = "none"; // type of output
		if (args.length > 3)
			all = args[3];

		System.out.println(Generator.generateSentence(fileName, seed, numWords, all)); // text generation
	}
}
