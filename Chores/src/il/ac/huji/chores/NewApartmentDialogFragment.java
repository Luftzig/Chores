package il.ac.huji.chores;

import java.util.ArrayList;

import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.exceptions.ApartmentAlreadyExistsException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
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
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.provider.Contacts;
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
    private int invitedIds = 0;
    private ArrayList<String> invited;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_apartment_dialog, container, false);

        // Move focus from the name textEdit
        final EditText nameInput = (EditText) view.findViewById(R.id.newApartmentNameEdit);
        nameInput.setOnEditorActionListener(
            new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND
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
        final Spinner divisonDaySpinner = (Spinner) view.findViewById(R.id.newApartmentDivisionDaySpinner);
        @SuppressWarnings("rawtypes")
        ArrayAdapter divisonDayAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.days_of_the_week_array,
                android.R.layout.simple_spinner_item);
        divisonDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisonDaySpinner.setAdapter(divisonDayAdapter);
        divisonDaySpinner.setOnItemSelectedListener(spinnerSelectListener);

        // Populate divison period spinner
        final Spinner divisonPeriodSpinner = (Spinner) view.findViewById(R.id.newApartmentDivisonPeriodSpinner);
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
                String apartmentName = nameInput.getText().toString();
                RoommatesApartment apartment = new RoommatesApartment();
                String divisonDay = divisonDaySpinner.getSelectedItem().toString();
                String divisonPeriod = divisonPeriodSpinner.getSelectedItem().toString();
                apartment.setName(apartmentName);
                apartment.setDivisionDay(divisonDay);
                apartment.setDivisionFrequency(divisonPeriod);
                try {
                String apartmentId = ApartmentDAL.createApartment(apartment);
                } catch (ApartmentAlreadyExistsException e) {
                    //  TODO handle exception
                    Log.d("NewApartmentDialogFragment", "Caught exception" + e);
                } catch (UserNotLoggedInException e) {
                    Log.d("NewApartmentDialogFragment", "Caught exception" + e);
                }
                Log.d(getClass().toString(), "Apartment created: " + apartment.toString());
            }
        });

        final String[] PEOPLE_PROJECTION = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
        };

        class ContactsCursorAdapter extends CursorAdapter implements Filterable {
            private ContentResolver content;

            public ContactsCursorAdapter(Context context, Cursor c) {
                super(context, c);
                content = context.getContentResolver();
            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                final LayoutInflater inflater = LayoutInflater.from(context);
                final TextView view = (TextView) inflater.inflate(
                        android.R.layout.simple_dropdown_item_1line, parent, false);
                view.setText(cursor.getString(1));
                Log.d("ContactsCursorAdapter", "new view " + cursor.getString(1));
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                Log.d("ContactsCursorAdapter", "Binding view " + cursor.getString(1));
                ((TextView) view).setText(cursor.getString(1));
            }

            @Override
            public String convertToString(Cursor cursor) {
                return cursor.getString(1);
            }

            @Override
            public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
                if (getFilterQueryProvider() != null) {
                    return getFilterQueryProvider().runQuery(constraint);
                }

                StringBuilder buffer = null;
                String[] args = null;
                if (constraint != null) {
                    buffer = new StringBuilder();
                    buffer.append("UPPER(");
                    buffer.append(ContactsContract.Contacts.DISPLAY_NAME);
                    buffer.append(") GLOB ?");
                    args = new String[] { constraint.toString().toUpperCase() + "*" };
                }

                return content.query(ContactsContract.Contacts.CONTENT_URI, PEOPLE_PROJECTION,
                        buffer == null ? null : buffer.toString(), args,
                        ContactsContract.Contacts.SORT_KEY_PRIMARY);
            }
        }

        // Invite Contacts
        // Get contact list for autocomplete
        final AutoCompleteTextView inviteEdit = (AutoCompleteTextView) view.findViewById(R.id.newApartmentInviteEditText);
        ContentResolver cr = getActivity().getContentResolver();
        Cursor contactsCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        inviteEdit.setAdapter(new ContactsCursorAdapter(getActivity(), contactsCursor));

        final LinearLayout invitedLayout = (LinearLayout) view.findViewById(R.id.newApartmentInvitedLayout);
        Button inviteButton = (Button) view.findViewById(R.id.newApartmentInviteButton);
        /* 
        inviteEdit.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                String contactName = inviteEdit.getText().toString();
                final TextView nameView = new TextView(getActivity());
                nameView.setText(contactName);
                nameView.setId(invitedIds++);
                invitedLayout.addView(nameView);
                inviteEdit.setText("");
                return false;
            }
        });
        */

        inviteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String contactName = inviteEdit.getText().toString();
                invited.add(contactName);
                final TextView nameView = new TextView(getActivity());
                nameView.setText(contactName);
                nameView.setId(invitedIds++);
                invitedLayout.addView(nameView);
                inviteEdit.setText("");
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

}
