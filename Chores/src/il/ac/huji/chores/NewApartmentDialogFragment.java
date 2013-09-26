package il.ac.huji.chores;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.parse.ParseException;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.exceptions.ApartmentAlreadyExistsException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

/**
 *
 */
public class NewApartmentDialogFragment extends Fragment {

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
        finishBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NewApartmentDialogFragment", "clicked create apartment");
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
                } catch (ParseException e) {
                    if (e.getCode() == ParseException.CONNECTION_FAILED) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.setting_saved_toast_text), Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d(getClass().toString(), "Apartment created: " + apartment.toString());
                // Return results
                getActivity().finish();
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
