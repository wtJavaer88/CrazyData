package netease.music.entity;

public class Comment implements spiderqueue.core.DbEntity {
	private int id;
	private long songId;
	private String userId;
	private String content;
	private String upvote;
	private String time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Comments{" + "id=" + id + ", userId=" + userId + ", content='" + content + '\'' + ", likedCount="
				+ upvote + ", time=" + time + '}';
	}

	public String getUpvote() {
		return upvote;
	}

	public void setUpvote(String upvote) {
		this.upvote = upvote;
	}

	public String getLogKey() {
		return this.id + "";

	}
}
