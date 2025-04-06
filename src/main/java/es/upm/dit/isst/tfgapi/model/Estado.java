package es.upm.dit.isst.tfgapi.model;

public enum Estado {

    SOLICITADO, 
    ACEPTADOPORTUTOR, 
    APROBADOPORCOA,
    RECHAZADO, 
    SOLICITADADEFENSA,
    PROPUESTADEFENSA,
    PROGRAMADADEFENSA,
    CALIFICADO;

    public boolean canTransitionTo(Estado destino) {
        switch (this) {
            case SOLICITADO:
                return destino == ACEPTADOPORTUTOR || destino == RECHAZADO;
            case ACEPTADOPORTUTOR:
                return destino == APROBADOPORCOA || destino == RECHAZADO;
            case APROBADOPORCOA:
                return destino == SOLICITADADEFENSA;
            case SOLICITADADEFENSA:
                return destino == PROPUESTADEFENSA;
            case PROPUESTADEFENSA:
                return destino == PROGRAMADADEFENSA;
            case PROGRAMADADEFENSA:
                return destino == CALIFICADO;
            case RECHAZADO:
                return false;
            case CALIFICADO:    
                return false;
            default:
                return false;
        }
    }

}
