package fr.pavnay.scrabble;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node implements Serializable {
	private static final long serialVersionUID = 3539215265338287172L;
	private Node[] nodes = new Node[26];
	private List<String> words;
	private String parent;

	public Node(String parent) {
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

	public Node getNode(char character) {
		Node node = this.nodes[(character - 'a')];
		if (node == null) {
			node = new Node(this.parent + character);
			this.nodes[(character - 'a')] = node;
		}
		return node;
	}

	public int getWordsCount() {
		int total = 0;
		for (int i = 0; i < 26; i++) {
			Node current = this.nodes[i];
			if (current != null) {
				total += current.getWordsCount();
			}
			if (this.words != null) {
				total += this.words.size();
			}
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
		for (int i = 0; i < 26; i++) {
			Node current = this.nodes[i];
			if (current != null) {
				sb.append(current);
			}
		}
		return sb.toString();
	}
}
