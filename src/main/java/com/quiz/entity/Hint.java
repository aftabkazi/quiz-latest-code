package com.quiz.entity;

public class Hint {

	private String hint;
	private Integer value;

	public Hint() {
		super();
	}

	public Hint(String hint, Integer value) {
		super();
		this.hint = hint;
		this.value = value;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Hint [hint=" + hint + ", value=" + value + "]";
	}
}
