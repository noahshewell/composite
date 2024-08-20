package comprehensive;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TextGeneratorTester {

	String worldTwo;
	String abc;
	String tricky;
	String helloWorld;
	String story;
	HashMap<String, HashMap<String, Integer>> map;

	@BeforeEach
	public void setup() {
		worldTwo = "/Users/natelemonnier/eclipse-workspace/CS2420/src/comprehensive/worldTwo";
		tricky = "/Users/natelemonnier/eclipse-workspace/CS2420/src/comprehensive/tricky";
		abc = "/Users/natelemonnier/eclipse-workspace/CS2420/src/comprehensive/abc";
		helloWorld = "/Users/natelemonnier/eclipse-workspace/CS2420/src/comprehensive/helloWorld";
		story = "/Users/natelemonnier/eclipse-workspace/CS2420/src/comprehensive/story";
		
	//	map = Generator.buildMap(Generator.wordArr(worldTwo));
		
	}
//	
//	@Test
//	void testSentenceGeneratorNormalHelloWorld() {
//		String expected = "world three two one ";
//		String actual = Generator.generateSentence(worldTwo, "world", 3, "none");
//		assertEquals(expected, actual);
//	}
//	
//	@Test
//	void testSentenceGeneratorRepeatingTrickyNormal() {
//		String expected = "things yup things ";
//		String actual = Generator.generateSentence(tricky, "things", 3, "all");
//		assertEquals(expected, actual);
//	}
//	
//	@Test
//	void testTricky() {
//		//System.out.println(Generator.wordArr("/Users/natelemonnier/eclipse-workspace/CS2420/src/comprehensive/tricky").toString());
//	}
//	
//	@Test
//	void testABC() {
//		String expected = "a b c d e f g ";
//		String actual = Generator.generateSentence(abc, "a", 6, "none");
//		assertEquals(expected, actual);
//	}
//	
//	@Test
//	void testClassProvidedExample() {
//		String expected = "hello world hello world hello world hello ";
//		String actual = Generator.generateSentence(helloWorld, "hello", 7, "all");
//		assertEquals(expected, actual);
//	}
//	
//	@Test
//	void testStoryComplicatedNormal() {
//		String expected = "i walk listen ";
//		String actual = Generator.generateSentence(story, "i", 4, "none");
//		assertEquals(expected, actual);
//	}
//	
//	@Test
//	void testStoryComplicatedNormalTwo() {
//			String expected = "in autumn in autumn ";
//			String actual = Generator.generateSentence(story, "in", 4, "one");
//			assertEquals(expected, actual);
//	}
//	
//	@Test
//	void testOneCorrectlyNormal() {
//		String expected = "world three world three world three world three world ";
//		String actual = Generator.generateSentence(worldTwo, "world", 9, "one");
//		assertEquals(expected, actual);
//	}
//	
//	@Test
//	void testOneSimple() {
//		String expected = "world ";
//		String actual = Generator.generateSentence(worldTwo, "world", 1, "one");
//		assertEquals(expected, actual);
//	}
//	
//	@Test
//	void testABCSimple() {
//		String expected = "d a ";
//		String actual = Generator.generateSentence(abc, "d", 3, "none");
//		assertEquals(expected, actual);
//	}
	
	@Test
	void testMaxValueSimple() {
		Comparator<Map.Entry<String, Integer>> cmp = new OrderByProb();
		//assertEquals("three", Generator.getMaxValue(map.get("world"), cmp));
	}
	
	@Test
	void textGenerateSentenceSimple() {
		Generator.generateSentence(story, "the", 10, "none");
	}

	protected static class OrderByProb implements Comparator<Map.Entry<String, Integer>> {

		@Override
		public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
			if (e1.getValue() > e2.getValue())
				return 1;
			else if (e1.getValue() < e2.getValue())
				return -1;
			else
				return e1.getKey().compareTo(e2.getKey());
		}

	}
}
