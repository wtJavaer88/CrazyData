package job51.entity;

public class OralSkill {
	private String oralType;
	private String desciption;

	public OralSkill(String oralType, String desciption) {
		super();
		this.oralType = oralType;
		this.desciption = desciption;
	}

	public String getOralType() {
		return oralType;
	}

	public void setOralType(String oralType) {
		this.oralType = oralType;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

}
