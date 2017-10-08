package job51.entity;

import java.util.List;
import java.util.Set;

/**
 * 公司信息
 * 
 * @author wWX452950
 * 
 */
public class Company {
	private Integer dbId;
	private String comId;
	private String url;
	private String comName;
	private String companyType;// 公司性质
	private String companysize;// 公司大小
	private String address;// 公司地址
	private Set<String> yewu;// 业务范围

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<String> getYewu() {
		return yewu;
	}

	public void setYewu(Set<String> yewu) {
		this.yewu = yewu;
	}

	public String getCompanysize() {
		return companysize;
	}

	public void setCompanysize(String companysize) {
		this.companysize = companysize;
	}

	private List<String> vocations;// 业务范围

	public Integer getDbId() {
		return dbId;
	}

	public void setDbId(Integer dbId) {
		this.dbId = dbId;
	}

	public String getComId() {
		return comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public List<String> getVocations() {
		return vocations;
	}

	public void setVocations(List<String> vocations) {
		this.vocations = vocations;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
