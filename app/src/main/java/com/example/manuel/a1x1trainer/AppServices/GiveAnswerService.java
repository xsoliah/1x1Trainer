package com.example.manuel.a1x1trainer.AppServices;

import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class GiveAnswerService extends AppService {
    private final String METHOD_NAME = "giveAnswer";

    private final String PARAMETER_ANSWER = "answer";
    private final String PARAMETER_QUESTION_ID = "questionId";
    private final String PARAMETER_REACTION_TIME = "reactionTime";
    private final String PARAMETER_SESSION_ID = "sessionId";

    private String answer;
    private Integer questionId;
    private Integer reactionTime;
    private Integer sessionId;

    public GiveAnswerService(String answer_, int question_id, int reaction_time, int session_id) {
        SOAP_ACTION = RuntimeConstants.RUNNING_LIVE ?
                "https://schule.learninglab.tugraz.at/einmaleins/api/answerService#giveAnswer" :
                "https://schule-dev.tugraz.at/einmaleins/api/answerService#giveAnswer";
        URL = RuntimeConstants.RUNNING_LIVE ?
                "https://schule.learninglab.tugraz.at/einmaleins/api/answerService" :
                "https://schule-dev.tugraz.at/einmaleins/api/answerService";
        answer = answer_;
        questionId = question_id;
        reactionTime = reaction_time;
        sessionId = session_id;
    }

    @Override
    public SoapObject createSoapObject() {
        SoapObject soapObject = new SoapObject(URL, METHOD_NAME);

        SoapObject session = new SoapObject("", "session");

        PropertyInfo propertyInfo1 = new PropertyInfo();
        propertyInfo1.setName(PARAMETER_ANSWER);
        propertyInfo1.setType(PARAMETER_ANSWER.getClass());
        propertyInfo1.setValue(answer);

        PropertyInfo propertyInfo2 = new PropertyInfo();
        propertyInfo2.setName(PARAMETER_QUESTION_ID);
        propertyInfo2.setType(PARAMETER_QUESTION_ID.getClass());
        propertyInfo2.setValue(questionId);

        PropertyInfo propertyInfo3 = new PropertyInfo();
        propertyInfo3.setName(PARAMETER_REACTION_TIME);
        propertyInfo3.setType(PARAMETER_REACTION_TIME.getClass());
        propertyInfo3.setValue(reactionTime);

        PropertyInfo propertyInfo4 = new PropertyInfo();
        propertyInfo4.setName(PARAMETER_SESSION_ID);
        propertyInfo4.setType(PARAMETER_SESSION_ID.getClass());
        propertyInfo4.setValue(sessionId);

        session.addProperty(propertyInfo1);
        session.addProperty(propertyInfo2);
        session.addProperty(propertyInfo3);
        session.addProperty(propertyInfo4);

        soapObject.addSoapObject(session);

        return soapObject;
    }
}
