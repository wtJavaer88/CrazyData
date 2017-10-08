package netease.music.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.crawl.core.util.HttpClientUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

import sun.misc.BASE64Encoder;

public class Util {
	public static String EmojiFilter(String str) {
		if ("".equals(str)) {
			return str;
		}
		String pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
		String reStr = "";
		Pattern emoji = Pattern.compile(pattern);
		Matcher emojiMatcher = emoji.matcher(str);
		str = emojiMatcher.replaceAll(reStr);
		return str;
	}

	public static Map<String, String> getJdbcProperty() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Properties prop = new Properties();
			FileInputStream fis = new FileInputStream("jdbc.properties");
			prop.load(fis);
			map.put("url", prop.getProperty("url"));
			map.put("username", prop.getProperty("username"));
			map.put("password", prop.getProperty("password"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String AES_encrypt(String text, byte[] key, byte[] iv) {
		String encrypt_text = "";
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
			byte[] cipherData = cipher.doFinal(text.getBytes("UTF-8"));
			encrypt_text = new BASE64Encoder().encode(cipherData);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return encrypt_text;
	}

	public static String getEncryParams(String param) {
		byte[] forth_param = { '0', 'C', 'o', 'J', 'U', 'm', '6', 'Q', 'y', 'w', '8', 'W', '8', 'j', 'u', 'd' };
		byte[] iv = { '0', '1', '0', '2', '0', '3', '0', '4', '0', '5', '0', '6', '0', '7', '0', '8' };
		byte[] secondKey = { 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F' };
		String h_encText;
		h_encText = Util.AES_encrypt(param, forth_param, iv);
		h_encText = Util.AES_encrypt(h_encText, secondKey, iv);
		return h_encText;
	}

	public static String getJson(String params, String url) throws IOException {
		String encSecKey = "257348aecb5e556c066de214e531faadd1c55d814f9be95fd06d6bff9f4c7a41f831f6394d5a3fd2e3881736d94a02ca919d952872e7d0a50ebfa1769a7a62d512f5f1ca21aec60bc3819a9c3ffca5eca9a0dba6d6f7249b06f5965ecfff3695b54e1c28f3f624750ed39e7de08fc8493242e26dbc4484a01c76f739e135637c";
		Map<String, String> data = new HashMap<String, String>();
		data.put("params", getEncryParams(params));
		data.put("encSecKey", encSecKey);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "*/*");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("Connection", "keep-alive");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Host", "music.163.com");
		headers.put("Origin", "http://music.163.com");
		headers.put("Referer", "http://music.163.com/");
		headers.put("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		String postRequest = HttpClientUtil.postRequest(url, headers, data);
		// return
		// Requests.post(url).headers(headers).forms(data).send().readToText();
		return postRequest;
	}

	static Set<Integer> ids = new HashSet<Integer>();
	static{
		List<String> readFrom = new ArrayList<String>();
		if(BasicFileUtil.isExistFile("seeked-log.txt")){
			readFrom.addAll(FileOp.readFrom("seeked-log.txt"));
		}
		if(BasicFileUtil.isExistFile("err-log.txt")){
			readFrom.addAll(FileOp.readFrom("err-log.txt"));//失败记录代表无歌曲的歌手
		}
		for (String string : readFrom) {
			ids.add(BasicNumberUtil.getNumber(PatternUtil.getFirstPattern(string, "\\d+")));
		}
	}
	public static boolean notSeeked(int id) {
		return !ids.contains(id);
	}
}
