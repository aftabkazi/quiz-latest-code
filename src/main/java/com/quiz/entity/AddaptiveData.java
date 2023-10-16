package com.quiz.entity;

public class AddaptiveData {
	
	private Boolean isTrue=false;
	private Integer easy;
	private Integer medium;
	private Integer hard;
	
	public AddaptiveData() {
		super();
	}

	public AddaptiveData(boolean isTrue, Integer easy, Integer medium, Integer hard) {
		super();
		this.isTrue = isTrue;
		this.easy = easy;
		this.medium = medium;
		this.hard = hard;
	}

	public Integer getEasy() {
		return easy;
	}

	public void setEasy(Integer easy) {
		this.easy = easy;
	}

	public Integer getMedium() {
		return medium;
	}

	public void setMedium(Integer medium) {
		this.medium = medium;
	}

	public Integer getHard() {
		return hard;
	}

	public void setHard(Integer hard) {
		this.hard = hard;
	}

	public Boolean getIsTrue() {
		return isTrue;
	}

	public void setIsTrue(Boolean isTrue) {
		this.isTrue = isTrue;
	}
}
