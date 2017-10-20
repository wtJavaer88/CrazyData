package mafengwo.entity;

public class Note {
	private Integer id;
	private Integer uid;
	private Integer upvotes;
	private Integer views;
	private Integer comments;
	private Integer stars;
	private String thunmnail;// 标题图片
	private String thumbDescription;

	private String noteTime;
	private String createTime;

	private String exerpt;
	private String url;
	private String title;
	private String type;

	private Integer words;
	private Integer pics;
	private Integer helps;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUpvotes() {
		return upvotes;
	}

	public void setUpvotes(Integer upvotes) {
		this.upvotes = upvotes;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public String getExerpt() {
		return exerpt;
	}

	public void setExerpt(String exerpt) {
		this.exerpt = exerpt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getWords() {
		return words;
	}

	public void setWords(Integer words) {
		this.words = words;
	}

	public Integer getPics() {
		return pics;
	}

	public void setPics(Integer pics) {
		this.pics = pics;
	}

	public Integer getHelps() {
		return helps;
	}

	public void setHelps(Integer helps) {
		this.helps = helps;
	}

	public String getNoteTime() {
		return noteTime;
	}

	public void setNoteTime(String noteTime) {
		this.noteTime = noteTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThunmnail() {
		return thunmnail;
	}

	public void setThunmnail(String thunmnail) {
		this.thunmnail = thunmnail;
	}

	public String getThumbDescription() {
		return thumbDescription;
	}

	public void setThumbDescription(String thumb_description) {
		this.thumbDescription = thumb_description;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

}