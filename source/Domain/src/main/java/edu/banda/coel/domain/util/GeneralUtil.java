package edu.banda.coel.domain.util;

import java.util.*;

import com.banda.core.util.ConversionUtil;
import com.banda.core.util.RandomUtil;

import edu.banda.coel.CoelRuntimeException;

/**
 * @author Peter Banda
 * @since 2011
 */
public class GeneralUtil {

	private static Map<Character, String> HTML_CHAR_ESCAPED_MAP = new HashMap<Character, String>();
	static {
		HTML_CHAR_ESCAPED_MAP.put('\b', "\\b");
		HTML_CHAR_ESCAPED_MAP.put('\f', "\\f");
		HTML_CHAR_ESCAPED_MAP.put('\n', "<br />");
		HTML_CHAR_ESCAPED_MAP.put('\t', "\\t");
		HTML_CHAR_ESCAPED_MAP.put('\'', "\\'");
		HTML_CHAR_ESCAPED_MAP.put('\"', "\\\"");
		HTML_CHAR_ESCAPED_MAP.put('\\', "\\\\");
		HTML_CHAR_ESCAPED_MAP.put('<' , "&lt;");
		HTML_CHAR_ESCAPED_MAP.put('>' , "&gt;");
		HTML_CHAR_ESCAPED_MAP.put('&' , "&amp;");
	}
	
	public static String getStringWithSpace(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public static <T> T[] expandOrShrinkArrayIfNeeded(T[] array, int requiredSize) {
		if (array == null) {
			throw new CoelRuntimeException("Array to expand can not be NULL.");
		}
		if (array.length == requiredSize) {
			return array;
		}
		return Arrays.copyOf(array, requiredSize);
	}

	public static <T extends Number> void setNullToZero(T[] array, Class<T> typeClass) {
		if (array == null) {
			return;
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				array[i] = ConversionUtil.convert(new Integer(0), typeClass);
			}
		}
	}

	public static double countHammingDistance(Double[] stateVector1, Double[] stateVector2) {
		if (stateVector1.length != stateVector2.length) {
			throw new CoelRuntimeException("The length of vectors differs: " + stateVector1.length + " vs. " + stateVector2.length);
		}
		double diff = 0;
		final int length = stateVector1.length;
		for (int i = 0; i < length; i++) {
			diff += Math.abs(stateVector1[i] - stateVector2[i]);
		}
		return diff;
	}

	private static List<Boolean> getBooleanFilledList(int size, Boolean value) {
		List<Boolean> booleanList = new ArrayList<Boolean>();
		for (int i = 0; i < size; i++) {
			booleanList.add(value);
		}
		return booleanList;
	}

	public static List<Boolean> getBooleanListWith0s(int size) {
		return getBooleanFilledList(size, Boolean.FALSE);
	}

	public static List<Boolean> getBooleanListWith1s(int size) {
		return getBooleanFilledList(size, Boolean.TRUE);
	}

	public static List<Boolean> getBooleanListWithSingle1(int size) {
		List<Boolean> list = getBooleanFilledList(size, Boolean.FALSE);
		int pos = RandomUtil.nextInt(0, list.size() - 2);
		list.set(pos, Boolean.TRUE);
		list.set(pos + 1, Boolean.TRUE);
		return list;
	}

	/**
	 * Checks if there is not-NULL element at given index in array.
	 * If the element does not exist, <code>RuntimeException</code> is thrown
	 * 
	 * @param indexOfElement
	 * @param array
	 */
	public static void checkElement(int indexOfElement, Object[] array) {
		if (array == null
		|| array.length <= indexOfElement
		|| array[indexOfElement] == null) {
			throw new RuntimeException("Element with index " + indexOfElement + " does not exist in array " + array + "!");
		}
	}

	/**
	 * Converts wildcards contained in given String to JAVA regular expressions.
	 * 
	 * @param string The String to play with
	 * @return The converted String with regular expressions instead of wildcards
	 */
	public static String convertWildcardsToRegex(String string) {
		if (string == null) {
			return string;
		}
		int liLength = string.length();
		StringBuffer lSb = new StringBuffer(liLength);
		lSb.append('^');
		for (int i = 0; i < liLength; i++) {
			char c = string.charAt(i);
			switch (c) {
			case '*':
				lSb.append(".*");
				break;
			case '?':
				lSb.append(".");
				break;
			case '(':
			case ')':
			case '[':
			case ']':
			case '$':
			case '^':
			case '.':
			case '{':
			case '}':
			case '|':
			case '\\':
				lSb.append("\\");
				lSb.append(c);
				break;
			default:
				lSb.append(c);
				break;
			}
		}
		lSb.append('$');
		return (lSb.toString());
	}

	/**
	 * Check if the text matches case insensitively given Regex.
	 * 
	 * @param text The text to check the matching
	 * @param regex The regex that should be valid for given text
	 * @return The result of test
	 */
	public static boolean matchCaseInsensitive(String text, String regex) {
		if (regex == null) {
			return true;
		}
		if (text != null) {
			text = text.toUpperCase();
			regex = regex.toUpperCase();
			return text.matches(regex);
		}
		return false;
	}

	/**
	 * Gets the comma list for given values.
	 * 
	 * @param values The values to handle as simple String
	 * @param rowCharacterCount The row character number what will indicate the new line should be added
	 * @return The comma list for given values
	 */
	public static String createCommaList(List<?> values, int rowCharacterCount) {
		StringBuffer lSB = new StringBuffer();
		StringBuffer lRowSB = new StringBuffer();
		int liValuesCount = values.size();
		int li = 0;
		for (Object lValue : values) {
			lRowSB.append(lValue.toString());
			if (li < liValuesCount - 1) {
				lRowSB.append(", ");
			}
			if (lRowSB.length() > rowCharacterCount) {
				lSB.append(lRowSB);
				lSB.append("\r\n");
				lRowSB = new StringBuffer();
			}
			li++;
		}
		lSB.append(lRowSB);
		return lSB.toString();
	}

	/**
	 * Gets the comma list for given values.
	 * 
	 * @param values  The values to handle as simple String
	 * @return The comma list for given values
	 */
	public static String createCommaList(List<?> values) {
		StringBuffer lSB = new StringBuffer();
		if (values != null) {
			for (Object lValue : values) {
				lSB.append(lValue.toString());
				lSB.append(", ");
			}
			if (lSB.length() > 0) {
				lSB.delete(lSB.length() - 2, lSB.length() - 1);
			}
		}
		return lSB.toString();
	}

	public static String escapeHTML(String s) {
		StringBuilder buffer = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != '\r') {
				final String escaped = HTML_CHAR_ESCAPED_MAP.get(c);
				buffer.append(escaped != null ? escaped : c);
			}			
		}
		return buffer.toString();
	}

	public static void main(String args[]) {
		Double[] expandedArray = expandOrShrinkArrayIfNeeded(new Double[0], 1);
		setNullToZero(expandedArray, Double.class);
		System.out.println(Arrays.toString(expandedArray));
	}
}
