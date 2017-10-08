package job51.entity;

import java.util.List;

import job51.entity.condition.RangeCondition;

/**
 * 工作信息
 * 
 * @author wWX452950
 * 
 */
public class Job {
	private Integer dbId;// 数据库id
	private Integer jobId;// 页面显示jobid
	private String jobName;
	private String url;
	private String workCity;// 工作城市
	private String workZone;// 工作区域
	private String workLocation;// 具体地点

	public String getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	private String publishDate;// 发布日期
	JobBasicCondition basicCondition;

	private RangeCondition salaryRange;

	private List<String> fuli;// 福利
	private JobDescription description;// 详细描述,包括岗位描述和技能要求

	private String createDate;
	private String updateDate;

	public Job(Integer jobId) {
		this.jobId = jobId;
	}

	public Integer getDbId() {
		return dbId;
	}

	public void setDbId(Integer dbId) {
		this.dbId = dbId;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	private Company company;

	public RangeCondition getSalaryRange() {
		return salaryRange;
	}

	public void setSalaryRange(RangeCondition salaryRange) {
		this.salaryRange = salaryRange;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getWorkCity() {
		return workCity;
	}

	public void setWorkCity(String workCity) {
		this.workCity = workCity;
	}

	public String getWorkZone() {
		return workZone;
	}

	public void setWorkZone(String workZone) {
		this.workZone = workZone;
	}

	public JobBasicCondition getBasicCondition() {
		return basicCondition;
	}

	public void setBasicCondition(JobBasicCondition basicCondition) {
		this.basicCondition = basicCondition;
	}

	public JobDescription getDescription() {
		return description;
	}

	public void setDescription(JobDescription description) {
		this.description = description;
	}

	public List<String> getFuli() {
		return fuli;
	}

	public void setFuli(List<String> fuli) {
		this.fuli = fuli;
	}

}
