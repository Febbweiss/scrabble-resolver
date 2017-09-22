package fr.pavnay.scrabble;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;

import fr.pavnay.scrabble.impl.HashNodeImpl;
import fr.pavnay.scrabble.serializer.NodeSerializer;

public class ScrabbleUtils {
	
	public static List<String> loadLanguages() {
		URL url = Main.class.getClassLoader().getResource("resolvers");
		File resolver = new File(url.getPath());
		String[] files = resolver.list();
		return Arrays.asList(files);
	}
	
	public static Resolver loadResolver(String language) {
		long t1 = System.currentTimeMillis();
		String path = DictionaryBuilder.class.getResource("/resolvers/" + language).getPath();
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("No resolver for " + language);
			return null;
		}
		
		Resolver resolver = null;
		
		try {
			ObjectMapper mapper = getObjectMapper();
			resolver = mapper.readerFor(Resolver.class).readValue(loadFile(file) );
		} catch(IOException ioe) {
			
		}
		System.out.println("Resolver in " + language + " loaded in " + (System.currentTimeMillis() - t1) + "ms.");
		return resolver;
	}
	
	public static boolean writeResolver(Resolver resolver, String language) {
		ObjectMapper mapper = getObjectMapper();
	    
		try {
		    String serialized = mapper.writeValueAsString(resolver);
		    writeFile(serialized.getBytes(), language);
		}catch(IOException e) {
			return false;
		}
		
	    return true;
	}
	
	private static void writeFile(byte[] content, String language) throws IOException {

	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(content);
        gzipOutputStream.close();
        
        FileOutputStream out = new FileOutputStream( "src/main/resources/resolvers/" + language );
        out.write(byteArrayOutputStream.toByteArray());
	    out.flush();
	    out.close();
	}

	
	private static InputStream loadFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int count = 0;
	    while ((count = fis.read(buffer))!=-1){                  
            out.write(buffer, 0, count);
        }      
	    byte[] b = out.toByteArray();
	    fis.close();
	    
	    return new GZIPInputStream(new ByteArrayInputStream(b));
	}
	
	private static ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		
		SimpleModule module = new SimpleModule();

		SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
		resolver.addMapping(Node.class, HashNodeImpl.class);

		module.setAbstractTypes(resolver);
		module.addSerializer(Node.class, new NodeSerializer());
		mapper.registerModule(module);
		
		return mapper;
	}
	
}
