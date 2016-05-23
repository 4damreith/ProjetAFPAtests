package com.afpa.categories.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by syjebrane on 23/05/2016.
 */
public class ChampAdapter extends ArrayAdapter<String> {

    public ChampAdapter(Context context, List<String> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
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
            //passage du viewHolder comme Tag au convertView
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ChampAdapterHolder) convertView.getTag();
        }
        String champCourant = getItem(position);
        if (champCourant != null) {
            viewHolder.textViewItemChamp.setText(champCourant);
        }
        return convertView;
    }

    private static class ChampAdapterHolder {
        public TextView textViewItemChamp;
    }
}