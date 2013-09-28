package il.ac.huji.chores;

import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.parse.FunctionCallback;
import com.parse.ParseException;

import java.util.ArrayList;

/**
 * User: yoav
 * Email: yoav.luft@gmail.com
 * Date: 06/09/13
 */
public class InviteContactsFragment extends Fragment {

    private int invitedIds = 0;
    private ArrayList<String> invited;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        View view = inflater.inflate(R.layout.invite_contacts_fragment, container, true);
        // Invite Contacts
        // Get contact list for autocomplete
        final AutoCompleteTextView inviteEdit = (AutoCompleteTextView) view.findViewById(R.id.inviteContactsEditText);
        ContentResolver cr = getActivity().getContentResolver();
        Cursor contactsCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        inviteEdit.setAdapter(new ContactsCursorAdapter(getActivity(), contactsCursor));

        final LinearLayout invitedLayout = (LinearLayout) view.findViewById(R.id.inviteContactsInvitedLayout);
        Button inviteButton = (Button) view.findViewById(R.id.inviteContactsInviteButton);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
                String contactName = inviteEdit.getText().toString();

                ContactsCursorAdapter adapter = (ContactsCursorAdapter) inviteEdit.getAdapter();

                Log.d("InviteContactsFragment", "Selection: " + adapter.getPhones(adapter.getCursor()));
                LayoutInflater inflaterRow = LayoutInflater.from(getActivity());
                final View rowView = inflaterRow.inflate(R.layout.invite_contact_row, invitedLayout);
                TextView nameView = (TextView) rowView.findViewById(R.id.contactRowContactNameText);
                String phone;
                if (adapter.getCursor().getPosition() > 0) {
                    phone = adapter.getPhones(adapter.getCursor());
                } else {
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.invit_phone_err), Toast.LENGTH_SHORT).show();
                	 (rowView.findViewById(R.id.contactRowProgress)).setVisibility(View.GONE);
                    return;
                }
          
                invited.add(contactName);
                MessagesToServer.invite(new FunctionCallback() {
                    @Override
                    public void done(Object o, ParseException e) {
                        ImageButton image = (ImageButton) rowView.findViewById(R.id.contactRowImageButton);
                        Log.d("InviteContactsFragment", "Got response from Parse " + e);
                        (rowView.findViewById(R.id.contactRowProgress)).setVisibility(View.INVISIBLE);
                        if (e == null) {
                            image.setImageResource(R.drawable.navigation_accept);
                        } else {
                            image.setImageResource(R.drawable.navigation_cancel);
                        }
                        image.setVisibility(View.VISIBLE);
                        image.refreshDrawableState();
                    }
                }, contactName, phone);
                nameView.setText(contactName);
                nameView.setId(invitedIds++);
                rowView.refreshDrawableState();
                // invitedLayout.addView(rowView);
                inviteEdit.setText("");
            }
        });
        return view;
    }

    public InviteContactsFragment() {
        super();
        invited = new ArrayList<String>();
    }
}
