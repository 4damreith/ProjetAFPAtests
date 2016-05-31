package com.afpa.categories.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by syjebrane on 25/05/2016.
 */
public class ListeCategorieFragment extends WebService implements AdapterView.OnItemClickListener {

    private List<String> nomsCategories;
    private AutoCompleteTextView autoCompleteTextView;
    private ListView listViewCategories;
    private ListeCategorieFragmentListener listener;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.nomsCategories = new ArrayList<String>();
        return inflater.inflate(R.layout.list_categorie_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initCategories();
    }

    /* Initialisation du champ texte de recherche et de la liste des categories */
    private void initCategories() {
        //preparation de l'url pour la requete
        String url = WebService.buildUrlForRequest(Metier.DOMAIN, Metier.NOM_MODELE_CATEGORIES, WebService.ACTION_LIST, null);

        //preparation et execution de la requete en asynchrone
        asyncHttpClient.get(getActivity(), url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    //recuperation des donnees et parsing en JSONArray
                    String response = new String(responseBody, ENCODING);
                    JSONArray jsonArray = new JSONArray(response);
                    ListeCategorieFragment.this.nomsCategories = JsonUtils.getValuesFromJSONArray(jsonArray, Metier.CLE_CATEGORIE_NOM, new ArrayList<String>());

                    //initialisation du champ texte aec auto-completion
                    ArrayAdapter<String> autoCompleteTextViewAdapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_dropdown_item_1line,
                            ListeCategorieFragment.this.nomsCategories);
                    ListeCategorieFragment.this.autoCompleteTextView = (AutoCompleteTextView)
                            ListeCategorieFragment.this.getView().findViewById(R.id.filterAutoCompleteTextView);
                    ListeCategorieFragment.this.autoCompleteTextView.setAdapter(autoCompleteTextViewAdapter);

                    //initialisation de la liste, elle partage l'adapter (destine a gerer l'autocompletion) )avec le champ texte
                    ListeCategorieFragment.this.listViewCategories = (ListView)
                            ListeCategorieFragment.this.getView().findViewById(R.id.listViewCategories);
                    ListeCategorieFragment.this.listViewCategories.setAdapter(autoCompleteTextViewAdapter);
                    ListeCategorieFragment.this.listViewCategories.setOnItemClickListener(ListeCategorieFragment.this);

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
    }

    @Override
    public void onAttach(Activity activity) {
        onAttachToContext(activity);
        super.onAttach(activity);
    }


    protected void onAttachToContext(Context context) {
        if (context instanceof ListeCategorieFragmentListener) {
            this.listener = (ListeCategorieFragmentListener) context;
        } else {
            throw new ClassCastException("L'activite conteneur doit implementer l'interface ListeCategorieFragment.ListeCategorieFragmentListener");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView) view;
        new AlertDialog.Builder(getActivity()).setMessage(tv.getText()).show();

    }

    public interface ListeCategorieFragmentListener {
        //TODO ajouter une methode pour le mode "creation de produit"
        void OnCategorieSelected(String nomCategorie);
    }
}
