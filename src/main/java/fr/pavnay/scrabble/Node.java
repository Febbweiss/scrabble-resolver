package fr.pavnay.scrabble;

import java.io.Serializable;
import java.util.List;

public interface Node extends Serializable {
	
	public void addWord(String word);
	public List<String> getWords();
	public Node getNode(char character);
	public Object getNodes();
	public int getWordsCount();

}
