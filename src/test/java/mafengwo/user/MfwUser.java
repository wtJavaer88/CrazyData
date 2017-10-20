package mafengwo.user;

public class MfwUser {
	// 以下6个在关注者列表即可获取
	private Integer id;
	private Integer notes;
	private Integer fans;
	private Integer paths;

	private String name;
	private String url;

	// 以下4项需要到个人主页获取
	private Integer follows;
	private Integer reviews;
	private Integer answers;
	private Integer level;// 等级
	private String nowPosition;// 现居
	private String description;// 简介
	private int sex;// 1:男 0:女
	private String profile;// 头像
	private boolean vip;// 是否 vip
	private boolean duo;// 是否分舵
	private boolean zhiluren;// 是否指路人

	// 以下2项到个人游记页获取
	private Integer noteViewTotal;// 游记浏览总数
	private Integer noteCommentTotal;// 游记评论总数

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getNowPosition() {
		return nowPosition;
	}

	public void setNowPosition(String nowPosition) {
		this.nowPosition = nowPosition;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNotes() {
		return notes;
	}

	public void setNotes(Integer notes) {
		this.notes = notes;
	}

	public Integer getFans() {
		return fans;
	}

	public void setFans(Integer fans) {
		this.fans = fans;
	}

	public Integer getFollows() {
		return follows;
	}

	public void setFollows(Integer follows) {
		this.follows = follows;
	}

	public Integer getPaths() {
		return paths;
	}

	public void setPaths(Integer paths) {
		this.paths = paths;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getReviews() {
		return reviews;
	}

	public void setReviews(Integer reviews) {
		this.reviews = reviews;
	}

	public Integer getAnswers() {
		return answers;
	}

	public void setAnswers(Integer answers) {
		this.answers = answers;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNoteViewTotal() {
		return noteViewTotal;
	}

	public void setNoteViewTotal(Integer noteViewTotal) {
		this.noteViewTotal = noteViewTotal;
	}

	public Integer getNoteCommentTotal() {
		return noteCommentTotal;
	}

	public void setNoteCommentTotal(Integer noteCommentTotal) {
		this.noteCommentTotal = noteCommentTotal;
	}

	@Override
	public String toString() {
		return "MfwUser [id=" + id + ", name=" + name + ", notes=" + notes + ", fans=" + fans + ", paths=" + paths
				+ "]";
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public boolean isDuo() {
		return duo;
	}

	public void setDuo(boolean duo) {
		this.duo = duo;
	}

	public boolean isZhiluren() {
		return zhiluren;
	}

	public void setZhiluren(boolean zhiluren) {
		this.zhiluren = zhiluren;
	}

}
