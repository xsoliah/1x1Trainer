package com.example.manuel.a1x1trainer.AppServices;

import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;
import com.example.manuel.a1x1trainer.Ressources.UserCredentials;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class GetQuestionService extends AppService {
    private final String METHOD_NAME = "getQuestion";
    private final String PARAMETER_USERID = "userId";

    public GetQuestionService() {
        SOAP_ACTION = RuntimeConstants.RUNNING_LIVE ?
                "https://schule.learninglab.tugraz.at/einmaleins/api/questionService#getQuestion" :
                "https://schule-dev.tugraz.at/einmaleins/api/questionService#getQuestion";
        URL = RuntimeConstants.RUNNING_LIVE ?
                "https://schule.learninglab.tugraz.at/einmaleins/api/questionService" :
                "https://schule-dev.tugraz.at/einmaleins/api/questionService";
    }

    public SoapObject createSoapObject() {
        SoapObject soapObject = new SoapObject(URL, METHOD_NAME);


        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setName(PARAMETER_USERID);
        propertyInfo.setType(UserCredentials.UserId.getClass());
        propertyInfo.setValue(UserCredentials.UserId);

        soapObject.addProperty(propertyInfo);

        return soapObject;
    }
}
