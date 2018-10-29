package com.yamba;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusFragment extends Fragment implements OnClickListener {
    private static final String TAG = "StatusFragment";
    private EditText editStatus;
    private Button buttonTweet;
    private TextView textCount;
    private int defaultTextColor;
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_status, container, false);
        editStatus = (EditText) view.findViewById(R.id.editStatus);
        buttonTweet = (Button) view.findViewById(R.id.buttonTweet);
        textCount = (TextView) view.findViewById(R.id.textCount);
        buttonTweet.setOnClickListener(this);
        defaultTextColor = textCount.getTextColors().getDefaultColor();
        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                int count = 140 - editStatus.length();
                textCount.setText(Integer.toString(count));
                textCount.setTextColor(Color.GREEN);
                if (count < 10)
                    textCount.setTextColor(Color.RED);
                else
                    textCount.setTextColor(defaultTextColor);
            }

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start, int before,
                                      int count) {
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        String status = editStatus.getText().toString();
        Log.d(TAG, "onClicked with status: " + status);
        new PostTask().execute(status);
    }

    private final class PostTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            /*YambaClient yambaCloud = new YambaClient("student", "password", "http://yamba.newcircle.com/api");
            try {
                yambaCloud.postStatus(params[0]);
                return "Successfully posted";
            } catch (YambaClientException e) {
                e.printStackTrace();
                return "Failed to post to yamba service";
            }*/
            //9.5.1. Each application has its own shared preferences available to all components of the application context.
            //To get the instance of this SharedPreferences, we use PreferenceManager.getDefaultSharedPreferences()
            //and pass it this as the current context for this app. The name 'shared' could be confusing.
            //To clarify, it means that this preference object contains data by various parts of this application only;
            //it is not shred with any other application
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            //9.5.2. Get the username and password from the shared preference object
            //The first parameter in getString() is the key we assigned to each preference item,
            //such as username and password. The second parameter is the default value in case such application is not found.
            //Keep in mind that the first time a user runs your application, the preference file doesn't exist,
            //so defaults will be used. So if the user hasn't set up her preferences in SettingActivity,
            // this code will attempt to log in with an empty username and password, and thus fail.
            //However, the failure will happen when the user tries to do actual status update because
            //that's how yambaclient library is designed
            String username = prefs.getString("username", "");
            String password = prefs.getString("password", "");

            //Check if the username and password are not empty
            //If empty, Toast a message to set login and bounce to SettingActivity
            //Hint: TextUtils
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {

                //9.5.3. This is just a quick checj that we actually have some legit values already configured in settings.
                //If we don;t, we'll communicate that to the user so she can go ahead and update her username and password first
                getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
                return "Please update your username and password";
            }
            YambaClient yambaCloud = new YambaClient(username, password, "http://yamba.newcircle.com/api");

            //9.5.4. Log in to Yamba service user-defined preferences
            try {
                yambaCloud.postStatus(params[0]);
                return "Successfully posted";
            } catch (YambaClientException e) {
                e.printStackTrace();
                return "Failed to post to yamba service";
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(StatusFragment.this.getActivity(),
                    result, Toast.LENGTH_LONG).show();
        }
    }
}