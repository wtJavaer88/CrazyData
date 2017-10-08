package javlib.utils;

import java.util.ArrayList;
import java.util.List;

import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class SpiderLogMgr {
	public static List<String> existUrls;
	public static List<String> retryUrls;
	static {
		// existUrls = readFile("starmovieurl.txt");
		existUrls = readFile2("seeked-log.txt");
		retryUrls = readFile("retrylog.txt");
	}

	private static List<String> readFile(String fileName) {
		try {
			return FileOp.readFrom(spiderqueue.util.QueueConfig.logPath + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	private static List<String> readFile2(String fileName) {
		List<String> list = new ArrayList<String>();
		try {
			List<String> readFrom = FileOp.readFrom(fileName);
			for (String string : readFrom) {
				list.add(PatternUtil.getLastPattern(string, "\\d+"));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static boolean isExist(String url) {
		return existUrls.contains(url);
	}

	public static boolean needRetry(String url) {
		return retryUrls.contains(url);
	}
}
