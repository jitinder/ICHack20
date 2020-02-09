package com.kill.bill;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemSelection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ItemSelection extends Fragment {

    String name;

    public ItemSelection(String name) {
        this.name = name;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_item_selection, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        TextView nametv = getView().findViewById(R.id.item_selection_textview);
        nametv.setText("Select items for: " +name);
    }

}
