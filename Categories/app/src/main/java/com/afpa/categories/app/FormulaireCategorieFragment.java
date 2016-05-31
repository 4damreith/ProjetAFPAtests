package com.afpa.categories.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by syjebrane on 19/05/2016.
 */
public class FormulaireCategorieFragment extends WebService implements View.OnClickListener {


    public final static String CATEGORIE_ARGUMENT_KEY = "mode";

    private static AlertDialog.Builder dialogCreationCategorieBuilder;

    private boolean edit = false;
    private EditText editTextCategorie;
    private ListView listViewChamps;
    private ChampAdapter spinnerChampAdapter;
    private ChampAdapter listViewChampAdapter;
    private FormulaireCategorieListener listener;
    private Spinner spinnerAjouterChamp;
    private JSONObject categorie;
    private String nomCategorie;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_categorie_fragment, container, false);

        //on teste la presence d'arguments
        if (getArguments() != null) {
            //si il y a des arguments, et qu'ils contiennent le nom d'une categorie, alors on passe en mode edition...
            this.edit = getArguments().containsKey(CATEGORIE_ARGUMENT_KEY);
            if (this.edit) {
                //...et on recupere le nom de la categorie a editer
                this.nomCategorie = getArguments().getString(CATEGORIE_ARGUMENT_KEY);
            }
        }

        //initialisation du Builder de dialogues modaux
        dialogCreationCategorieBuilder = new AlertDialog.Builder(getActivity()).setCancelable(false);

        //deserialisation des widgets necessaires
        TextView boutonValiderCategorie = (TextView) view.findViewById(R.id.boutonValiderCategorie);
        TextView boutonAjouterChamp = (TextView) view.findViewById(R.id.boutonAjouterChamp);
        this.spinnerAjouterChamp = (Spinner) view.findViewById(R.id.spinnerAjouterChamp);
        this.listViewChamps = (ListView) view.findViewById(R.id.listViewChamps);
        this.editTextCategorie = (EditText) view.findViewById(R.id.editTextCategorie);

        //si on est en mode d'edition (les arguments contiennnt un Bundle comportant le nom de la categorie à éditer)...
        if (this.edit) {
            //...alors on initialise la listview avec les champs contenus dans la categorie...
            initFormEditMode();
        } else {
            //...sinon on initialise la listview avec un ChampAdapter vide
            initFormCreateMode();
            /*this.listViewChampAdapter = new ChampAdapter(getActivity());*/
        }
        //(dans tous les cas on initialisera le ChampAdapter du spinner de selection de champ, avec les champs trouvés en base, voir les deux méthodes d'initialisation)

        //ajout des adapters
        this.spinnerAjouterChamp.setAdapter(this.spinnerChampAdapter);
        this.listViewChamps.setAdapter(this.listViewChampAdapter);

        //initialisation des evenements
        boutonValiderCategorie.setOnClickListener(this);
        boutonAjouterChamp.setOnClickListener(this);
        return view;
    }

    /**
     * Initialise le spinner de selection de champs
     */
    private void initSpinnerSelectionChamps() {

        //preparation de l'URL, recuperation de tous les champs dispo. dans la BDD
        final String url = WebService.buildUrlForRequest(Metier.DOMAIN, Metier.NOM_MODELE_CATEGORIES, WebService.ACTION_LIST, null);

        //preparation et execution de la requete en asynchrone
        asyncHttpClient.get(getActivity(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //recuperation des donnees et parsing en JSONArray
                String response = null;
                try {
                    response = new String(responseBody, ENCODING);
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("JSONARRAY", jsonArray.toString(1));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.error_msg_fail_retrieve_data) + statusCode).show();
            }
        });
/*
        try {
            JSONObject json = JsonUtils.loadJSONFromResources(getActivity(), R.raw.champs);
            this.spinnerChampAdapter = new ChampAdapter(getActivity(), JsonUtils.getJsonObjects(json, new ArrayList<JSONObject>()));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Initialisation du formulaire en mode de creation
     */
    private void initFormCreateMode() {
        initSpinnerSelectionChamps();
        //TODO
    }

    /**
     * Initialisation du formulaire en mode d'edition
     */
    private void initFormEditMode() {
        initSpinnerSelectionChamps();
        //encodage de la chaine de caracteres correspondant au nom du produit pour etre passé dans l'URL
        String urlCategorieName = null;
        try {
            urlCategorieName = URLEncoder.encode(this.nomCategorie, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //preparation de l'URL
        String[] params = {urlCategorieName};
        final String url = WebService.buildUrlForRequest(Metier.DOMAIN, Metier.NOM_MODELE_CATEGORIES, WebService.ACTION_GET, params);

        //requete pour recuperer la categorie a editer (asynchrone)
        asyncHttpClient.get(getActivity(), url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, ENCODING);
                    JSONObject jsonObject = new JSONObject(response);
                    //TODO PAUSE
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("CATEGORIE_EDIT", "fail to connect: " + url + " " + statusCode);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        onAttachToContext(activity);
        super.onAttach(activity);
    }

    protected void onAttachToContext(Context context) {
        if (context instanceof FormulaireCategorieListener) {
            this.listener = (FormulaireCategorieListener) context;
        } else {
            throw new ClassCastException("L'activite conteneur doit implementer l'interface FormulaireCategorieFragment.FormulaireCategorieListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.boutonAnnulerCategorie:
                triggerAnnulerCategorie();
                break;
            case R.id.boutonValiderCategorie:
                triggerValiderCategorie();
                break;
            case R.id.boutonAjouterChamp:
                triggerAjouterChamp(this.spinnerChampAdapter.getItem(this.spinnerAjouterChamp.getSelectedItemPosition()));
                break;
        }
    }

    /**
     * A executer lors du clic sur le bouton Annuler
     * Notification de l'evenement a l'activite conteneur
     */
    private void triggerAnnulerCategorie() {
        this.listener.OnCancelCategorie();
    }

    /**
     * A executer lors du clic sur le bouton d'ajout de champ
     * Ajoute un nouveau chanp au format JSON a la ListView des champs
     */
    private void triggerAjouterChamp(JSONObject item) {
        this.listViewChampAdapter.add(item);
    }


    /**
     * A executer lors du clic sur le bouton Valider
     * Preparation d'une nouvelle categorie au format JSON et appel de la methode d'insertion en BDD
     */
    private void triggerValiderCategorie() {
        JSONObject categorie = new JSONObject();
        String categorie_nom = this.editTextCategorie.getText().toString();
        try {
            categorie.put(Metier.CLE_CATEGORIE_NOM, categorie_nom);
            categorie.put(Metier.CLE_PRODUIT_NOM, "");
            for (int i = 0; i < this.listViewChampAdapter.getCount(); i++) {
                JSONObject champ = this.listViewChampAdapter.getItem(i);
                categorie.put(champ.optString(Metier.CLE_CHAMP_NOM), "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        insererCategorie(categorie);
    }

    /**
     * Insertion d'une nouvelle categorie en BDD
     *
     * @param categorie la nouvelle categorie au format JSON
     */
    private void insererCategorie(final JSONObject categorie) {
        //preparation du JSON pour le passer dans le corps de la requete HTTP
        StringEntity entityJson = null;
        try {
            entityJson = new StringEntity(categorie.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //preparation de l'URL
        String url = WebService.buildUrlForRequest(Metier.DOMAIN, Metier.NOM_MODELE_CATEGORIES, WebService.ACTION_INSERT, null);

        //requete pour inserer la nouvelle categorie (asynchrone)
        asyncHttpClient.post(getActivity(), url, entityJson, DATA_TYPE, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialogCreationCategorieBuilder
                        .setMessage(getString(R.string.dialog_ajout_categorie_succes))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                FormulaireCategorieFragment.this.listener.OnValidCategorie();
                            }
                        }).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialogCreationCategorieBuilder
                        .setMessage(getString(R.string.dialog_ajout_categorie_echec))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    public interface FormulaireCategorieListener {

        void OnValidCategorie();

        void OnCancelCategorie();
    }
}
