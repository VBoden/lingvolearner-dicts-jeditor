package ua.vboden.dto;

import java.util.Objects;

public class IdString implements Comparable<IdString> {
	private Integer id;
	private String value;

	public IdString(Integer id, String value) {
		super();
		this.id = id;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	public int compareTo(IdString another) {
		return value.compareTo(another.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdString other = (IdString) obj;
		return Objects.equals(id, other.id);
	}

}
