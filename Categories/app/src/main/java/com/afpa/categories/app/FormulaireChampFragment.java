package com.afpa.categories.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by syjebrane on 26/05/2016.
 */
public class FormulaireChampFragment extends WebService implements View.OnClickListener {


    private static AlertDialog.Builder dialogCreationCategorieBuilder;
    private ArrayAdapter<String> typesChampAdapter;
    private EditText editTextNomChamp;

    private Spinner spinnerTypeChamp;

    private FormulaireCategorieListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_champ_fragment, container, false);

        //initialisation du Builder de dialogues modaux
        dialogCreationCategorieBuilder = new AlertDialog.Builder(getActivity()).setCancelable(false);

        //deserialisation des widgets necessaires
        this.editTextNomChamp = (EditText) view.findViewById(R.id.editTextNomChamp);
        this.spinnerTypeChamp = (Spinner) view.findViewById(R.id.spinnerTypeChamp);
        Button boutonAjouterChamp = (Button) view.findViewById(R.id.boutonAjouterChamp);

        //initialisation et ajout des adapters
        ArrayList<String> types = new ArrayList<String>();
        for (Type type : Type.values()) {
            types.add(type.name());
        }
        this.typesChampAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, types);
        this.spinnerTypeChamp.setAdapter(this.typesChampAdapter);

        //initialisation des evenements
        boutonAjouterChamp.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boutonAjouterChamp:
                triggerValiderChamp();
                break;
            case R.id.boutonAnnulerChamp:
                triggerAnnulerChamp();
                break;
        }
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
            throw new ClassCastException("L'activite conteneur doit implementer l'interface FormulaireChampFragment.FormulaireCategorieListener");
        }
    }

    /**
     * A executer lors du clic sur le bouton Annuler
     * Notification de l'evenement a l'activite conteneur
     */
    private void triggerAnnulerChamp() {
        this.listener.OnCancelChamp();
    }

    /**
     * Preparation d'un objet JSON (nouveau champ) et appel de la methode d'insertion
     */
    private void triggerValiderChamp() {
        JSONObject champ = new JSONObject();
        String libelleChamp = this.editTextNomChamp.getText().toString();
        String typeChamp = this.typesChampAdapter.getItem(this.spinnerTypeChamp.getSelectedItemPosition());
        try {
            //TODO remplacer par des constantes metier, preparer une classe pour y acceder
            champ.put("champ", libelleChamp);
            champ.put("type", typeChamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        insererChamp(champ);
    }

    /**
     * Insertion d'un nouveau champ dans la base et feedback
     *
     * @param champ le champ a inserer formatte en JSONObject
     */
    private void insererChamp(final JSONObject champ) {
        //preparation du JSON pour le passer dans le corps de la requete HTTP
        StringEntity entityJson = null;
        try {
            entityJson = new StringEntity(champ.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //preparation de l'URL
        final String url = WebService.buildUrlForRequest(Metier.DOMAIN, Metier.NOM_MODELE_CHAMPS, WebService.ACTION_INSERT, null);

        //requete pour recuperer la categorie a editer (asynchrone)
        asyncHttpClient.post(getActivity(), url, entityJson, DATA_TYPE, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialogCreationCategorieBuilder
                        .setMessage(getString(R.string.dialog_ajout_champ_succes))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                FormulaireChampFragment.this.listener.OnValidChamp();
                            }
                        }).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialogCreationCategorieBuilder
                        .setMessage(getString(R.string.error_msg_fail_insert_data) + url + "\r\n" + statusCode)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    /**
     * Interface d'ecoute du Fragment, l'activite conteneur devra implementer cette interface
     * pour capter les evenements au sein du fragment courant.
     */
    public interface FormulaireCategorieListener {

        void OnValidChamp();

        void OnCancelChamp();
    }
}
