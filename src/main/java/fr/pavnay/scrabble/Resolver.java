package fr.pavnay.scrabble;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Resolver implements Serializable {
	private static final long serialVersionUID = 8267126995323709570L;
	private Node[] nodes = new Node[26];
	private float[] statistics = new float[26];
	private float totalCharacter = 0.0F;

	public Node getNode(char character) {
		Node node = this.nodes[(character - 'a')];
		this.statistics[(character - 'a')] += 1.0F;
		this.totalCharacter += 1.0F;
		if (node == null) {
			node = new Node(Character.toString(character));
			this.nodes[(character - 'a')] = node;
		}
		return node;
	}

	public void updateStatistics(char character) {
		this.statistics[(character - 'a')] += 1.0F;
		this.totalCharacter += 1.0F;
	}

	public void computeStatistics() {
		for (int i = 0; i < 26; i++) {
			this.statistics[i] = (this.statistics[i] * 100.0F / this.totalCharacter);
			System.out.println((char) (i + 97) + "=" + this.statistics[i] + "%");
		}
	}

	public void displayStatistics() {
		for (int i = 0; i < 26; i++) {
			System.out.println((char) (i + 97) + "=" + this.statistics[i] + "%");
		}
	}

	public float[] getStatistics() {
		return this.statistics;
	}

	public Enigma generateEnigma(int minimalLettersCount, int maximalLettersCount) {
		boolean mustRun = false;
		char[] letters;
		Map<Integer, List<String>> allWords;
		do {
			letters = getRandomSet(maximalLettersCount);
			allWords = getWords(letters, minimalLettersCount);

			int wordsCount = 0;
			for (int i = minimalLettersCount; i < maximalLettersCount + 1; i++) {
				List<String> words = allWords.get(Integer.valueOf(i));
				if (words != null) {
					wordsCount += words.size();
				}
			}
			mustRun = wordsCount < (maximalLettersCount + minimalLettersCount) * 0.7D;
		} while (mustRun);
		return new Enigma(letters, allWords, minimalLettersCount, maximalLettersCount);
	}

	private char[] getRandomSet(int maxLetter) {
		float[] statistics = getStatistics();
		float max = 0.0F;
		for (int i = 0; i < 26; i++) {
			max += statistics[i];
		}
		Random randomizer = new Random();
		char[] letters = new char[maxLetter];
		for (int i = 0; i < maxLetter; i++) {
			letters[i] = getRandomLetter(randomizer, max, statistics);
		}
		return letters;
	}

	private char getRandomLetter(Random randomizer, float max, float[] statistics) {
		float r = randomizer.nextFloat() * (int) max;
		max = 0.0F;
		for (int i = 0; i < 26; i++) {
			max += statistics[i];
			if (r < max) {
				return (char) (i + 97);
			}
		}
		return 'z';
	}

	private Map<Integer, List<String>> getWords(char[] letters, int minimum) {
		List<String> words = new ArrayList<String>();
		for (int i = letters.length; i > 2; i--) {
			List<String> newWords = getWords(letters, minimum, i);
			if (newWords != null) {
				for (String word : newWords) {
					if (!words.contains(word)) {
						words.add(word);
					}
				}
			}
		}
		Map<Integer, List<String>> lists = new HashMap<Integer, List<String>>();
		for (String word : words) {
			List<String> list = lists.get(Integer.valueOf(word.length()));
			if (list == null) {
				list = new ArrayList<String>();
				lists.put(Integer.valueOf(word.length()), list);
			}
			list.add(word);
		}
		for (int i = letters.length; i > 2; i--) {
			List<String> list = lists.get(Integer.valueOf(i));
			if (list != null) {
				Collections.sort(list);
			}
		}
		return lists;
	}

	public List<String> getWords(char[] letters, int minimum, int size) {
		if (size < minimum) {
			return null;
		}
		String set = new String(letters);
		char[] sortLetters = StringUtils.sortLetters(set);

		int setSize = letters.length;
		if (size == setSize) {
			Node currentNode = this.nodes[(sortLetters[0] - 'a')];
			if (currentNode == null) {
				return null;
			}
			for (int i = 1; i < setSize; i++) {
				currentNode = currentNode.getNode(sortLetters[i]);
				if (currentNode == null) {
					return null;
				}
				if (i == setSize - 1) {
					return currentNode.getWords();
				}
			}
			return null;
		}
		String nString = new String(sortLetters);
		List<String> results = null;
		for (int i = 0; i < setSize - size; i++) {
			for (int j = 0; j < setSize; j++) {
				List<String> words = getWords(
						(nString.substring(0, j) + nString.subSequence(j + 1, setSize)).toCharArray(), minimum,
						size - i);
				if (words != null) {
					if (results == null) {
						results = new ArrayList<String>();
					}
					for (String word : words) {
						if (!results.contains(word)) {
							results.add(word);
						}
					}
				}
			}
		}
		if (results != null) {
			Collections.sort(results);
		}
		return results;
	}

	public int countWords() {
		int total = 0;
		for (int i = 0; i < 26; i++) {
			Node current = this.nodes[i];
			if (current != null) {
				total += current.getWordsCount();
			}
		}
		return total;
	}
}
