package com.afpa.categories.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
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


    private final static String NOM_MODELE = "/categorie";

    private static Dialog dialogCreationCategorie;
    private static TextView textViewConfirmDialog;


    private EditText editTextCategorie;
    private ListView listViewChamps;
    private ChampAdapter spinnerChampAdapter;
    private ChampAdapter listViewChampAdapter;
    private FormulaireCategorieListener listener;
    private Spinner spinnerAjouterChamp;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_categorie_fragment, container, false);

        //init dialog
        View viewDialog = inflater.inflate(R.layout.dialog_create_categorie, null, false);
        textViewConfirmDialog = (TextView) viewDialog.findViewById(R.id.textViewConfirmDialog);
        ButtonRectangle confirmButtonDialog = (ButtonRectangle) viewDialog.findViewById(R.id.boutonConfirmDialog);
        confirmButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCreationCategorie.dismiss();
            }
        });
        dialogCreationCategorie = new AlertDialog.Builder(getActivity()).setCancelable(false).setView(viewDialog).create();

        //init widgets
        ButtonFloat boutonValiderCategorie = (ButtonFloat) view.findViewById(R.id.boutonValiderCategorie);
        ButtonFloat boutonAjouterChamp = (ButtonFloat) view.findViewById(R.id.boutonAjouterChamp);
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
        this.listViewChampAdapter = new ChampAdapter(getActivity());

        //set adapters to adapterViews
        this.spinnerAjouterChamp.setAdapter(this.spinnerChampAdapter);
        this.listViewChamps.setAdapter(this.listViewChampAdapter);

        //init events
        boutonValiderCategorie.setOnClickListener(this);
        boutonAjouterChamp.setOnClickListener(this);
        return view;
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
            case R.id.boutonValiderCategorie:
                triggerValiderCategorie();
                break;
            case R.id.boutonAjouterChamp:
                triggerAjouterChamp(this.spinnerChampAdapter.getItem(this.spinnerAjouterChamp.getSelectedItemPosition()));
                break;
        }
    }

    private void triggerAjouterChamp(JSONObject item) {
        this.listViewChampAdapter.add(item);
    }

    /*http://localhost:8088/Glutton-1.0-SNAPSHOT/acces/categorie/insert/{values} */
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
                textViewConfirmDialog.setText(R.string.dialog_ajout_categorie_succes);
                dialogCreationCategorie.show();
                FormulaireCategorieFragment.this.listener.OnValidCategorie();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                textViewConfirmDialog.setText(R.string.dialog_ajout_categorie_succes);
                dialogCreationCategorie.show();
            }
        });
    }

    public interface FormulaireCategorieListener {
        void OnValidCategorie();
    }
}
