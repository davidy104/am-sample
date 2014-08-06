package com.aucklandmuseum.image;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

@Component
public class XMLToMapConverter implements Converter {
	private static final String VALUE_ATTRIBUTE_NAME = "__value__";

	@Override
	public boolean canConvert(final Class clazz) {
		return AbstractMap.class.isAssignableFrom(clazz);
	}

	@Override
	public void marshal(final Object value,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object unmarshal(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		return unmarshalRec(reader);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> unmarshalRec(
			final HierarchicalStreamReader reader) {
		final Map<String, Object> res = new HashMap<>();
		res.putAll(XMLToMapConverter.getAttributes(reader));
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			Object newValue;
			final String key = reader.getNodeName();
			final Object oldValue = res.get(key);
			final Object tmpValue = getValue(reader,
					XMLToMapConverter.getAttributes(reader));
			if (oldValue != null) {
				if (oldValue instanceof List) {
					((List<Object>) oldValue).add(tmpValue);
					newValue = oldValue;
				} else {
					newValue = new ArrayList<>();
					((List<Object>) newValue).add(oldValue);
					((List<Object>) newValue).add(tmpValue);
				}
			} else {
				newValue = tmpValue;
			}

			res.put(key, newValue);
			reader.moveUp();
		}

		return res;
	}

	@SuppressWarnings("unchecked")
	private Object getValue(final HierarchicalStreamReader reader,
			final Map<String, Object> attributes) {
		Object value;
		if (reader.hasMoreChildren()) {
			value = unmarshalRec(reader);
			((Map<String, Object>) value).putAll(attributes);
		} else {
			final String stringValue = reader.getValue();
			if (!attributes.isEmpty()) {
				attributes.put(VALUE_ATTRIBUTE_NAME, stringValue);
				value = attributes;
			} else {
				value = stringValue;
			}
		}

		return value;
	}

	private static Map<String, Object> getAttributes(
			final HierarchicalStreamReader reader) {
		final Map<String, Object> attributes = new HashMap<>();
		if (reader.getAttributeCount() > 0) {
			final Iterator<String> attributeNames = reader.getAttributeNames();
			while (attributeNames.hasNext()) {
				final String attributeName = attributeNames.next();
				attributes.put("@" + attributeName,
						reader.getAttribute(attributeName));
			}
		}

		return attributes;
	}

}
