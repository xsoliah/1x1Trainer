package com.example.manuel.a1x1trainer.AppServices;

import org.ksoap2.serialization.SoapObject;

/**
 * App Service
 *
 * Parent class for all Application-Services. (Web-Services)
 */
public abstract class AppService {
    String URL;
    String SOAP_ACTION;

    /**
     * Getter for soap action
     * @return soap action
     */
    public String getSoapAction() {
        return SOAP_ACTION;
    }

    /**
     * Getter for the URL
     * @return the URL of the WebService
     */
    public String getURL() {
        return URL;
    }

    /**
     * Abstract method for creating the SOAP envelope
     * @return created SOAP envelope
     */
    public abstract SoapObject createSoapObject();
}
