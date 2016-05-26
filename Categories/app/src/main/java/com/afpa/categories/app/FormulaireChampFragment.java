package com.afpa.categories.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by syjebrane on 26/05/2016.
 */
public class FormulaireChampFragment extends WebServiceFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_champ_fragment, container, false);
        Spinner spinnerTypeChamp = (Spinner) view.findViewById(R.id.spinnerTypeChamp);
        ArrayList<String> types = new ArrayList<String>();
        for (Type type : Type.values()) {
            types.add(type.name());
        }
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
        return view;
    }
}
