package javlib.service;

import java.util.List;

import javlib.entity.JStar;

import org.jsoup.nodes.Document;

public interface IStarIndexService
{
    List<JStar> getPageStars( Document doc );

    /**
     * 获取某索引下的明星页数
     * 
     * @param index
     * @return
     */
    int getPageCount( Document doc );
}
