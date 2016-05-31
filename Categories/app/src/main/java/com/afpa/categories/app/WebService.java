package com.afpa.categories.app;

import android.app.Fragment;
import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by syjebrane on 19/05/2016.
 */
public abstract class WebService extends Fragment {

    //methodes d'acces en lecture ecriture (REST)
    public final static String ACTION_UPDATE = "update";
    public final static String ACTION_GET = "get";
    public final static String ACTION_LIST = "liste";
    public final static String ACTION_INSERT = "insert";

    //encodage des caracteres
    public final static String ENCODING = "ISO-8859-1";

    //type MIME
    public static final String DATA_TYPE = "application/json";


    //client http asynchrone
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    /**
     * Construit une url pour  consomation des webservices
     *
     * @param domain le domaine a appeler
     * @param model  le nom du modele auquel on veut acceder
     * @param action le nom de l'action a effectuer (typiquement findAll, findById, etc.)
     * @param params d'autres parametres optionnels (filtres par ex)
     * @return l'url coomposee du domaine, du modele, de l'action et des autres parametres
     * <p/>
     * ex : http://pc872:8085/Glutton-1.0-SNAPSHOT/acces/produit/get/123 (Acces a la liste du produit qui porte l'id 123)
     */
    public static String buildUrlForRequest(String domain, String model, String action, String[] params) {
        String url = domain + "/" + model + "/" + action;
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                url += "/" + params[i];
            }
        }
        return url;
    }

}
