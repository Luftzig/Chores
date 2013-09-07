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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                invited.add(contactName);
                ContactsCursorAdapter adapter = (ContactsCursorAdapter) inviteEdit.getAdapter();

                Log.d("InviteContactsFragment", "Selection: " + adapter.getPhones(adapter.getCursor()));
                final TextView nameView = new TextView(getActivity());
                String phone = adapter.getPhones(adapter.getCursor());
                MessagesToServer.callFunction("invite", new ASyncListener() {
                    @Override
                    public void onASyncComplete(String result) {
                        Log.d("InviteContactsFragment", "Called from listener!");
                    }
                }, getActivity(), "\"phone\" = \"" + phone + "\"", "\"name\" = \"" + contactName + "\"");
                nameView.setText(contactName);
                nameView.setId(invitedIds++);
                invitedLayout.addView(nameView);
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
