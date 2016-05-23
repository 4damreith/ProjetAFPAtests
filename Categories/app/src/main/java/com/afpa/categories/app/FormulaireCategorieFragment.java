package com.afpa.categories.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by syjebrane on 19/05/2016.
 */
public class FormulaireCategorieFragment extends WebServiceFragment implements View.OnClickListener {


    private final static String NOM_MODELE = "categorie";
    private ChampAdapter spinnerChampAdapter;
    private FormulaireCategorieListener listener;
    private Spinner spinnerAjouterChamp;

    public FormulaireCategorieFragment() {
        super(NOM_MODELE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.formulaire_categorie_fragment, container, false);
        Button boutonValiderCategorie = (Button) view.findViewById(R.id.boutonValiderCategorie);
        this.spinnerAjouterChamp = (Spinner) view.findViewById(R.id.spinnerAjouterChamp);
        JSONObject json = null;
        try {
            json = JsonUtils.loadJSONFromResources(getActivity(), R.raw.champs);
            this.spinnerChampAdapter = new ChampAdapter(getActivity(), JsonUtils.getKeysAsList(json, new ArrayList<String>()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.spinnerAjouterChamp.setAdapter(this.spinnerChampAdapter);
        boutonValiderCategorie.setOnClickListener(this);
        return view;
    }


    @SuppressWarnings("deprecation")
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
        }
    }

    private void triggerAjouterChamp() {
        //TODO
    }

    private void triggerValiderCategorie() {
        this.listener.fireValidCategorie();
    }

    public interface FormulaireCategorieListener {
        void fireValidCategorie();
    }
}
