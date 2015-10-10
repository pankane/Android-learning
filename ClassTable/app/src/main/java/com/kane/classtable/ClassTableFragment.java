package com.kane.classtable;


import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ClassTableFragment extends Fragment {
private View view;
    public ClassTableFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_class_table, container, false);
        TextView tvTest=(TextView)view.findViewById(R.id.test);
 tvTest.setText("this is classtable");





        return view;
    }



}
