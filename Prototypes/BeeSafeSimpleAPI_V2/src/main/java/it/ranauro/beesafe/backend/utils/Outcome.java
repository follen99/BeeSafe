package it.ranauro.beesafe.backend.utils;

/**
 * usato per indicare l'esito dell'operazione
 */
public enum Outcome {
    SUCCESS,
    NOT_AUTHORIZATED,
    NOT_AUTHENTICATED,
    AUTHENTICATED_NOT_AUTHORIZED,
    GENERIC_ERROR,
    BAD_REQUEST,
    INTERNAL_SERVER_ERROR
}
