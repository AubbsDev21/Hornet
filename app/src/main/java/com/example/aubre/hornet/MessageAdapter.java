package com.example.aubre.hornet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Aubre on 2/18/2016.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mMessages;

    public MessageAdapter(Context context, List<ParseObject> messages ) {
        super(context, R.layout.message_item, messages);
        mContext = context;
        mMessages = messages;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
             //this creates the view if hasnt been created
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView)convertView.findViewById(R.id.messageIcon);
            holder.nameLabel = (TextView)convertView.findViewById(R.id.senderLabel);
            convertView.setTag(holder);
        } //if view already has been created then recyle the veiw and change the data
        else {
            holder = (ViewHolder)convertView.getTag();
        }
          //setting the data for the list view with the picture icon and name
        ParseObject message = mMessages.get(position);

        if (message.getString(ParseConstant.KEY_FILE_TYPE).equals(ParseConstant.TYPE_IMAGE)) {
            holder.iconImageView.setImageResource(R.drawable.ic_action_picture);
        }
        else {
           return null;
           // holder.iconImageView.setImageResource(R.drawable.ic_action_play_over_video);
        }
        holder.nameLabel.setText(message.getString(ParseConstant.KEY_SENDER_NAME));

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView nameLabel;
    }

    public void refill(List<ParseObject> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

}
