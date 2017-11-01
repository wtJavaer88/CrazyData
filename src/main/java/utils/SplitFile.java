package utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class SplitFile {
	private static final String IMPLEMENTS = " implements ";
	private static final String EXTENDS = " extends ";

	public static void main(String[] args) {
		List<String> readFrom = FileOp.readFrom("D:/dianping.java");
		String fileName = "";
		List<String> lines = new ArrayList<String>(100);
		for (String string : readFrom) {
			// System.out.println( string );
			if (string.startsWith("package ")) {
				save(lines, fileName);
			}
			lines.add(string);
			if (string.startsWith("public class ") || string.startsWith("public abstract class ")
					|| string.startsWith("public enum ") || string.startsWith("public interface ")) {
				string = string.replace("{", "");
				if (!string.contains(IMPLEMENTS) && !string.contains(EXTENDS)) {
					fileName = PatternUtil.getLastPatternGroup(string, "(\\S+)");
				} else if (string.contains(IMPLEMENTS)) {
					fileName = PatternUtil.getLastPatternGroup(string, "(\\S+) implements");
				} else if (string.contains(EXTENDS)) {
					fileName = PatternUtil.getLastPatternGroup(string, "(\\S+) extends");
				}
			}
		}
		save(lines, fileName);
	}

	static long t = System.currentTimeMillis();
	static {
		BasicFileUtil.makeDirectory("D:/" + t);
	}

	private static void save(List<String> lines, String fileName) {
		System.out.println(fileName);
		if (!lines.isEmpty()) {
			BasicFileUtil.writeFileString("D:/" + t + "/" + fileName + ".java", StringUtils.join(lines, "\r\n"), null,
					false);
			lines.clear();
		}
	}
}