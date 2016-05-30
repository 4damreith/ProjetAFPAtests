package com.afpa.categories.app;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity implements FormulaireCategorieFragment.FormulaireCategorieListener, ListeCategorieFragment.ListeCategorieFragmentListener, FormulaireChampFragment.FormulaireCategorieListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new FormulaireChampFragment())
                    .commit();
        }
      /*  FragmentManager fm = getFragmentManager();
        Fragment frag = new FormulaireCategorieFragment();
        Bundle args = new Bundle();
        args.putString(FormulaireCategorieFragment.CATEGORIE_ARGUMENT_KEY, "category");*//*
        frag.setArguments(args);*//*
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, frag);
        ft.commit();*/
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/


    @Override
    public void OnValidCategorie() {
        //TODO
    }

    @Override
    public void OnCancelCategorie() {
        //TODO
    }

    @Override
    public void OnCategorieSelected(String nomCategorie) {
        //TODO
    }

    @Override
    public void OnValidChamp() {
        //TODO
    }

    @Override
    public void OnCancelChamp() {
        //TODO
    }
}
