package mafengwo.action;

import java.io.IOException;

import com.crawl.spider.MySpiderAction;

import mafengwo.TestLogin;

public class MfwUserAction implements MySpiderAction {

	int startUserId = 79568445;// 思子

	public void execute() {
		try {
			TestLogin.login();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// SpiderHttpClient.getInstance().getNetPageThreadPool().execute(new
		// MfwUserTask(79568445));
	}

}
