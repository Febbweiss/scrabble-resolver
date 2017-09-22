package fr.pavnay.scrabble;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Enigma implements Serializable {
	private static final long serialVersionUID = 7542854436817730656L;
	private int min;
	private int max;
	private char[] letters;
	private Map<Integer, List<String>> words;

	public Enigma(char[] letters, Map<Integer, List<String>> words, int min, int max) {
		this.letters = letters;
		this.words = words;
		this.min = min;
		this.max = max;
	}

	public List<String> getLetters() {
		List<String> lLetters = new ArrayList<String>();
		if (this.letters == null) {
			return lLetters;
		}
		for (int i = 0; i < this.letters.length; i++) {
			lLetters.add(Character.toString(this.letters[i]));
		}
		return lLetters;
	}

	public Map<Integer, List<String>> getWords() {
		return this.words;
	}

	public boolean isValid(String word) {
		if (StringUtils.isBlank(word)) {
			return false;
		}
		List<String> lWords = this.words.get(Integer.valueOf(word.length()));
		return lWords.contains(word);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Letters : ");
		if (this.letters != null) {
			sb.append(new String(this.letters));
		} else {
			sb.append("-");
		}
		sb.append("\nWords between ").append(this.min).append(" and ").append(this.max).append(" letters : \n");
		for (int i = this.max; i > this.min - 1; i--) {
			List<String> lWords = this.words.get(Integer.valueOf(i));
			if (lWords != null) {
				sb.append("\twith ").append(i).append(" letters (" + lWords.size() + "): ").append(lWords).append('\n');
			}
		}
		return sb.toString();
	}
}
