package il.ac.huji.chores;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link NewApartmentDialogFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link NewApartmentDialogFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class NewApartmentDialogFragment extends Fragment {

    private RoommatesApartment apartment;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_apartment_dialog, container, false);
        Button finishBtn = (Button) view.findViewById(R.id.newApartmentFinishButton);
        finishBtn.setOnClickListener( new OnClickListener() {
            @Override public void onClick(View v) {
                apartment = new RoommatesApartment();
                apartment.setName("Test1");
                apartment.createApartment();
                Log.d(getClass().toString(), "Apartment created");
            }
        });
        return view;
    }

}
