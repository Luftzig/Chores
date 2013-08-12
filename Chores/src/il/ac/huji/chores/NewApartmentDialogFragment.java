package il.ac.huji.chores;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.provider.ContactsContract;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_apartment_dialog, container, false);

        // Move focus from the name textEdit
        EditText nameInput = (EditText) view.findViewById(R.id.newApartmentNameEdit);
        nameInput.setOnEditorActionListener(
            new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event.getAction() == KeyEvent.ACTION_DOWN 
                            && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        v.clearFocus();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        AdapterView.OnItemSelectedListener spinnerSelectListener = 
            new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapter, View v,
                        int position, long id) {
                    adapter.clearFocus();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                    // Noop
                }
            };

        // Populate divison day spinner
        Spinner divisonDaySpinner = (Spinner) view.findViewById(R.id.newApartmentDivisionDaySpinner);
        @SuppressWarnings("rawtypes")
        ArrayAdapter divisonDayAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.days_of_the_week_array,
                android.R.layout.simple_spinner_item);
        divisonDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisonDaySpinner.setAdapter(divisonDayAdapter);
        divisonDaySpinner.setOnItemSelectedListener(spinnerSelectListener);

        // Populate divison period spinner
        Spinner divisonPeriodSpinner = (Spinner) view.findViewById(R.id.newApartmentDivisonPeriodSpinner);
        @SuppressWarnings("rawtypes")
        ArrayAdapter divisonPeriodAdapter =
            ArrayAdapter.createFromResource( 
                    getActivity(), 
                    R.array.chores_divison_periods,
                    android.R.layout.simple_spinner_item);
        divisonPeriodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisonPeriodSpinner.setAdapter(divisonPeriodAdapter);
        divisonPeriodSpinner.setOnItemSelectedListener(spinnerSelectListener);

        // Complete apartment creation
        Button finishBtn = (Button) view.findViewById(R.id.newApartmentFinishButton);
        finishBtn.setOnClickListener( new OnClickListener() {
            @Override public void onClick(View v) {
                apartment = new RoommatesApartment();
                apartment.setName("Test1");
                apartment.createApartment();
                Log.d(getClass().toString(), "Apartment created");
            }
        });

        // Invite Contacts
        // Get contact list for autocomplete
        AutoCompleteTextView inviteEdit = (AutoCompleteTextView) view.findViewById(R.id.newApartmentInviteEditText);
        ContentResolver cr = getActivity().getContentResolver();
        Uri contacts = Uri.parse("content://icc/adn");
        Cursor contactsCursor = cr.query(contacts, null, null, null, null);
        SimpleCursorAdapter contactsAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                contactsCursor,
                new String[] {"name"},
                new int[] {android.R.id.text1},
                0);
        inviteEdit.setAdapter(contactsAdapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

}
