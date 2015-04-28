package edu.banda.coel.core.client.rbn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import com.banda.core.util.FileUtil;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class MedianClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileUtil fu = FileUtil.getInstance();
		double sums[] = new double[21601];
		for (int i = 0; i < sums.length; i++) {
			sums[0] = 0;
		}
		try {
			
			String input = fu.readStringFromFile("input");
			StringTokenizer st = new StringTokenizer(input, "\n");
			int lines = 0;
			while (st.hasMoreTokens()) {
				String line = st.nextToken();
				StringTokenizer stl = new StringTokenizer(line);
				int index = 0;
				while (stl.hasMoreTokens()) {
					String num = stl.nextToken();
					sums[index] += Double.parseDouble(num);
					index++;
				}
				lines++;
			}
			StringBuffer outputSB = new StringBuffer();
			for (int i = 0; i < sums.length; i++) {
				outputSB.append((sums[i] / lines) + "\n");
			}
			fu.appendStringToFile(outputSB.toString(), "output");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}