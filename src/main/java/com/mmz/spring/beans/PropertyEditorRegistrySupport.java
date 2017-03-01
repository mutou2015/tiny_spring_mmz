package com.mmz.spring.beans;

import java.beans.PropertyEditor;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;


import org.xml.sax.InputSource;

import com.mmz.spring.beans.propertyeditor.CustomNumberEditor;

public class PropertyEditorRegistrySupport {
	
	private boolean defaultEditorsActive = false;
	
	private Map<Class<?>, PropertyEditor> defaultEditors;

	private Map<Class<?>, PropertyEditor> overriddenDefaultEditors;
	
	public PropertyEditor getDefaultEditor(Class<?> requiredType) {
		if (!this.defaultEditorsActive) {
			return null;
		}
		if (this.overriddenDefaultEditors != null) {
			PropertyEditor editor = this.overriddenDefaultEditors.get(requiredType);
			if (editor != null) {
				return editor;
			}
		}
		if (this.defaultEditors == null) {
			createDefaultEditors();
		}
		return this.defaultEditors.get(requiredType);
	}
	
	/**
	 * Actually register the default editors for this registry instance.
	 */
	private void createDefaultEditors() {
		this.defaultEditors = new HashMap<Class<?>, PropertyEditor>(64);

		// Simple editors, without parameterization capabilities.
		// The JDK does not contain a default editor for any of these target types.
//		this.defaultEditors.put(Charset.class, new CharsetEditor());
//		this.defaultEditors.put(Class.class, new ClassEditor());
//		this.defaultEditors.put(Class[].class, new ClassArrayEditor());
//		this.defaultEditors.put(Currency.class, new CurrencyEditor());
//		this.defaultEditors.put(File.class, new FileEditor());
//		this.defaultEditors.put(InputStream.class, new InputStreamEditor());
//		this.defaultEditors.put(InputSource.class, new InputSourceEditor());
//		this.defaultEditors.put(Locale.class, new LocaleEditor());
//		this.defaultEditors.put(Pattern.class, new PatternEditor());
//		this.defaultEditors.put(Properties.class, new PropertiesEditor());
//		this.defaultEditors.put(Resource[].class, new ResourceArrayPropertyEditor());
//		this.defaultEditors.put(TimeZone.class, new TimeZoneEditor());
//		this.defaultEditors.put(URI.class, new URIEditor());
//		this.defaultEditors.put(URL.class, new URLEditor());
//		this.defaultEditors.put(UUID.class, new UUIDEditor());
//		if (zoneIdClass != null) {
//			this.defaultEditors.put(zoneIdClass, new ZoneIdEditor());
//		}
//
//		// Default instances of collection editors.
//		// Can be overridden by registering custom instances of those as custom editors.
//		this.defaultEditors.put(Collection.class, new CustomCollectionEditor(Collection.class));
//		this.defaultEditors.put(Set.class, new CustomCollectionEditor(Set.class));
//		this.defaultEditors.put(SortedSet.class, new CustomCollectionEditor(SortedSet.class));
//		this.defaultEditors.put(List.class, new CustomCollectionEditor(List.class));
//		this.defaultEditors.put(SortedMap.class, new CustomMapEditor(SortedMap.class));
//
//		// Default editors for primitive arrays.
//		this.defaultEditors.put(byte[].class, new ByteArrayPropertyEditor());
//		this.defaultEditors.put(char[].class, new CharArrayPropertyEditor());
//
//		// The JDK does not contain a default editor for char!
//		this.defaultEditors.put(char.class, new CharacterEditor(false));
//		this.defaultEditors.put(Character.class, new CharacterEditor(true));
//
//		// Spring's CustomBooleanEditor accepts more flag values than the JDK's default editor.
//		this.defaultEditors.put(boolean.class, new CustomBooleanEditor(false));
//		this.defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));

		// The JDK does not contain default editors for number wrapper types!
		// Override JDK primitive number editors with our own CustomNumberEditor.
		this.defaultEditors.put(byte.class, new CustomNumberEditor(Byte.class, false));
		this.defaultEditors.put(Byte.class, new CustomNumberEditor(Byte.class, true));
		this.defaultEditors.put(short.class, new CustomNumberEditor(Short.class, false));
		this.defaultEditors.put(Short.class, new CustomNumberEditor(Short.class, true));
		this.defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, false));
		this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
		this.defaultEditors.put(long.class, new CustomNumberEditor(Long.class, false));
		this.defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, true));
		this.defaultEditors.put(float.class, new CustomNumberEditor(Float.class, false));
		this.defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, true));
		this.defaultEditors.put(double.class, new CustomNumberEditor(Double.class, false));
		this.defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, true));
		this.defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
		this.defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));

//		// Only register config value editors if explicitly requested.
//		if (this.configValueEditorsActive) {
//			StringArrayPropertyEditor sae = new StringArrayPropertyEditor();
//			this.defaultEditors.put(String[].class, sae);
//			this.defaultEditors.put(short[].class, sae);
//			this.defaultEditors.put(int[].class, sae);
//			this.defaultEditors.put(long[].class, sae);
//		}
	}
	
	public void registerDefaultEditors() {
		this.defaultEditorsActive = true;
	}

}
