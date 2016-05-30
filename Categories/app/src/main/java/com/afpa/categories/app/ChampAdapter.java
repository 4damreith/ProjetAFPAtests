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
import java.util.HashMap;
import java.util.List;

/**
 * Created by syjebrane on 23/05/2016.
 */
public class ChampAdapter extends ArrayAdapter<JSONObject> {

    public static final String LIBELLE_CHAMP_KEY = "libelle";
    public static final String TYPE_CHAMP_KEY = "type";
    private static final HashMap<String, Integer> TYPE_ICONS;

    static {
        TYPE_ICONS = new HashMap<String, Integer>();
        TYPE_ICONS.put(Type.alpha.name(), R.drawable.ic_format_quote_black_48dp);
        TYPE_ICONS.put(Type.integer.name(), R.drawable.ic_numeric_48dp);
        TYPE_ICONS.put(Type.decimal.name(), R.drawable.ic_numeric_48dp);
        TYPE_ICONS.put(Type.date.name(), R.drawable.ic_date_range_black_48dp);
        TYPE_ICONS.put(Type.time.name(), R.drawable.ic_access_time_black_48dp);
        TYPE_ICONS.put(Type.liste.name(), R.drawable.ic_list_black_48dp);
    }

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
        JSONObject champCourant = getItem(position);


        if (champCourant != null) {
            viewHolder.textViewItemChamp.setText(champCourant.optString(LIBELLE_CHAMP_KEY));
            viewHolder.imageViewItemChamp.setImageResource(getImageResourceForItem(champCourant));
        }
        return convertView;
    }

    private int getImageResourceForItem(JSONObject champCourant) {
        String type = champCourant.optString(TYPE_CHAMP_KEY);
        if (type != null && !type.isEmpty() && TYPE_ICONS.containsKey(type)) {
            return TYPE_ICONS.get(type);
        }
        return R.drawable.ic_not_interested_black_48dp;
    }

    private static class ChampAdapterHolder {
        public TextView textViewItemChamp;
        public ImageView imageViewItemChamp;
    }


}