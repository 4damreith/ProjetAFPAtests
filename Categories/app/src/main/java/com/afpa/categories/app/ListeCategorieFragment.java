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
public class ListeCategorieFragment extends WebServiceFragment implements AdapterView.OnItemClickListener {

    private static final String CATEGORIE_NOM_KEY = "categorie";
    private final static String NOM_MODELE = "/categorie";
    private List<String> nomsCategories;
    private AutoCompleteTextView autoCompleteTextView;
    private ListView listViewCategories;
    private ListeCategorieFragmentListener listener;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_categorie_fragment, container, false);
        this.nomsCategories = new ArrayList<String>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initCategories();
    }

    private void initCategories() {

        String url = DOMAIN + NOM_MODELE + ACTION_LIST;
        asyncHttpClient.get(getActivity(), url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                try {
                    //retrieving data
                    response = new String(responseBody, ENCODING);
                    JSONArray jsonArray = new JSONArray(response);
                    ListeCategorieFragment.this.nomsCategories = JsonUtils.getValuesFromArray(jsonArray, CATEGORIE_NOM_KEY, new ArrayList<String>());

                    //init autocomplete filter text field
                    ArrayAdapter<String> autoCompleteTextViewAdapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_dropdown_item_1line,
                            ListeCategorieFragment.this.nomsCategories);
                    ListeCategorieFragment.this.autoCompleteTextView = (AutoCompleteTextView)
                            ListeCategorieFragment.this.getView().findViewById(R.id.filterAutoCompleteTextView);
                    ListeCategorieFragment.this.autoCompleteTextView.setAdapter(autoCompleteTextViewAdapter);

                    //init listview
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
                new AlertDialog.Builder(getActivity()).setMessage("Failed to retrieve data. Status code: " + statusCode).show();
            }
        });
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
        //TODO definir des methodes d'ecoute au besoin
    }
}
