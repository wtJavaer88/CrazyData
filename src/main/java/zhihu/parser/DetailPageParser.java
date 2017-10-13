package zhihu.parser;

import com.crawl.spider.entity.Page;

import zhihu.entity.User;

public interface DetailPageParser {
	User parseDetailPage(Page page);
}
