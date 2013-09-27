package il.ac.huji.chores;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
		// Move focus from the name textEdit
		final EditText nameInput = (EditText) getActivity().findViewById(
				R.id.newApartmentNameEdit);
		nameInput.setFocusableInTouchMode(false);
		nameInput.setFocusable(false);
		nameInput.setFocusableInTouchMode(true);
		nameInput.setFocusable(true);
		nameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {

				if (v.getId() == R.id.newApartmentNameEdit && !hasFocus) {

					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				}

			}
		});

		AdapterView.OnItemSelectedListener spinnerSelectListener = new OnItemSelectedListener() {
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
		final Spinner divisonDaySpinner = (Spinner) getActivity().findViewById(
				R.id.newApartmentDivisionDaySpinner);
		@SuppressWarnings("rawtypes")
		ArrayAdapter divisonDayAdapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.days_of_the_week_array,
				android.R.layout.simple_spinner_item);
		divisonDayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		divisonDaySpinner.setAdapter(divisonDayAdapter);
		divisonDaySpinner.setOnItemSelectedListener(spinnerSelectListener);

		// Populate divison period spinner
		final Spinner divisonPeriodSpinner = (Spinner) getActivity()
				.findViewById(R.id.newApartmentDivisonPeriodSpinner);
		@SuppressWarnings("rawtypes")
		ArrayAdapter divisonPeriodAdapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.chores_divison_periods,
				android.R.layout.simple_spinner_item);
		divisonPeriodAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		divisonPeriodSpinner.setAdapter(divisonPeriodAdapter);
		divisonPeriodSpinner.setOnItemSelectedListener(spinnerSelectListener);

		// Complete apartment creation
		Button finishBtn = (Button) (getActivity()
				.findViewById(R.id.newApartmentFinishButton));

		finishBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "clicked create apartment",
						Toast.LENGTH_SHORT).show();
				System.out.println("clicked create apartment");
				Log.d("NewApartmentDialogFragment", "clicked create apartment");
				String apartmentName = nameInput.getText().toString();
				Log.d("NewApartmentDialogFragment", "apartmentName:"
						+ apartmentName + ";");
				boolean err = false;
				if (apartmentName.trim().equals("")) {
					err = true;
					Toast.makeText(
							getActivity(),
							getActivity()
									.getResources()
									.getString(
											R.string.apartment_creation_no_apartment_name),
							Toast.LENGTH_SHORT).show();
					return;
				}
				RoommatesApartment apartment = new RoommatesApartment();
				String divisonDay = divisonDaySpinner.getSelectedItem()
						.toString();
				String divisonPeriod = divisonPeriodSpinner.getSelectedItem()
						.toString();
				apartment.setName(apartmentName);
				apartment.setDivisionDay(divisonDay);
				apartment.setDivisionFrequency(divisonPeriod);
				try {
					String apartmentId = ApartmentDAL
							.createApartment(apartment);
				} catch (ApartmentAlreadyExistsException e) {
					Toast.makeText(
							getActivity(),
							getActivity()
									.getResources()
									.getString(
											R.string.apartment_creation_apartment_name_exists),
							Toast.LENGTH_LONG).show();
					Log.d("NewApartmentDialogFragment", "Caught exception" + e);
					return;

				} catch (UserNotLoggedInException e) {
					Log.d("NewApartmentDialogFragment", "Caught exception" + e);
					LoginActivity.OpenLoginScreen(getActivity(), false);
				} catch (ParseException e) {
					if (e.getCode() == ParseException.CONNECTION_FAILED) {
						Toast.makeText(
								getActivity(),
								getActivity().getResources().getString(
										R.string.setting_saved_toast_text),
								Toast.LENGTH_SHORT).show();
						return;
					}
				}

				// Return results
				if (!err) {
					Log.d(getClass().toString(), "Apartment created: "
							+ apartment.toString());
					getActivity().finish();
				}
			}
		});
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_apartment_dialog,
				container, false);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

}
