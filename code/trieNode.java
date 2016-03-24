import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class trieNode {

	private Set<trieNode> children = new HashSet<trieNode>();
	private String letter;
	private Set<Road> isRoad;

	public trieNode(String letter) {
		this.setLetter(letter);
		isRoad = new HashSet<Road>();
	}

	public Set<trieNode> getChildren() {
		return this.children;
	}

	public void add(Road r, String s[]) {
		if (s.length == 0) {
			this.isRoad.add(r);
			return;
		}
		for (trieNode t : this.children) {
			if (t.getLetter().equals(s[0])) {
				String[] newS = new String[s.length-1];
				for(int i =0; i<newS.length;i++){
					newS[i]=s[i+1];
				}
				t.add(r, newS);
				return;
			}
		}
		this.children.add(new trieNode(s[0]));
		this.add(r, s);
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public Set<Road> getR() {
		return isRoad;
	}

	public void setR(Set<Road> r) {
		this.isRoad = r;
	}

}
