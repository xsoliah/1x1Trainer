package com.example.manuel.a1x1trainer.AppServices;

import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;
import com.example.manuel.a1x1trainer.Ressources.UserCredentials;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class GetSessionService extends AppService {
    private final String METHOD_NAME = "getSession";

    private final String PARAMETER_PLATFORM_ID = "idPlatform";
    private final String PARAMETER_USERID = "idUser";
    private final String PARAMETER_NETWORK = "network";

    public GetSessionService() {
        SOAP_ACTION = RuntimeConstants.RUNNING_LIVE ?
                "https://schule.learninglab.tugraz.at/einmaleins/api/sessionService#getSession" :
                "https://schule-dev.tugraz.at/einmaleins/api/sessionService#getSession";
        URL = RuntimeConstants.RUNNING_LIVE ?
                "https://schule.learninglab.tugraz.at/einmaleins/api/sessionService" :
                "https://schule-dev.tugraz.at/einmaleins/api/sessionService";
    }

    @Override
    public SoapObject createSoapObject() {
        SoapObject soapObject = new SoapObject(URL, METHOD_NAME);

        SoapObject session = new SoapObject("", "session");

        PropertyInfo propertyInfo1 = new PropertyInfo();
        propertyInfo1.setName(PARAMETER_PLATFORM_ID);
        propertyInfo1.setType(RuntimeConstants.APPLICATION_ID.getClass());
        propertyInfo1.setValue(1);

        PropertyInfo propertyInfo2 = new PropertyInfo();
        propertyInfo2.setName(PARAMETER_USERID);
        propertyInfo2.setType(UserCredentials.UserId.getClass());
        propertyInfo2.setValue(UserCredentials.UserId);

        PropertyInfo propertyInfo3 = new PropertyInfo();
        propertyInfo3.setName(PARAMETER_NETWORK);
        propertyInfo3.setType(RuntimeConstants.NETWORK.getClass());
        propertyInfo3.setValue(RuntimeConstants.NETWORK);

        session.addProperty(propertyInfo1);
        session.addProperty(propertyInfo2);
        session.addProperty(propertyInfo3);

        soapObject.addSoapObject(session);

        return soapObject;
    }
}
