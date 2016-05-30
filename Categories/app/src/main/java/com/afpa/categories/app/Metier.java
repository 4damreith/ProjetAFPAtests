package com.afpa.categories.app;

import java.util.HashMap;

/**
 * Une classe utilitaire qui regroupe des methodes pour acceder aux constantes "metier"
 * <p/>
 * ALTERNATIVE POSSIBLE: Les constantes peuvent etre stockees dans les ressources de l'app (ex res/values/metier.xml)
 * et etre deserialisees a la demande (lazy-loading)
 * <p/>
 * Created by syjebrane on 30/05/2016.
 */
public final class Metier {

    /* DOMAINE/URL web service */
    public static final String DOMAIN = "http://pc872:8085/Glutton-1.0-SNAPSHOT/acces";

    /* CHAMPS */
    public final static String CLE_CHAMP_NOM = "champ";
    public static final String CLE_CHAMP_TYPE = "type";
    public static final String NOM_MODELE_CHAMPS = "champs";

    /* CATEGORIES */
    public final static String CLE_CATEGORIE_NOM = "categorie";
    public static final String NOM_MODELE_CATEGORIES = "categories";

    /* PRODUITS */
    public static final String CLE_PRODUIT_NOM = "nom";

    /*ICONES representant les differents types de donnees*/
    public static final HashMap<String, Integer> TYPE_ICONS;

    static {
        TYPE_ICONS = new HashMap<String, Integer>();
        TYPE_ICONS.put(Type.alpha.name(), R.drawable.ic_format_quote_black_48dp);
        TYPE_ICONS.put(Type.integer.name(), R.drawable.ic_numeric_48dp);
        TYPE_ICONS.put(Type.decimal.name(), R.drawable.ic_numeric_48dp);
        TYPE_ICONS.put(Type.date.name(), R.drawable.ic_date_range_black_48dp);
        TYPE_ICONS.put(Type.time.name(), R.drawable.ic_access_time_black_48dp);
        TYPE_ICONS.put(Type.liste.name(), R.drawable.ic_list_black_48dp);
    }

}
