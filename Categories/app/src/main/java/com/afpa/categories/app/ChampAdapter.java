package com.afpa.categories.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestion des donnees sous-jacentes et la coherence de leur affichage dans les AdapterViews contenant des "champs"
 * <p/>
 * Created by syjebrane on 23/05/2016.
 */
public class ChampAdapter extends ArrayAdapter<JSONObject> {

    public ChampAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<JSONObject>());
    }

    public ChampAdapter(Context context, List<JSONObject> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChampAdapterHolder viewHolder = null;
        if (convertView == null) {
            //initialisation du layout
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_champ, parent, false);
            //initialisation du viewHolder
            viewHolder = new ChampAdapterHolder();
            viewHolder.textViewItemChamp = (TextView) convertView.findViewById(R.id.textViewItemChamp);
            viewHolder.imageViewItemChamp = (ImageView) convertView.findViewById(R.id.imageViewItemChamp);
            //passage du viewHolder comme Tag au convertView
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ChampAdapterHolder) convertView.getTag();
        }
        //initialisation du texte et de l'icone en fonction du champ courant
        JSONObject champCourant = getItem(position);
        if (champCourant != null) {
            viewHolder.textViewItemChamp.setText(champCourant.optString(Metier.CLE_CHAMP_NOM));
            viewHolder.imageViewItemChamp.setImageResource(getImageResourceForItem(champCourant));
        }
        return convertView;
    }

    /**
     * Recupere l'icone correspondant à un type de donnees
     */
    private int getImageResourceForItem(JSONObject champCourant) {
        String type = champCourant.optString(Metier.CLE_CHAMP_TYPE);
        if (type != null && !type.isEmpty() && Metier.TYPE_ICONS.containsKey(type)) {
            return Metier.TYPE_ICONS.get(type);
        }
        return R.drawable.ic_not_interested_black_48dp;
    }

    /**
     * Technique du "holder", sert a optimiser le scroll dans les AdapterViews
     */
    private static class ChampAdapterHolder {
        public TextView textViewItemChamp;
        public ImageView imageViewItemChamp;
    }


}