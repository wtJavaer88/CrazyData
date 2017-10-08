package job51.entity.condition;

import job51.entity.type.RangeType;

public class RangeCondition {

	private Integer begin;
	private Integer end;
	private RangeType type;

	public RangeCondition(Integer begin, Integer end, RangeType type) {
		super();
		this.begin = begin;
		this.end = end;
		this.type = type;
	}

	public String toString() {
		if (begin == null && end == null) {
			return null;
		}
		String beginStr = begin == null ? "" : "" + begin;
		String endStr = end == null ? "" : "" + end;
		if (type == RangeType.EQU) {
			return endStr.length() > beginStr.length() ? endStr : beginStr;
		}
		return beginStr + "~" + endStr;
	}

	public Integer getBegin() {
		return begin;
	}

	public void setBegin(Integer begin) {
		this.begin = begin;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public RangeType getType() {
		return type;
	}

	public void setType(RangeType type) {
		this.type = type;
	}
}
