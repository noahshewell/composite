package comprehensive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;

import assign08.TimerTemplate;
import assign08.TimerTemplate.Result;

public class Timer extends TimerTemplate {
	TextGenerator gen;
	String fileName;
	String seedWord;
	String numWords;
	String all;

	public Timer(int[] problemSizes, int timesToLoop) {
		super(problemSizes, timesToLoop);

	}

	@Override
	protected void setup(int n) {
		fileName = "/Users/natelemonnier/eclipse-workspace/CS2420/src/comprehensive/bible";
		seedWord = "at";
		numWords = "30";
		all = "all";
	}

	@Override
	protected void timingIteration(int n) {
		String[] args = {fileName, seedWord, "" + n, all};
		TextGenerator.main(args);
	}

	@Override
	protected void compensationIteration(int n) {
		String[] args = new String[4];
	}

	public static void main(String args[]) {

		ArrayList<Integer> ns = new ArrayList<>();
		for (double n = 10000; n <= 300000; n += 10000) {
			ns.add((int) n);
		}

		// convert to int[]
		int[] problemSizes = new int[ns.size()];
		for (int i = 0; i < problemSizes.length; i++) {
			problemSizes[i] = ns.get(i);
		}

		var timer = new Timer(problemSizes, 1);
		var results = timer.run();
		System.out.println("n, time");
		for (var result : results) {
			System.out.println(result.n() + ", " + result.avgNanoSecs());
		}

	}

}
