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
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;

import mafengwo.user.MfwUser;

/**
 * 获取用户关注者列表, 建议从【思子：79568445】入手，此人有54个关注，3969粉丝
 * 
 * @author wWX452950
 * 
 */
public class MfwUserListParser {
	public List<MfwUser> parseList(Document doc) {
		List<MfwUser> MfwUsers = new ArrayList<MfwUser>();
		MfwUser MfwUser = null;
		Elements select = doc.select("body > div.wrap.clearfix > div.content > ul > li");
		System.out.println(select.size());
		for (Element element : select) {
			MfwUser = new MfwUser();
			MfwUser.setName(element.select(".name a").text());
			MfwUser.setUrl(element.select(".name a").first().attr("href"));
			MfwUser.setId(BasicNumberUtil.getNumber(PatternUtil.getLastPattern(MfwUser.getUrl(), "\\d+")));

			MfwUser.setFans(BasicNumberUtil
					.getNumber(PatternUtil.getLastPattern(element.select(".num_list .fans b").text(), "\\d+")));
			MfwUser.setNotes(BasicNumberUtil
					.getNumber(PatternUtil.getLastPattern(element.select(".num_list .travelnotes b").text(), "\\d+")));
			MfwUser.setPaths(BasicNumberUtil
					.getNumber(PatternUtil.getLastPattern(element.select(".num_list .noborder b").text(), "\\d+")));

			MfwUsers.add(MfwUser);
		}

		return MfwUsers;
	}

	public static void main(String[] args) throws IOException {
		Document parse = Jsoup.parse(new File("MfwUser_followers.html"), "UTF-8");
		List<MfwUser> parseList = new MfwUserListParser().parseList(parse);
		for (MfwUser MfwUser : parseList) {
			System.out.println(JSONObject.toJSONString(MfwUser, SerializerFeature.PrettyFormat,
					SerializerFeature.WriteMapNullValue));
		}
	}
}
