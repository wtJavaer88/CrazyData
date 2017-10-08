package netease.news.entity;

/**
 * 目标评论页
 * http://comment.news.163.com/{boardId}/{docId}.html
 * @author wnc
 *
 */
public class TopBoard {
	private String boardId;//板块id
	private String docId; //新闻id
	private int rCount;//未知， 转发数量？
	private int score;//参与人数
	private int tCount;//总跟帖数
	private int threadId;
	private String title;//新闻标题
	private  String url;//新闻url
	private int year;//新闻年份
	
	public String getBoardId() {
		return boardId;
	}
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	public String getDocId() {
		return docId;
	}
	@Override
	public String toString() {
		return "TopBoard [boardId=" + boardId + ", docId=" + docId + ", rCount=" + rCount + ", score=" + score
				+ ", tCount=" + tCount + ", threadId=" + threadId + ", title=" + title + ", url=" + url + ", year="
				+ year + "]";
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public int getrCount() {
		return rCount;
	}
	public void setrCount(int rCount) {
		this.rCount = rCount;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int gettCount() {
		return tCount;
	}
	public void settCount(int tCount) {
		this.tCount = tCount;
	}
	public int getThreadId() {
		return threadId;
	}
	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
}
