package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public final class KeyValueEntryTransfer {
	private Object key;
	private Object value;

	public Object getKey() {
		return (key);
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return (value);
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public static KeyValueEntryTransfer toTransfer(Map.Entry<? extends Object, ? extends Object> entry) {
		KeyValueEntryTransfer result = new KeyValueEntryTransfer();

		result.setKey(entry.getKey());
		result.setValue(entry.getValue());

		return (result);
	}

	public static KeyValueEntryTransfer[] toTransfer(Map<? extends Object, ? extends Object> map) {
		ArrayList<KeyValueEntryTransfer> result = new ArrayList<KeyValueEntryTransfer>();
		
		for (Entry<? extends Object, ? extends Object> entry : map.entrySet())
		{
			result.add(toTransfer(entry));
		}
		
		return(result.toArray(new KeyValueEntryTransfer[0]));
	}
}