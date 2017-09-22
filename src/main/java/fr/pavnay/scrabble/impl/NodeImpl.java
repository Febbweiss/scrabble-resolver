package fr.pavnay.scrabble.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.pavnay.scrabble.Node;

public class NodeImpl implements Node {
	private static final long serialVersionUID = 6637351994367583747L;

	public Node[] nodes = new Node[26];
	public List<String> words;
	private String parent;
	
	public NodeImpl() {}

	public NodeImpl(String parent) {
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

	public Node[] getNodes() {
		return this.nodes;
	}
	
	public Node getNode(char character) {
		Node node = this.nodes[(character - 'a')];
		if (node == null) {
			node = new NodeImpl(this.parent + character);
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
