package job51.entity;

import job51.entity.condition.RangeCondition;

/**
 * 工作所需基本条件, 可以部分为空
 * 
 * @author wWX452950
 * 
 */
public class JobBasicCondition {
	private String jobId;

	private String year;
	private String need;
	private String education;// 教育水平
	private String oral;
	private String specialLine;// 专业
	private String unKownTag6;

	public String getOral() {
		return oral;
	}

	public void setOral(String oral) {
		this.oral = oral;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	private RangeCondition yearRange;// 工作经验
	private RangeCondition needCountRange;// 招聘人数
	/**
	 * 外语技巧, 有多个要求的情况下只取第一个
	 */
	private OralSkill oralSkill;// 外语技巧

	private int educationLevel;// 教育水平

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getUnKownTag6() {
		return unKownTag6;
	}

	public void setUnKownTag6(String unKownTag6) {
		this.unKownTag6 = unKownTag6;
	}

	public RangeCondition getYearRange() {
		return yearRange;
	}

	public void setYearRange(RangeCondition yearRange) {
		this.yearRange = yearRange;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getNeed() {
		return need;
	}

	public void setNeed(String need) {
		this.need = need;
	}

	public RangeCondition getNeedCountRange() {
		return needCountRange;
	}

	public void setNeedCountRange(RangeCondition needCountRange) {
		this.needCountRange = needCountRange;
	}

	public OralSkill getOralSkill() {
		return oralSkill;
	}

	public void setOralSkill(OralSkill oralSkill) {
		this.oralSkill = oralSkill;
	}

	public String getSpecialLine() {
		return specialLine;
	}

	public void setSpecialLine(String specialLine) {
		this.specialLine = specialLine;
	}

	public int getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(int educationLevel) {
		this.educationLevel = educationLevel;
	}
}
