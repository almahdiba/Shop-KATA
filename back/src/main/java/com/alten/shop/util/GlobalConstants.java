package com.alten.shop.util;


public final class GlobalConstants {

    private GlobalConstants() throws InstantiationException {
        throw new InstantiationException("Instances of this type are forbidden");
    }

    /**
     * API ERROR CODES
     */

     public static final String URI_TECHNICAL_EXCEPTION = "/problem/technical-exception";
     public static final String URI_FUNCTIONAL_EXCEPTION = "/problem/functional-exception";
     public static final String URI_RESOURCE_NOT_FOUND_EXCEPTION = "/problem/resource-not-found-exception";
     public static final String URI_METHOD_ARGUMENT_NOT_VALID =   "/method-argument-not-valid";

     private static final String BASE_URI = "/problem";
     public static final String URI_DEFAULT = BASE_URI + "/error-with-detail";
     public static final String URI_MISSING_REQUEST_PARAMETER = BASE_URI + "/missing-request-params";
     public static final String URI_INTER_SERVICE_COMMUNICATION = BASE_URI + "/inter-service-communication";
     public static final String URI_VALIDATION_CONSTRAINT_VIOLATION = BASE_URI + "/validation-constraint-violation";
     public static final String URI_VALIDATION_CONSTRAINT = BASE_URI + "/validation-constraint";
 
     public static final String URI_UNEXPECTED_TECHNICAL_ERROR = BASE_URI + "/unexpected-technical-exception";
 
     public static final String URI_CALL_STORED_PROCEDURE 			= BASE_URI + "/db/call-stored-procedure";
     public static final String URI_MESSAGE_NOT_READABLE =   "/message-not-readable";
     public static final String URI_ACCESS_DENIED = "/errors/access-denied";

    /**
     * codes messages
     */
    public static final String ERROR_WS_TECHNICAL = "Une erreur technique inattendue est survenue. Veuillez transmettre le jeton {0} à votre administrateur pour obtenir plus d’informations.";

     
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";


    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    
}
