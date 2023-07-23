package ua.vboden.dto;

public class CodeString implements Comparable<CodeString> {
	private String code;
	private String value;

	public CodeString(String code, String value) {
		super();
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int compareTo(CodeString another) {
		return value.compareTo(another.value);
	}

}
