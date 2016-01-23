package com.example.aubre.hornet;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class EditFriends_Activity extends ListActivity {
    //set progress bar
    public static final String TAG = EditFriends_Activity.class.getSimpleName();
    protected List<ParseUser>  mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends_);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseQuery <ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstant.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null){
                    //success
                    mUsers = users;
                    String[] usernames = new  String[mUsers.size()];
                    int i=0;
                    for (ParseUser user : mUsers){
                        usernames[i] = user.getUsername();
                        i++;
                        ArrayAdapter <String> adapter = new ArrayAdapter<String>(EditFriends_Activity.this,
                                android.R.layout.simple_list_item_checked,
                                usernames);
                            setListAdapter(adapter);
                    }
                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder  builder = new AlertDialog.Builder(EditFriends_Activity.this);
                    builder.setMessage(R.string.Edit_friend_error)
                            .setTitle(R.string.Edit_friend_title)
                            .setPositiveButton(R.string.ok_label, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

            }
        });
    }
}
