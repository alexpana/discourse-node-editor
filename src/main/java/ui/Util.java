package ui;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Util {

	public static Map<String, Object> searchDefaults(String key) {
		Map<String, Object> result = new HashMap<>();

		for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
			if (entry.getKey().toString().toLowerCase().contains(key)) {
				result.put(entry.getKey().toString(), entry.getValue());
			}
		}

		return result;
	}
}
