package zhihu.util;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.crawl.core.util.HttpClientUtil;
import com.wnc.string.PatternUtil;

public class ZhihuAuthUtil {
	private static ZhihuAuthUtil zhihuAuthUtil = new ZhihuAuthUtil();
	private static String authorization = "";

	private ZhihuAuthUtil() {
	}

	public static ZhihuAuthUtil getInstance() {
		return zhihuAuthUtil;
	}

	public static String getAuthorization() {
		return authorization;
	}

	public String initAuthorization() {
		String content = null;
		try {
			content = HttpClientUtil.getWebPage("https://www.zhihu.com/people/wo-yan-chen-mo/following");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String jsSrc = getJsSrc(content);
		if (StringUtils.isBlank(jsSrc))
			throw new RuntimeException("not find javascript url");
		String jsContent = null;
		try {
			jsContent = HttpClientUtil.getWebPage(jsSrc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		authorization = PatternUtil.getFirstPatternGroup(jsContent, "CLIENT_ALIAS=\"(.*?)\"");
		if (StringUtils.isBlank(authorization))
			throw new RuntimeException("not get authorization");
		return authorization;
	}

	private String getJsSrc(String content) {
		String jsSrc;
		jsSrc = PatternUtil.getFirstPatternGroup(content,
				"src=\"(https://static.zhihu.com/heifetz/main.app.*?\\.js)\"");
		if (StringUtils.isBlank(jsSrc))
			jsSrc = PatternUtil.getFirstPatternGroup(content,
					"src=\"(https://static.zhihu.com/heifetz/account.app.*?\\.js)\"");
		return jsSrc;
	}
}
