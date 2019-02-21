package com.example.manuel.a1x1trainer.AppServices;

import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;
import com.example.manuel.a1x1trainer.Security.SecurityAlgorithm;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class IsUserAllowedService extends AppService {
    private final String METHOD_NAME = "isUserAllowed";

    private final String PARAMETER_USERNAME = "username";
    private final String PARAMETER_PW = "password";
    private final String PARAMETER_KEY = "idApp";
    private final String PARAMETER_HMAC = "hmacClient";

    private String Username;
    private String Password;

    public IsUserAllowedService(String username, String password) {
        SOAP_ACTION = RuntimeConstants.RUNNING_LIVE ?
                "https://schule.learninglab.tugraz.at/usermanager/soap#isUserAllowed" :
                "https://schule-dev.tugraz.at/usermanager/soap#isUserAllowed";
        URL = RuntimeConstants.RUNNING_LIVE ?
                "https://schule.learninglab.tugraz.at/usermanager/soap" :
                "https://schule-dev.tugraz.at/usermanager/soap";
        Username = username;
        Password = password;
    }

    public String getSoapAction() {
        return this.SOAP_ACTION;
    }

    public SoapObject createSoapObject() {
        SoapObject soapObject = new SoapObject(RuntimeConstants.USERMANAGER_URL, METHOD_NAME);

        PropertyInfo propertyInfo1 = new PropertyInfo();
        propertyInfo1.setName(PARAMETER_USERNAME);
        propertyInfo1.setType(Username.getClass());
        propertyInfo1.setValue(Username);

        PropertyInfo propertyInfo2 = new PropertyInfo();
        propertyInfo2.setName(PARAMETER_PW);
        String passwordHash = SecurityAlgorithm.sha256(Password);
        propertyInfo2.setType(passwordHash.getClass());
        propertyInfo2.setValue(passwordHash);

        PropertyInfo propertyInfo3 = new PropertyInfo();
        propertyInfo3.setName(PARAMETER_KEY);
        propertyInfo3.setType(RuntimeConstants.APPLICATION_ID.getClass());
        propertyInfo3.setValue(RuntimeConstants.APPLICATION_ID);

        PropertyInfo propertyInfo4 = new PropertyInfo();
        propertyInfo4.setName(PARAMETER_HMAC);
        String toHmac = Username.concat(passwordHash).concat(RuntimeConstants.APPLICATION_ID.toString());
        String hmac = SecurityAlgorithm.hmac_sha256(RuntimeConstants.APPLICATION_KEY_SECRET, toHmac);
        propertyInfo4.setType(hmac.getClass());
        propertyInfo4.setValue(hmac);

        soapObject.addProperty(propertyInfo1);
        soapObject.addProperty(propertyInfo2);
        soapObject.addProperty(propertyInfo3);
        soapObject.addProperty(propertyInfo4);

        return soapObject;
    }
}
