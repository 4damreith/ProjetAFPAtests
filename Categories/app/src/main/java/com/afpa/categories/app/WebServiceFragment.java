package com.afpa.categories.app;

import android.app.Fragment;
import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by syjebrane on 19/05/2016.
 */
public abstract class WebServiceFragment extends Fragment {

    protected static final String DATA_TYPE = "application/json";
    protected static final String DOMAIN = "http://pc872:8085/Glutton-1.0-SNAPSHOT/acces";
    /*private final static String ACTION_INSERT = "/insert";*/
    protected final static String ACTION_UPDATE = "/update";
    protected final static String ACTION_INSERT = "/body";
    protected final static String ACTION_GET = "/get";
    protected final static String ACTION_LIST = "/liste";
    protected final static String ENCODING = "ISO-8859-1";
    protected static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

}
