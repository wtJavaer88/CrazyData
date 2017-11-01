package dianping.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

import dianping.entity.ShopDish;
import dianping.entity.ShopTag;

public class ShopDetailPageParser
{
    Document parse;

    public ShopDetailPageParser( Document parse )
    {
        this.parse = parse;
    }

    public static void main( String[] args ) throws IOException
    {
        Document parse = Jsoup.parse( new File(
                "D:/project/CrazyData/demo/大众点评/shop.html" ), "UTF-8" );
        parse.setBaseUri( "http://www.dianping.com" );
        ShopDetailPageParser shopDetailPageParser = new ShopDetailPageParser(
                parse );
        shopDetailPageParser.parsePicCmts();
        shopDetailPageParser.parseShopDish();
        shopDetailPageParser.parseShopTag();
    }

    public int parsePicCmts()
    {
        System.out.println( "带图片评论数:"
                + crt2num( PatternUtil.getFirstPattern(
                        parse.select( "label.filter-item.J-filter-pic > span" )
                                .text(), "\\d+" ) ) );
        return crt2num( PatternUtil.getFirstPattern(
                parse.select( "label.filter-item.J-filter-pic > span" ).text(),
                "\\d+" ) );
    }

    public List<ShopTag> parseShopTag()
    {
        List<ShopTag> list = new ArrayList<ShopTag>();
        Elements select = parse
                .select( "#summaryfilter-wrapper > div.comment-condition.J-comment-condition.Fix > div.content span" );
        System.out.println( "Tag Count:" + select.size() );
        int shopId = 1123;
        for ( Element element : select )
        {
            String tagName = PatternUtil.getFirstPatternGroup( element.text(),
                    "(.*?)\\(" ).trim();
            int upvote = crt2num( PatternUtil.getLastPattern( element.text(),
                    "\\d+" ) );
            boolean isPositive = element.hasClass( "good" );
            ShopTag shopTag = new ShopTag( shopId, tagName, upvote, isPositive );
            System.out.println( shopTag );
            list.add( shopTag );
        }
        return list;
    }

    public List<ShopDish> parseShopDish()
    {
        List<ShopDish> list = new ArrayList<ShopDish>();
        Elements select = parse
                .select( "#shop-tabs > div.shop-tab-recommend.J-panel > p a" );
        System.out.println( "Dish Count:" + select.size() );
        for ( Element element : select )
        {
            String dishName = element.ownText();
            int upvote = crt2num( PatternUtil.getLastPattern( element.text(),
                    "\\d+" ) );
            String url = element.attr( "href" );
            int shopId = crt2num( PatternUtil.getFirstPattern( url, "\\d+" ) );
            int dishId = crt2num( PatternUtil.getLastPattern( url, "\\d+" ) );
            ShopDish shopDish = new ShopDish( shopId, dishId, upvote, dishName,
                    url );
            System.out.println( shopDish );
            list.add( shopDish );
        }
        return list;
    }

    private int crt2num( String str )
    {
        return BasicNumberUtil.getNumber( str );
    }
}

