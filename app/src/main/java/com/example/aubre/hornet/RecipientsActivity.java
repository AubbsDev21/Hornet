package com.example.aubre.hornet;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class RecipientsActivity extends ListActivity {
    public static final String TAG = RecipientsActivity.class.getSimpleName();
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected List<ParseUser> mFriends;
    protected Button Send;
    protected Uri mMediaUri;
    protected String mFileType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);

        //Choose multiple people
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mMediaUri = getIntent().getData();
        mFileType = getIntent().getExtras().getString(ParseConstant.KEY_FILE_TYPE);
    }

    @Override
    protected void onResume() {
        //grabbing a list of users from query  in the app with username
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstant.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstant.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    //success
                    //getting the list of friends using the app
                    mFriends = friends;
                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for (ParseUser user : mFriends) {
                        usernames[i] = user.getUsername();
                        i++;
                        //Allowing the user to check the friends
                        ArrayAdapter<String> adapter = new <Object>ArrayAdapter<String>(
                                RecipientsActivity.this,
                                android.R.layout.simple_list_item_checked,
                                usernames);
                        setListAdapter(adapter);


                    }
                } else {
                    //if user doesnt have any friends
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
                    builder.setMessage(R.string.Edit_friend_error)
                            .setTitle(R.string.Edit_friend_title)
                            .setPositiveButton(R.string.ok_label, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

            }
        });
        final Button Sendbutton = (Button)findViewById(R.id.send_button);
        /*-----------Send Button Action-------------*/
        Sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject message = createMessage();
                if (message == null) {

                    // Toast.makeText(RecipientsActivity.this, getString(R.string.error_selecting_file), Toast.LENGTH_LONG).show();
                    //error
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
                    builder.setMessage(R.string.error_selecting_file)
                            .setTitle(R.string.error_selecting_file_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    //sending the message using the send method below
                    send(message);
                    finish();
                }

            }


        });


    }



  /*  @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //if the a item is selected on Listview then send button will appear if not the will be invisble
        if (l.getCheckedItemCount() > 0) {
            mSendMenuItem.setVisible(true);
        } else {
            mSendMenuItem.setVisible(false);
        }
    }*/

    private ParseObject createMessage() {
        ParseObject message = new ParseObject(ParseConstant.CLASS_MESSAGES);
        message.put(ParseConstant.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstant.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstant.KEY_RECIPIENT_IDS, getRecipientIds());
        message.put(ParseConstant.KEY_FILE_TYPE, mFileType);

       byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);

        if (fileBytes == null){
            return null;
        }else{
                 //this resizes our image to keep it from going over
            if (mFileType.equals(ParseConstant.TYPE_IMAGE)){
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            }
                //setting the filename, file, and filetype
            String filename = FileHelper.getFileName(this, mMediaUri, mFileType);
            ParseFile file = new ParseFile(filename, fileBytes);
            message.put(ParseConstant.KEY_FILE, file);
        }
        return message;
    }

   protected ArrayList<String> getRecipientIds(){
       ArrayList<String> recipentIds = new <Object>ArrayList<String>();
       for(int i = 0;  i < getListView().getCount(); i++){
           if (getListView().isItemChecked(i)){
               recipentIds.add(mFriends.get(i).getObjectId());
           }
       }
       return recipentIds;
   }
    protected void send(ParseObject message) {
        //saving the message to the background
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //success
                    Toast.makeText(RecipientsActivity.this, getString(R.string.success_message), Toast.LENGTH_LONG).show();

                } else{
                    //error
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
                    builder.setTitle("Message Error")
                            .setMessage(R.string.error_fail_message)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

}
