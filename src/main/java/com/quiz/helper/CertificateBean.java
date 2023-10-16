package com.quiz.helper;

import java.util.ArrayList;
import com.quiz.entity.CertificateDTO;

public class CertificateBean {
	
	public ArrayList<CertificateDTO> getDataBeanList(String name, String topic, String sessiondate, String certificateid, String issuedate, String prize,Integer percentage) {
        ArrayList<CertificateDTO> dataBeanList = new ArrayList<CertificateDTO>();
        dataBeanList.add(produceData(name, topic, sessiondate, certificateid, issuedate, prize,percentage));
        
        return dataBeanList;
    }

    private CertificateDTO produceData(String name, String topic, String sessiondate, String certificateid, String issuedate, String prize,Integer percentage) {
        CertificateDTO data = new CertificateDTO();
        data.setName(name);
        data.setQuizname(topic);
        data.setPercentage(percentage);
        data.setSessiondate(sessiondate);
        data.setCertificateid(certificateid);
        data.setIssuedate(issuedate);
        data.setPrize(prize);
        return data;
    }
}
