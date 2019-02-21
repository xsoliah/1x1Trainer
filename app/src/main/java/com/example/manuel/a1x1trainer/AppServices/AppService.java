package com.example.manuel.a1x1trainer.AppServices;

import org.ksoap2.serialization.SoapObject;

public abstract class AppService {
    protected String URL;
    protected String SOAP_ACTION;
    public String getSoapAction() {
        return SOAP_ACTION;
    };
    public String getURL() {
        return URL;
    }
    public abstract SoapObject createSoapObject();
}
