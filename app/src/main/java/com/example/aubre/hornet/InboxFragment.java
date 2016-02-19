package com.example.aubre.hornet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aubre on 1/22/2016.
 */
public class InboxFragment extends android.support.v4.app.ListFragment {

    protected List<ParseObject> mMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        return rootView;
    }
      //getting a query of users based on relation
    @Override
    public void onResume() {
        super.onResume();

        ParseQuery<ParseObject> query = new <Object>ParseQuery<ParseObject>(ParseConstant.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstant.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstant.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {

                if (e == null) {
                    //Found the message
                    mMessages = messages;
                    //grabbing the end users message
                    String[] usernames = new String[mMessages.size()];
                    int i = 0;
                    for (ParseObject message : mMessages){
                        usernames[i] = message.getString(ParseConstant.KEY_SENDER_NAME);
                        i++;
                    }
                    //connecting the custom adapter to the listveiw in this fragment
                    if (getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(
                                getListView().getContext(),
                                mMessages);
                        setListAdapter(adapter);
                    }
                    else {
                        // refill the adapter!
                        ((MessageAdapter)getListView().getAdapter()).refill(mMessages);
                    }

                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //knowing what type of message it is
        ParseObject message = mMessages.get(position);
        String messageType = message.getString(ParseConstant.KEY_FILE_TYPE);
        ParseFile file = message.getParseFile(ParseConstant.KEY_FILE);
        Uri fileUri = Uri.parse(file.getUrl());

        if (messageType.equals(ParseConstant.TYPE_IMAGE)) {
            // view the image
            //passing it through to the image view
            Intent intent = new Intent(getActivity(), View_Image_Acttivity.class);
            intent.setData(fileUri);
            startActivity(intent);
        }
        else {
            // view the video
            return;
           /* Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.setDataAndType(fileUri, "video/*");
            startActivity(intent);*/
        }

        // Delete it!
        List<String> ids = message.getList(ParseConstant.KEY_RECIPIENT_IDS);

        if (ids.size() == 1) {
            // last recipient - delete the whole thing!
            message.deleteInBackground();
        }
        else {
            // remove the recipient and save
            ids.remove(ParseUser.getCurrentUser().getObjectId());

            ArrayList<String> idsToRemove = new ArrayList<String>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());

            message.removeAll(ParseConstant.KEY_RECIPIENT_IDS, idsToRemove);
            message.saveInBackground();
        }
    }
}
