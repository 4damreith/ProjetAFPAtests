package com.afpa.categories.app;

import android.app.Fragment;
import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by syjebrane on 19/05/2016.
 */
public abstract class WebServiceFragment extends Fragment {

    //methodes d'acces en lecture ecriture (REST)
    public final static String ACTION_UPDATE = "/update";
    public final static String ACTION_GET = "/get";
    public final static String ACTION_LIST = "/liste";
    public final static String ACTION_INSERT = "/insert";

    //encodage des caracteres
    public final static String ENCODING = "ISO-8859-1";

    //type MIME
    public static final String DATA_TYPE = "application/json";

    //domaine/url du web service
    public static final String DOMAIN = "http://pc872:8085/Glutton-1.0-SNAPSHOT/acces";

    //client http asynchrone
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

}
