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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by syjebrane on 19/05/2016.
 */
public class FormulaireCategorieFragment extends WebServiceFragment implements View.OnClickListener {


    public final static String CATEGORIE_ARGUMENT_KEY = "mode";

    private final static String NOM_MODELE = "/categorie";

    private static AlertDialog.Builder dialogCreationCategorieBuilder;

    private boolean edit;
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

        this.edit = getArguments().containsKey(CATEGORIE_ARGUMENT_KEY);
        if (this.edit)
            this.nomCategorie = getArguments().getString(CATEGORIE_ARGUMENT_KEY);

        //init dialog
        dialogCreationCategorieBuilder = new AlertDialog.Builder(getActivity()).setCancelable(false);

        //init widgets
        TextView boutonValiderCategorie = (TextView) view.findViewById(R.id.boutonValiderCategorie);
        TextView boutonAjouterChamp = (TextView) view.findViewById(R.id.boutonAjouterChamp);
        this.spinnerAjouterChamp = (Spinner) view.findViewById(R.id.spinnerAjouterChamp);
        this.listViewChamps = (ListView) view.findViewById(R.id.listViewChamps);
        this.editTextCategorie = (EditText) view.findViewById(R.id.editTextCategorie);

        //init adapters
        try {
            JSONObject json = JsonUtils.loadJSONFromResources(getActivity(), R.raw.champs);
            this.spinnerChampAdapter = new ChampAdapter(getActivity(), JsonUtils.getJsonObjects(json, new ArrayList<JSONObject>()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.edit) {
            initFormEditMode();
        } else {
            this.listViewChampAdapter = new ChampAdapter(getActivity());
        }

        //set adapters to adapterViews
        this.spinnerAjouterChamp.setAdapter(this.spinnerChampAdapter);
        this.listViewChamps.setAdapter(this.listViewChampAdapter);

        //init events
        boutonValiderCategorie.setOnClickListener(this);
        boutonAjouterChamp.setOnClickListener(this);
        return view;
    }

    private void initFormEditMode() {
        final String url = DOMAIN + NOM_MODELE + ACTION_GET + "/" + this.nomCategorie;
        asyncHttpClient.get(getActivity(), url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, ENCODING);
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("CATEGORIE_EDIT", jsonObject.toString(1));
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

    /*
     * Called when the fragment attaches to the context
     */
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

    private void triggerAnnulerCategorie() {
        this.listener.OnCancelCategorie();
    }

    private void triggerAjouterChamp(JSONObject item) {
        this.listViewChampAdapter.add(item);
    }

    private void triggerValiderCategorie() {
        JSONObject categorie = new JSONObject();
        String categorie_nom = this.editTextCategorie.getText().toString();
        try {
            categorie.put("categorie", categorie_nom);
            categorie.put("nom", "");
            for (int i = 0; i < this.listViewChampAdapter.getCount(); i++) {
                JSONObject champ = this.listViewChampAdapter.getItem(i);
                categorie.put(champ.optString(ChampAdapter.LIBELLE_CHAMP_KEY), champ.optString(ChampAdapter.TYPE_CHAMP_KEY));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        insererCategorie(categorie);
    }

    private void insererCategorie(final JSONObject categorie) {
        try {
            Log.e("CAT ", categorie.toString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entityJson = null;
        try {
            entityJson = new StringEntity(categorie.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = DOMAIN + NOM_MODELE + ACTION_INSERT;
        asyncHttpClient.post(getActivity(), url, entityJson, DATA_TYPE, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialogCreationCategorieBuilder
                        .setMessage(getString(R.string.dialog_ajout_categorie_succes))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FormulaireCategorieFragment.this.listener.OnValidCategorie();
                                dialog.dismiss();
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
