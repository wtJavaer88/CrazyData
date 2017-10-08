package netease.music.entity;

public class Song {
	private long id;
	private String songId;

	public String getSongId() {
		return songId;
	}

	public void setSongId(String songId) {
		this.songId = songId;
	}

	private String name;
	private int no;
	private long mvid;
	private double score;

	private Album album;
	private int duration;
	private int comment_count;

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public long getMvid() {
		return mvid;
	}

	public void setMvid(long mvid) {
		this.mvid = mvid;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDrt_txt() {
		String ret = "";
		int seconds = this.getDuration() / 1000;
		int h = seconds / 3600;
		int m = seconds / 60;
		int s = seconds % 60;
		if (h > 0)
			ret += format(h) + ":";
		ret += format(m) + ":";
		ret += format(s);
		return ret;
	}

	private String format(int n) {
		return n > 10 ? "" + n : "0" + n;
	}
}
