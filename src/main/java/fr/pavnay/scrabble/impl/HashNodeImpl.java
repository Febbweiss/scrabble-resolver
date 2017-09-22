package fr.pavnay.scrabble.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.pavnay.scrabble.Node;

public class HashNodeImpl implements Node {
	
	private static final long serialVersionUID = 4221818951064573126L;
	
	private Map<Character,Node> nodes = new HashMap<Character, Node>();
	private List<String> words;
	private String parent;

	public HashNodeImpl() {
	}
	
	public HashNodeImpl(String parent) {
		this.parent = parent;
	}

	public void addWord(String word) {
		if (this.words == null) {
			this.words = new ArrayList<String>();
		}
		if (!this.words.contains(word)) {
			this.words.add(word);
		}
	}

	public List<String> getWords() {
		if (this.words == null) {
			return null;
		}
		Collections.sort(this.words);
		return this.words;
	}

	public Map<Character, Node> getNodes() {
		return nodes;
	}
	
	public Node getNode(char character) {
		Node node = this.nodes.get(character);
		if (node == null) {
			node = new HashNodeImpl(this.parent + character);
			nodes.put(character, node);
		}
		return node;
	}

	public int getWordsCount() {
		int total = 0;
		
		for(Map.Entry<Character, Node> node : nodes.entrySet()) {
			total += node.getValue().getWordsCount();
		}
		if (this.words != null) {
			total += this.words.size();
		}
		
		return total;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Parent " + this.parent);
		if (this.words != null) {
			sb.append(this.words.toString());
		} else {
			sb.append('-');
		}
		for(Map.Entry<Character, Node> node : nodes.entrySet()) {
			sb.append(node.getValue());
		}
		return sb.toString();
	}
}
