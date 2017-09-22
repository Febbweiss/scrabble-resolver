package fr.pavnay.scrabble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
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

	public static Enigma generateEnigma(String language, int minLength, int maxLength) {
		Resolver resolver;
		try {
			resolver = loadResolver(language);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return resolver.generateEnigma(minLength, maxLength);
	}

	public static void generateResolver(String language) throws IOException {
		File file = new File(
				"C:/Users/viaduc105.smallbusiness/Documents/workspace-sts/SocialArena/resources/" + language);
		if (!file.exists()) {
			System.out.println("No dictionary for " + language);
			return;
		}
		Resolver resolver = generateResolver(new Resolver(), file, 3, 7);
		resolver.computeStatistics();

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File("C:/Users/viaduc105.smallbusiness/Documents/workspace-sts/SocialArena/resources/resolver/"
						+ language)));
		oos.writeObject(resolver);
		oos.flush();
		oos.close();
	}

	public static void loadResolvers() throws IOException, ClassNotFoundException {
		List<String> languages = ScrabbleUtils.loadLanguages();
		for (String language : languages) {
			long t1 = System.currentTimeMillis();
			loadResolver(language);
			System.out.println("Resolver in " + language + " loaded in " + (System.currentTimeMillis() - t1) + "ms.");
		}
	}

	private static Resolver loadResolver(String language)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		Resolver resolver = (Resolver) resolvers.get(language);
		if (resolver == null) {
			long t1 = System.currentTimeMillis();
			String path = DictionaryBuilder.class.getResource("/resolvers/" + language).getPath();
			File file = new File(path);
			if (!file.exists()) {
				System.out.println("No resolver for " + language);
				return null;
			}
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			resolver = (Resolver) ois.readObject();
			ois.close();

			resolvers.put(language, resolver);
			System.out.println("Resolver in " + language + " loaded in " + (System.currentTimeMillis() - t1) + "ms.");
		}
		return resolver;
	}
}
