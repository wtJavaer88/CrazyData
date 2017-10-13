package zhihu.parser;

import java.util.List;

import com.crawl.spider.entity.Page;

public interface ListPageParser<T> {
	List<T> parseListPage(Page page);
}
