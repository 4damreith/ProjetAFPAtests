package com.afpa.categories.app;

import android.app.Fragment;
import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by syjebrane on 19/05/2016.
 */
public abstract class WebServiceFragment extends Fragment {

    protected static final String DOMAIN = "http://pc872:8085/Glutton-1.0-SNAPSHOT/acces/";
    protected static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private final String nomModele;

    public WebServiceFragment(String nomModele) {
        this.nomModele = nomModele;
    }
}
