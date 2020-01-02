package hello;

import java.util.Map;

public class Sheet {

	private final Map<String, Integer>  content;

	public Sheet(Map<String, Integer>  content) {
		this.content = content;
	}

	public Map<String, Integer> getContent() {
		return content;
	}
}
