package hello;

import java.util.Map;

public class Sheet {

	private Map<String, Integer> content = null;

	public Sheet() {
	}

	public Sheet(Map<String, Integer> content) {
		this.content = content;
	}

	public Map<String, Integer> getContent() {
		return content;
	}
}
