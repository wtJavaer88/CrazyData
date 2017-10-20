package mafengwo.city;

import java.util.HashSet;
import java.util.Set;

import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

public class Food {
	public static void main(String[] args) {
		Set<String> citylist = new HashSet<String>();
		String folder = "F:/资源/爬虫/马蜂窝/food";
		for (String s : FileOp.readFrom(folder + "/cityall.txt")) {
			citylist.add(s);
		}
		//
		for (String s : FileOp.readFrom(folder + "/city-cylsit-1508071165856.txt")) {
			citylist.remove(PatternUtil.getFirstPattern(s, "\\d+"));
		}
		System.out.println(citylist);
		// city-cy-err
		for (String s : FileOp.readFrom(folder + "/city-cy-err.txt")) {
			citylist.add(PatternUtil.getFirstPattern(s, "\\d+"));
		}
		System.out.println(citylist);
		for (String string : citylist) {
			BasicFileUtil.writeFileString(folder + "/city-rest.txt", string + "\r\n", null, true);
		}
	}
}
