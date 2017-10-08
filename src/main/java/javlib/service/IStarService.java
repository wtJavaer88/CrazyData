package javlib.service;

import java.util.List;

import javlib.entity.JMovie;

import org.jsoup.nodes.Document;


public interface IStarService
{
    /**
     * @param doc
     *            某一页的Movie列表,只有基本信息
     * @return
     */
    List<JMovie> getPageBasicMovies( Document doc );

    int getPageCount( Document doc );
}

