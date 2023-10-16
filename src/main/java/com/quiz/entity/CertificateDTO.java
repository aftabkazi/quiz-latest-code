package com.quiz.entity;

public class CertificateDTO {

    private String name;
    private String quizname;
    private String sessiondate;
    private String certificateid;
    private String issuedate;
    private String prize;
    private Integer percentage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuizname() {
        return quizname;
    }

    public void setQuizname(String quizname) {
        this.quizname = quizname;
    }

    public String getSessiondate() {
        return sessiondate;
    }

    public void setSessiondate(String sessiondate) {
        this.sessiondate = sessiondate;
    }

    public String getCertificateid() {
        return certificateid;
    }

    public void setCertificateid(String certificateid) {
        this.certificateid = certificateid;
    }

    public String getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(String issuedate) {
        this.issuedate = issuedate;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return "CertificateDTO [name=" + name + ", quizname=" + quizname + ", sessiondate=" + sessiondate
				+ ", certificateid=" + certificateid + ", issuedate=" + issuedate + ", prize=" + prize + ", percentage="
				+ percentage + "]";
	}
}
