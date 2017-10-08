package job51.action;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JobPositonAction {
	public static void main(String[] args) throws IOException {
		Document parse = Jsoup.parse(new File("job_position.html"), "GBK");
		Elements select = parse.select("em");
		for (Element element : select) {
			System.out.println(element.attr("data-value") + " / " + element.text());
		}

	}
}
