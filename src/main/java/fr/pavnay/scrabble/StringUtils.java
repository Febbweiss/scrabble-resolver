package fr.pavnay.scrabble;

import java.text.Normalizer;
import java.util.Arrays;

public final class StringUtils {
	
	public static String toLowerCaseASCII(String s) {
		if (isBlank(s)) {
			return "";
		}
		return stripAccents(s).trim().toLowerCase();
	}

	public static boolean isBlank(String s) {
		return (s == null) || (s.trim().length() == 0);
	}

	public static char[] sortLetters(String s) {
		if (s == null) {
			return null;
		}
		if (isBlank(s)) {
			return s.toCharArray();
		}
		char[] sortedLetters = s.toCharArray();
		Arrays.sort(sortedLetters);

		return sortedLetters;
	}

	public static String stripAccents(String s) {
	    s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").replaceAll("[^\\p{ASCII}]", "");
	    return s;
	}
}
