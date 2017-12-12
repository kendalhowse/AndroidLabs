package com.example.kendalsasus.androidlabs;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.test.SingleLaunchActivityTestCase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by Kendal's Asus on 2017-12-03.
 */

public class MessageFragment extends android.app.Fragment {

    long id;
    String message;
    Button delete;
    ChatWindow chatWindow;

    public MessageFragment(){

    }

    public MessageFragment(ChatWindow chatWindow){
        this.chatWindow = chatWindow;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle passedInfo = getArguments();

       id = 0;
       message = "";
        if(passedInfo != null) {
            id = passedInfo.getLong("ID");
            message = passedInfo.getString("MESSAGE");

        }
        Log.i("Passed key", ""+id);
        Log.i("Passed message", message);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.layout_messages, container, false);


        TextView idText = v.findViewById(R.id.idHere);
        idText.setText(Long.toString(id));

        delete = v.findViewById(R.id.deleteButton);

        final TextView messageText = v.findViewById(R.id.messageHere);
        messageText.setText(message);

        if (getResources().getBoolean(R.bool.isTablet) == false){
            delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view){
                    Intent resultIntent = new Intent(view.getContext(), ChatWindow.class);
                    resultIntent.putExtra("deleteThis", id);
                    resultIntent.putExtra("Message", message);
                    getActivity().setResult(10, resultIntent);
                    getActivity().finish();
                    //startActivity(resultIntent);
                }
            });
        }

        else {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatWindow.deleteID(id, message, chatWindow.getDb(), chatWindow.getMessageAdapter(), chatWindow.listView);
                    chatWindow.getMessageAdapter().remove(message);

                    getActivity().getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();

                }
            });

        }



        return v;
    }

    public Button getDelete(){
        return delete;
    }


}

