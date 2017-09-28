package fr.pavnay.scrabble;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	private final static List<String> availableLanguages = ScrabbleUtils.loadLanguages();
	private final static Options helpOptions = configHelpParameter();
	private final static Options options = configParameters();
	
	public static void main(String[] args) {
		//achilst
		final CommandLineParser parser = new DefaultParser();
		CommandLine firstLine = null;
		try {
			firstLine = parser.parse(helpOptions, args, true);
		} catch (ParseException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		boolean helpMode = firstLine.hasOption("help");
		if (helpMode) {
		    final HelpFormatter formatter = new HelpFormatter();
		    formatter.printHelp("Main", options, true);
		    System.exit(0);
		}
		
		try {
			CommandLine line = parser.parse(options, args);
			System.out.println(line.getArgList());
			if( line.hasOption( "build" ) ) {
				generate(line);
			} else {
				getEnigma(line);
			}
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

	}
	
	private static void generate(CommandLine line) {
		final String source = line.getOptionValue("build", "");
		final String language = line.getOptionValue("lang", "");
		final int min = Integer.parseInt(line.getOptionValue("min", "3"));
		final int max = Integer.parseInt(line.getOptionValue("max", "7"));

		System.out.println(String.format("Generating %s resolver from %s.\nWords size: [%d, %d]", language, source, min, max ));
		
		File file = new File(source);
	    if (!file.exists())
	    {
	      System.err.println("Source file not found " + source);
	      return;
	    }
	    
	    try {
		    Resolver resolver = DictionaryBuilder.generateResolver(new Resolver(), file, min, max);
		    resolver.computeStatistics();
		    resolver.displayStatistics();
		    
		    ScrabbleUtils.writeResolver(resolver, language);
	    } catch( IOException e) {
	    	System.err.println(e.getMessage());
	    }
	}
	
	
	private static void getEnigma(CommandLine line) {
		final String language = line.getOptionValue("lang", "");
		final int min = Integer.parseInt(line.getOptionValue("min", "3"));
		final int max = Integer.parseInt(line.getOptionValue("max", "7"));
		char[] letters = null;
		if( line.getArgList().size() == 1 ) {
			letters = line.getArgList().get(0).toCharArray();
		}
		try {
			manageLanguage(language);
			Enigma enigma = DictionaryBuilder.generateEnigma(language, min, max, letters);
			System.out.println(enigma);
		} catch(IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}
	
	private static Options configHelpParameter() {
		 final Option helpFileOption = Option.builder("h") 
				.longOpt("help")
				.desc("Display help message").build();

	    final Options firstOptions = new Options();

	    firstOptions.addOption(helpFileOption);

	    return firstOptions;
	}
	
	private static Options configParameters() {
		
		final Option buildOption = Option.builder("b")
				.longOpt("build") //
				.desc("Build a new dictionary from the source file (a Linux words file)")
				.hasArg(true)
				.argName("source")
				.required(false)
				.build();

		final Option languageOption = Option.builder("l")
				.longOpt("lang")
				.desc("Language (in " + availableLanguages + ")")
				.hasArg(true)
				.argName("language")
				.required(true)
				.build();
		
		final Option helpFileOption = Option.builder("h") 
				.longOpt("help")
				.desc("Display help message").build();

		final Option minOption = Option.builder("min")
				.longOpt("min")
				.desc("Minimum word length (default : 3)")
				.hasArg(true)
				.argName("min")
				.required(false)
				.build();
		
		final Option maxOption = Option.builder("max")
				.longOpt("max")
				.desc("Maximum word length (default : 7)")
				.hasArg(true)
				.argName("max")
				.required(false)
				.build();

		final Options options = new Options();

		options.addOption(buildOption);
		options.addOption(helpFileOption);
		options.addOption(languageOption);
		options.addOption(minOption);
		options.addOption(maxOption);

		return options;
	}
	
	private static void manageLanguage(String language) {
		if( StringUtils.isBlank(language) ) {
			throw new IllegalArgumentException("No language provided");
		}
		
		if( !availableLanguages.contains(language.toLowerCase()) ) {
			throw new IllegalArgumentException(String.format("Unknown %s language. Available languages : %s", language, availableLanguages.toString()));
		}
	}
}
