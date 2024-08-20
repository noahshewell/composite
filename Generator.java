package comprehensive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * This class contains all of the methods required to successfully generate a sentence
 * from a fileName, seedWord, number of words, and type of generation ('all', 'none', 
 * or 'one').
 * 
 * @author Nate LeMonnier and Noah Shewell
 * @version April 20, 2024
 */
public class Generator {

	/**
	 * This class represents the bulk of the sentence generation, returning a String
	 * with the generated sentence. It creates a word array from fileName, then uses 
	 * both numWords and seedWord to generate a sentence depending on which type of
	 * generation was desired. 
	 * 
	 * @param fileName - String fileName that dictates which fileName to pass into wordArr
	 * @param seedWord - String seedWord that acts as the first word to call all of the 
	 * text generation methods on.
	 * @param numWords - int value that dictates how many words are to be generated
	 * @param allArg - String value that dictates what type of text generation is to be used
	 * @return sentence - String value that contains the generated sentence
	 * @throws IllegalArgumentException - if the fourth argument in args is not 'one', 'all',
	 * or nonexistent, an IllegalArgumentException is thrown. 
	 */
	public static String generateSentence(String fileName, String seedWord, int numWords, String allArg) {

		ArrayList<String> words = wordArr(fileName);
		HashMap<String, HashMap<String, Integer>> map = buildMap(words); // build map

		String sentence = "";

		switch (allArg) {
		case "none": // there is no fourth argument

			sentence = generateNone(map, seedWord, numWords); // calls the generateNone method
			break;

		case "all": // 'all' fourth argument

			sentence = generateAll(words, seedWord, numWords); // calls the generateAll method
			break;

		case "one": // 'one' fourth argument

			sentence = generateOne(map, seedWord, numWords); // calls the generate One method
			break;

		default: // invalid argument
			throw new IllegalArgumentException("Invalid output type: " + allArg);
		}

		return sentence; // the newly generated sentence

	}
	
	/**
	 * This method generates a sentence using the no-fourth-argument logic. To do this,
	 * it gets all of the subsequent words off of 'seedWord', ordered by highest probability
	 * to lowest probability. From here, it will print the K most likely words. If there 
	 * are fewer than K words, it will print as many as possible. 
	 * 
	 * @param map - HashMap<String, HashMap<String, Integer>> object that is used to get the
	 * HashMap<String, Integer> linked to seedWord that contains all of seedWord's subsequent
	 * words and their likelihood. 
	 * @param seedWord - String object who's complete subsequent words are chosen sorted
	 * by probability, descending.
	 * @param numWords - int value that dictates how many words are to be generated.
	 * @return sentence - String that contains the K most probable words off of 'seedWord' 
	 */
	private static String generateNone(HashMap<String, HashMap<String, Integer>> map, String seedWord, int numWords) {
		HashMap<String, Integer> wordMap = map.get(seedWord);
		ArrayList<String> maxValues = getMaxValues(wordMap);
		String sentence = "";
		
		for (int i = 0; i < numWords; i++) {
			if (i >= maxValues.size()) // if there are no more subsequent words, break
				break;
			sentence = sentence + maxValues.get(i) + " ";
		}
		return sentence;
	}
	
	/**
	 * This method generates a sentence using the 'all' sentence generation logic. To do
	 * this, the method creates a new String to which it adds the seedWord. From here,
	 * it adds random word from seedWord's subsequent words. If the next most probable word 
	 * is non-existent (the word was a distinct, final word in the file), the method 
	 * adds the seedWord again and gets a random value from seedWord's subsequent words.
	 * If the random word is not the final word in the file, it gets and adds a random 
	 * value from the randomWord's subsequent words. This generation (seedWord, seedWord
	 * subsequent value, seedWord's subsequentValue subsequent value) continues numWords
	 * times. 
	 * 
	 * @param words - ArrayList<String> of every word in the text file. 
	 * @param seedWord - String object that is first used in finding a random 
	 * subsequent word. 
	 * @param numWords - int value that dictates how many words are to be generated
	 * @return sentence - String value that contains the generated sentence.
	 */
	private static String generateAll(ArrayList<String> words, String seedWord, int numWords) {
		HashMap<String, ArrayList<String>> allWordsMap = buildAllWordsMap(words);
		String sentence = seedWord + " ";

		String randomWord = getRandomValue(allWordsMap.get(seedWord));

		for (int i = 0; i < numWords - 1; i++) {
			String temp = randomWord;
			if (randomWord.equals("There is not subsequent word.")) {
				sentence += seedWord + " ";
				randomWord = getRandomValue(allWordsMap.get(seedWord)); // return to seedWord
			} else {
				sentence += temp + " ";
				randomWord = getRandomValue(allWordsMap.get(temp)); // randomValue from the random word
			}
		}
		return sentence;
	}
	
	/**
	 * This method generates a sentence using the 'one' sentence generation logic. It does
	 * this by getting the most likely (based on probability) subsequent word to seedWord.
	 * From here, it adds seedWord to a String 'sentence', then checks seedWord's subsequent
	 * word. If the subsequent word does not exist, the method again adds seedWord and sets
	 * the next word to seedWord's most likely subsequent word. If the subsequent word
	 * exists, the method gets the subsequent word's most likely next word and adds it
	 * to 'sentence'. This sentence generation (seedWord, seedWord subsequent value, seedWord
	 * subsequentValue's subsequent value) continues numWords times. 
	 * 
	 * @param map - HashMap<String, HashMap<String, Integer>> object that contains every
	 * word from the text file and its corresponding HashMap with all subsequent words and
	 * the amount of times they were repeated. 
	 * @param seedWord - the first word to be added to sentence and the first word who's
	 * subsequent most likely word is added to the sentence.
	 * @param numWords - int value that dictates how many words are to be generated
	 * @return sentence - String value that contains the newly generated sentence. 
	 */
	private static String generateOne(HashMap<String, HashMap<String, Integer>> map, String seedWord, int numWords) {
		String maxWord = getMaxValue(map.get(seedWord));
		String org = maxWord;
		String sentence = seedWord + " ";
		
		for (int i = 0; i < numWords - 1; i++) {
			String temp = maxWord;
			if (maxWord.equals("There is not subsequent word.")) {
				sentence += seedWord + " "; // return to seedWord.
				maxWord = org;
			} else {
				sentence += temp + " ";
				maxWord = getMaxValue(map.get(temp)); // most likely word from the most likely word
			}
		}
		return sentence; 
	}

	/**
	 * This methods returns an ArrayList<String> with every word in the text file in 
	 * order of appearance. If there is any punctuation, only the letters that appear
	 * before the punctuation are counted. 
	 * 
	 * @param fileName - String fileName that is used to make a new File, then read
	 * using a Scanner. 
	 * @return strArr - ArrayList<String> object that contains all words in the text
	 * file in order of appearance.
	 */
	public static ArrayList<String> wordArr(String fileName) {

		File file = new File(fileName); // create new file
		Scanner sc;

		try {
			sc = new Scanner(file); // open file
		} catch (FileNotFoundException e) {
			System.out.println("The file name does not exist. ");
			return null;
		}

		sc.useDelimiter("\\s+");
		ArrayList<String> strArr = new ArrayList<>();

		while (sc.hasNext()) {
			String temp = sc.next();
			String[] ele = temp.split("\\W");
			String word;
			if (ele.length == 0) {
				continue;
			} else {
				word = ele[0].toLowerCase();
			}

			if (word.length() != 0)
				strArr.add(word);
		}

		sc.close(); // close file
		return strArr;
	}

	/**
	 * This method returns a HashMap<String, HashMap<String, Integer>> representation of
	 * all of the words in the ArrayList<String> passed in the parameters. It does this by
	 * examining an index and placing the index + 1 into the value HashMap<String, Integer> 
	 * tied to the original index's String object. From here, if there are more of the same
	 * subsequent word, the integer in the value HashMap<String, Integer> is increased
	 * by one, consequentially acting as number of the times the word appeared in relation 
	 * to the key String. 
	 * 
	 * @param words - ArrayList<String> object that contains all of the words in the text file,
	 * built with the wordArr method.
	 * @return map - HashMap<String, HashMap<String, Integer>> object that contains a key
	 * String and a value HashMap<String, Integer> containing all subsequent strings and the
	 * number of times they were repeated. 
	 */
	private static HashMap<String, HashMap<String, Integer>> buildMap(ArrayList<String> words) {
		HashMap<String, HashMap<String, Integer>> map = new HashMap<>();
		HashMap<String, Integer> entry;
		String next;

		for (int i = 0; i < words.size(); i++) {
			String current = words.get(i);
			if (i + 1 < words.size())
				next = words.get(i + 1);
			else
				next = "There is not subsequent word.";

			if (!map.containsKey(current)) { // initial case
				entry = new HashMap<>();
				entry.put(next, 1);
				map.put(current, entry);
			} else { // if its already in the map, after added
				entry = map.get(current);
				if (entry.containsKey(next)) // increase total by one if next is already in the entry
					entry.put(next, (entry.get(next) + 1));
				else // put it in the entry if not
					entry.put(next, 1);
			}
		}
		return map;
	}

	/**
	 * This method creates a HashMap<String, ArrayList<String>> object that holds a key
	 * String tied to an ArrayList<String> containing all subsequent words from the
	 * text file. If there are repeated subsequent words, they are added to the 
	 * ArrayList<String> as duplicates. 
	 * 
	 * @param words - ArrayList<String> object containing all of the words in the original 
	 * text file. 
	 * @return map - HashMap<String, ArrayList<String>> containing all words linked to 
	 * all of their subsequent words, duplicates for any repetitions. 
	 */
	private static HashMap<String, ArrayList<String>> buildAllWordsMap(ArrayList<String> words) {
		HashMap<String, ArrayList<String>> map = new HashMap<>();
		ArrayList<String> list;
		String next;

		for (int i = 0; i < words.size(); i++) {
			String current = words.get(i);
			if (i + 1 < words.size())
				next = words.get(i + 1);
			else
				next = "There is not subsequent word.";

			if (!map.containsKey(current)) { // initial case
				list = new ArrayList<>();
				list.add(next);
				map.put(current, list);
			} else {
				list = map.get(current);
				list.add(next);
				map.put(current, list);
			}

		}

		return map;

	}

	/**
	 * This method returns an ArrayList<String> object containing all of the most likely 
	 * words linked to the key String object that existed where the method was called. 
	 * The method uses Collections.sort() and a custom Comparator to sort an ArrayList 
	 * filled with Map.Entry<String, Integer> objects. From here, the method deposits 
	 * all of the key String object in each Map.Entry<String, Integer> into a separate
	 * ArrayList<String>, which is then returned. 
	 * 
	 * @param map - HashMap<String, Integer> object that contains every subsequent word 
	 * and an integer representing how many times the word was repeated. 
	 * @return sorted - ArrayList<String> object containing only the key String objects of
	 * the keys in map, in sorted order. 
	 */
	private static ArrayList<String> getMaxValues(HashMap<String, Integer> map) {
		ArrayList<Map.Entry<String, Integer>> arr = new ArrayList<>(map.entrySet());
		ArrayList<String> sorted = new ArrayList<>();

		Collections.sort(arr, new OrderByProb()); // Collections.sort() called
		
		for (Map.Entry<String, Integer> ele : arr)
			sorted.add(ele.getKey());
		
		return sorted;
	}

	/**
	 * This method return the maximum value in the HashMap<String, Integer> map by
	 * using Collections.sort() on an ArrayList<Map.Entry<String, Integer>> and 
	 * returning the key value linked to the first index, 0. 
	 * 
	 * @param map - HashMap<String, Integer> object that contains every subsequent word 
	 * and an integer representing how many times the word was repeated.
	 * @return String key of the first element in the sorted 
	 * ArrayList<Map.Entry<String, Integer>>.
	 */
	private static String getMaxValue(HashMap<String, Integer> map) {
		ArrayList<Map.Entry<String, Integer>> arr = new ArrayList<>(map.entrySet());
		Collections.sort(arr, new OrderByProb());
		return arr.get(0).getKey();
	}

	/**
	 * This method returns a String representing a random subsequent word tied to 
	 * a String word in the original text file. It does this by using the ArrayList
	 * created in the buildAllWordsMap method and generating a new random value, then
	 * returning the String at that index. As there are duplicates in the ArrayList,
	 * this verifies that the words are returned weighted on probability. 
	 * 
	 * @param words - ArrayList<String> object that contains all of the subsequent words.
	 * @return String random index of the words ArrayList<String>.
	 */
	private static String getRandomValue(ArrayList<String> words) {
		Random rng = new Random();
		int randomNum = rng.nextInt(words.size());
		return words.get(randomNum);
	}

	/**
	 * This class represents a custom Comparator<Map.Entry<String, Integer>> object
	 * that is used to sort the ArrayList<Map.Entry<String, Integer>> in the getMaxValues
	 * and getMaxValue methods.
	 * 
	 * @author Nate LeMonnier and Noah Shewell
	 * @version April 20, 2024
	 */
	protected static class OrderByProb implements Comparator<Map.Entry<String, Integer>> {

		/**
		 * This class overrides the Comparator class's 'compare' method. It returns a
		 * positive value if the probability of the second object is greater, a negative value
		 * if the probability of the first object is greater, or a value tied to Comparable's
		 * 'compareTo' method, that compares Strings lexographically. 
		 * 
		 * @param e1 - Map.Entry<String, Integer> object that is to compared
		 * @param e2 - Map.Entry<String, Integer> object that is to compared
		 * @return 1 if e2's probability is greater than e1, -1 if e2's probability is 
		 * less than e1, or a value returned by Comparable's compareTo method if e1 and e2
		 * have the same probability. 
		 */
		@Override
		public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
			if (e1.getValue() < e2.getValue())
				return 1;
			if (e1.getValue() > e2.getValue())
				return -1;
			return e1.getKey().compareTo(e2.getKey());
		}

	}
}
