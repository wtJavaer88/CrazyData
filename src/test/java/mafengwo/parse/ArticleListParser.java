package mafengwo.parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

import mafengwo.entity.Note;

public class ArticleListParser {
	public int getMaxPage(Document doc) {
		String pages = "1";
		if (doc.text().contains("末页")) {
			pages = doc.select("a").last().attr("data-p");
		} else if (doc.select("a").size() > 1) {
			pages = doc.select("a").last().previousElementSibling().attr("data-p");
		}
		return cvt2Number(pages);
	}

	public List<Note> parseList(Document doc) {
		List<Note> notes = new ArrayList<Note>();
		Note note = null;
		Elements select = doc.select(".notes_list > ul  li dl");
		for (Element element : select) {
			note = new Note();
			parseDt(note, element.select("dt").first());
			parseDd(note, element.select("dd").first());
			notes.add(note);
		}
		return notes;
	}

	private void parseDt(Note note, Element element) {
		note.setThunmnail(element.select("img").attr("src"));
		note.setThumbDescription(element.select(".thumb_description").text());
	}

	private void parseDd(Note note, Element element) {
		String upvote = element.select(".MDing span").text();
		note.setUpvotes(cvt2Number(upvote));

		int id = cvt2Number(element.select(".MDing a").attr("data-iid"));
		note.setId(id);

		Elements titleEmts = element.select("div.note_info > h3 > a");
		if (titleEmts.size() == 2) {
			// 游记类别
			note.setType(titleEmts.get(0).attr("title"));

			note.setUrl(titleEmts.get(1).attr("href"));
			note.setTitle(titleEmts.get(1).text());
		} else if (titleEmts.size() == 1) {
			note.setUrl(titleEmts.get(0).attr("href"));
			note.setTitle(titleEmts.get(0).text());
		}

		String view = element.select("div.note_info > div.note_more > span:nth-child(1) > em").text();
		note.setViews(cvt2Number(PatternUtil.getFirstPattern(view, "\\d+")));

		note.setComments(cvt2Number(PatternUtil.getLastPattern(view, "\\d+")));

		String star = element.select("div.note_info > div.note_more > span:nth-child(2) > em").text();
		note.setStars(cvt2Number(star));

		String time = element.select("div.note_info > div.note_more > span.time").text();
		note.setNoteTime(time);

		note.setExerpt(element.select(".note_word").text());

		note.setCreateTime(BasicDateUtil.getCurrentDateTimeString());
	}

	public int cvt2Number(String str) {
		return BasicNumberUtil.getNumber(str);
	}

	public static void main(String[] args) throws IOException {
		Document parse = Jsoup.parse(new File("F:/资源/爬虫/马蜂窝/note/note.html"), "GBK");

		ArticleListParser articleListParser = new ArticleListParser();
		System.out.println(articleListParser.getMaxPage(parse));
		List<Note> parseList = articleListParser.parseList(parse);
		for (Note note : parseList) {
			System.out.println(
					JSONObject.toJSONString(note, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue));
		}
	}

}
