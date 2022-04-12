package com.example.beesafe.utils;

public class Constants {
    /**
     * zoom predefinito usato nelle mappe
     */
    public static final float ZOOM_LEVEL = 13.5f;

    /**
     * una semplice proof of concept, il secret non dovrebbe essere tenuto in questo modo.
     * */
    public static final String SECRET = "BernardiMarkMe30L";
    public static final String REST_HOSTNAME = "ec2-3-17-73-225.us-east-2.compute.amazonaws.com";
    // solo per debug
    //public static final String REST_HOSTNAME = "192.168.1.48";
    public static final String REST_PORT = "8080";

    public static final String NO_CONNECTION_TITLE = "Non sei connesso ad internet!";
    public static final String NO_CONNECTION_MESSAGE = "Questa icona viene visualizzata quando il telefono non è connesso ad internet.\n\n" +
            "Quando non sei connesso ad internet, non puoi inviare report o visualizzare report che non sono presenti in memoria.";

    public static final String NO_CONNECTION_TO_REST_TITLE = "Non sei connesso al server!";
    public static final String NO_CONNECTION_TO_REST_MESSAGE = "Questa icona viene visualizzata quando il telefono non è connesso al server.\n\n" +
            "Quando non sei connesso al server, non puoi inviare report o visualizzare report che non sono presenti in memoria.\n\n " +
            "Questo potrebbe essere dato da un nostro problema o da una mancanza di connessione del dispositivo.";

    public static final String NO_CONNECTION_MINIGAME_MESSAGE = "Questa icona diventerà verde quando sarai connesso ad internet.";

    public static final String CONNECTED_TO_INTERNET_MINIGAME_TITLE = "Sei connesso ad internet!";
    public static final String CONNECTED_TO_INTERNET_MINIGAME_MESSAGE = "Puoi tornare alla home ed aggiungere nuovi report.";

    public static final int CONNECTION_TIMEOUT = 3000;

    /**
     * Ruolo di Utente base
     */
    public static final String USER = "USR";

    /**
     * Ruolo di Amministratore
     */
    public static final String ADMINISTRATOR = "ADM";

    public static final String MAPS_MULTIPLE_FLAG_NAME = "isMultiple";
    public static final String MAPS_REPORT_ELEMENT_NAME = "report";
    public static final String MAPS_REPORT_ELEMENTS_NAME = "reports";

    public static final String FOCUS_VIEW_REPORT = "report";

    public static final Float FIRST_FOCUS_LEVEL = 15f;
    public static final Float SECOND_FOCUS_LEVEL = 16f;
    public static final Float THIRD_FOCUS_LEVEL = 17f;

    public static final String REQUEST_GET_ALL = "/reports";
    public static final String REQUEST_GET_ALL_ORDER_BY_GRAVITY = "/reports/orderby/gravity";
    public static final String REQUEST_GET_ALL_ORDER_BY_URGENCY = "/reports/orderby/urgency";

    public static final String FILTER_SALUTE = "Salute";
    public static final String FILTER_FURTO = "Furto";
    public static final String FILTER_VIOLENZA = "Violenza";
    public static final String FILTER_INCENDIO = "Incendio";
    public static final String FILTER_INCIDENTE_STRADALE = "Incidente Stradale";



    public static final String CAR_ACCIDENT_BLURRED_IMAGE_SHORT = "https://firebasestorage.googleapis.com/v0/b/beesafe-pc.appspot.com/o/BlurredImages%2Fcar_accident_short.png?alt=media&token=91a94cac-ebd9-4286-bd91-f15bdee3dbc0";
    public static final String FIRE_BLURRED_IMAGE_SHORT = "https://firebasestorage.googleapis.com/v0/b/beesafe-pc.appspot.com/o/BlurredImages%2Ffire_short.png?alt=media&token=eedcf9de-8cef-4af9-8b5a-74ba88642334";
    public static final String HEALTH_BLURRED_IMAGE_SHORT = "https://firebasestorage.googleapis.com/v0/b/beesafe-pc.appspot.com/o/BlurredImages%2Fhealth_short.png?alt=media&token=e23bd5f3-a9fc-4f9f-9079-bc11302fd8f2";
    public static final String THIEF_BLURRED_IMAGE_SHORT = "https://firebasestorage.googleapis.com/v0/b/beesafe-pc.appspot.com/o/BlurredImages%2Fthief_short.png?alt=media&token=114f3e91-ae4d-47c7-b8b4-15f166063e1f";
    public static final String VIOLENCE_BLURRED_IMAGE_SHORT = "https://firebasestorage.googleapis.com/v0/b/beesafe-pc.appspot.com/o/BlurredImages%2Fviolence_short.png?alt=media&token=b39c98ac-e87f-4794-9ba7-826aed269c32";

    public static final String CAR_ACCIDENT_BLURRED_IMAGE = "https://firebasestorage.googleapis.com/v0/b/beesafe-pc.appspot.com/o/BlurredImages%2Fcar_accident.png?alt=media&token=3bc006ac-3539-434d-8867-b11c1c32e919";
    public static final String FIRE_BLURRED_IMAGE = "https://firebasestorage.googleapis.com/v0/b/beesafe-pc.appspot.com/o/BlurredImages%2Ffire.png?alt=media&token=2ee8d2d8-df10-439c-a367-d358e6b3225e";
    public static final String HEALTH_BLURRED_IMAGE = "https://firebasestorage.googleapis.com/v0/b/beesafe-pc.appspot.com/o/BlurredImages%2Fhealth.png?alt=media&token=346ae8de-3ad3-4045-a82e-ce5b7ffb5a7f";
    public static final String THIEF_BLURRED_IMAGE = "https://firebasestorage.googleapis.com/v0/b/beesafe-pc.appspot.com/o/BlurredImages%2Fthief.png?alt=media&token=f11601bf-dd3e-4425-9abf-c83053ba5cef";
    public static final String VIOLENCE_BLURRED_IMAGE = "https://firebasestorage.googleapis.com/v0/b/beesafe-pc.appspot.com/o/BlurredImages%2Fviolence.png?alt=media&token=970aa8b5-67ec-46f3-b4e4-130bb65eef29";
}
