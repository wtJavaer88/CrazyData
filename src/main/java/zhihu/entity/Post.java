package zhihu.entity;

public class Post {
	private int id;
	private String title;
	private String content;
	private String url;
	private String author;
	private String excerpt;
	private int updated;
	private int created;
	private int voteup_count;
	private int comment_count;
	private String image_url;
	private String type;

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", content=" + content + ", url=" + url + ", author=" + author
				+ ", excerpt=" + excerpt + ", updated=" + updated + ", created=" + created + ", voteup_count="
				+ voteup_count + ", comment_count=" + comment_count + ", image_url=" + image_url + ", type=" + type
				+ "]";
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getVoteup_count() {
		return voteup_count;
	}

	public void setVoteup_count(int voteup_count) {
		this.voteup_count = voteup_count;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

}
