package fr.pavnay.scrabble;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ScrabbleUtils {
	
	public static List<String> loadLanguages() {
		URL url = Main.class.getClassLoader().getResource("resolvers");
		File resolver = new File(url.getPath());
		String[] files = resolver.list();
		return Arrays.asList(files);
	}
	
}
