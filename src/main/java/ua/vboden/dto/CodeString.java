package ua.vboden.dto;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodeString other = (CodeString) obj;
		return Objects.equals(code, other.code);
	}

}
