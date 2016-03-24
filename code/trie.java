import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class trie {
	private int i = 0;
	private int count;
	private trieNode root;

	public trie() {
		root = new trieNode(null);
	}

	public trieNode getRoot() {
		return root;
	}

	public void add(Road r) {
		root.add(r, r.getLabel().split(""));
		count = count + r.getLabel().length();
	}

	public void printAll(trieNode n) {
		for (trieNode i : n.getChildren()) {
			printAll(i);

			for (trieNode r : n.getChildren()) {
				System.out.println("3 " + r.getLetter());
			}

		}
	}

	public int size() {
		return this.count;
	}

	public Set<Road> contains(String[] s, trieNode pointer) {
		if (pointer == root) {
			i = 0;
		}
		for (trieNode t : pointer.getChildren()) {
			if (i == s.length - 1 && s[i].equalsIgnoreCase(t.getLetter())) {
				return allChildren(t);
			}
			if (s[i].equalsIgnoreCase(t.getLetter())) {
				i++;
				return contains(s, t);
			}
		}
		return null;
	}

	public Set<Road> allChildren(trieNode n) {
		
		Set<Road> allRoadsWithPrefix = new HashSet<Road>();

		for (trieNode t : n.getChildren()) {
			allRoadsWithPrefix.addAll(allChildren(t));
			if (t.getR().size() >= 1) {
				for (Road r : t.getR()) {
					allRoadsWithPrefix.add(r);
				}
			}
		}
		return allRoadsWithPrefix;

	}
}
