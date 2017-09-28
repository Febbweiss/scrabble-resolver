package fr.pavnay.scrabble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class DictionaryBuilder {
	private static Map<String, Resolver> resolvers = new HashMap<String, Resolver>();

	public static Resolver generateResolver(Resolver resolver, File dictionary, int minimalSize, int maximalSize)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dictionary), "UTF-8"));
		String line = null;

		int total = 0;
		int valid = 0;
		while ((line = br.readLine()) != null) {
			int size = line.length();
			total++;
			if ((size >= minimalSize) && (size <= maximalSize) && (!line.contains("-")) && (!line.contains("."))
					&& (!line.contains("'"))) {
				String cleaned = StringUtils.toLowerCaseASCII(line);
				if (cleaned != null) {
					valid++;

					char[] sortedLetters = StringUtils.sortLetters(cleaned);

					char currentLetter = sortedLetters[0];

					Node currentNode = resolver.getNode(currentLetter);
					for (int i = 1; i < size; i++) {
						currentLetter = sortedLetters[i];
						resolver.updateStatistics(currentLetter);
						if (i == size - 1) {
							currentNode.getNode(currentLetter).addWord(cleaned);
						} else {
							currentNode = currentNode.getNode(currentLetter);
						}
					}
				}
			}
		}
		br.close();

		System.out.println("Total words in file : " + total + " - Valid words : " + valid);
		return resolver;
	}

	public static Enigma generateEnigma(String language, int minLength, int maxLength, char[] letters) {
		Resolver resolver;
		try {
			resolver = ScrabbleUtils.loadResolver(language);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return resolver.generateEnigma(minLength, maxLength, letters);
	}


	public static void main(String[] args) throws Exception {
		for( String language : new String[] {"english", "french"} ) {
			long avg = 0;
			for( int i = 0; i < 100; i++) {
				resolvers.clear();
				long t1 = System.currentTimeMillis();
				ScrabbleUtils.loadResolver(language);
				long t2 = System.currentTimeMillis() - t1;
				avg += t2;
			}
			System.out.println(language + " : " + (avg/100));
		}
	}
	
}
