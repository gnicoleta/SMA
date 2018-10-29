package com.yamba;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

//1. implements onClickListener to make OldStatusActivity capable of being a button listener
public class OldStatusActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "OldStatusActivity";
    private EditText editStatus;
    private Button buttonTweet;
    private TextView textCount; //3.1. textCount is our text view
    private int defaultTextColor; //3.2. this variable will hold the default text color. Will be calculated at runtime


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_status);

        editStatus = findViewById(R.id.editStatus);
        buttonTweet = findViewById(R.id.buttonTweet);

        //2. Register the button to nitify this (OldStatusActivity) when it gets clicked
        buttonTweet.setOnClickListener(this);

        //3.3. First, we need to find the textCount in the inflated layout
        textCount = findViewById(R.id.textCount);

        //3.4. This is where we determine the default color of the text
        defaultTextColor = textCount.getTextColors().getDefaultColor();

        //3.5. We attach the TextWatcher listener to the status text area. Unlike in onClick Listener,
        // we chose to implement the TextWatcher as an anonymous inner class.
        // This is a very standard practice in Android, especially for UI event handlers
        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            //3.6. TextWatcher has a nr. of callbacks, but we use only afterTextChanged().
            // This method is called once the user makes a change in teh actual text field.
            // Here, we set the initial text to 140 because that's the max length of a status msg in our app
            // Note that TextView takes text as a value, so we convert a number to text here
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //3.9. This method is called before the actual text replacement is completed therefore
                //we don't need this method, but as part of the TextWatcher interface,
                // we need to provide an implementation even tho is empty
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //3.10. same as 3.9
            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = 140 - editStatus.length(); //3.7.here we do some math to see how many characters we have left
                textCount.setText(Integer.toString(count));
                textCount.setTextColor(Color.GREEN); //3.8. teh textCount field will change color dynamically based on the no. of remaining characters
                if (count < 10) {
                    textCount.setTextColor(Color.RED);
                } else {
                    textCount.setTextColor(defaultTextColor);
                }
            }
        });
    }

    @Override
    //3. this method is called when the button is clicked, as part of the OnCickListener interface
    public void onClick(View view) {
        //4. we loock up the value of the actual text of the status in the UI
        String status = editStatus.getText().toString();

        //5. Use the log system to print out the value so we know this is working
        Log.d(TAG, "onClicked with status: " + status);

        /*2.1. Once we have our AsyncTask set up, we can run it. To do so, we simply instantiate it
        and call execute() on it. The argument that we pass in is what goes into the doInBackground()
        call. Note that in this case we are passing a single string that is being converted into a string array
        int the actual method later on, which is an example of Java's variable number of arguments feature.
        It is important to remember that this method executes on a separate thread, so you can't update the UI from it
        */
        new PostTask().execute(status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds to the action bar if it's present
        //getMenuInflater().inflate(R.menu.status, menu);
        return true;
    }

    //2.2. The PostTask class in this class is an inner class of ActivityStatus. It also subclasses AsyncTask.
    // Notice the use of Java generics to describe the data types that this AsyncTask will use in its methods.
    // We'll explain these 3 types following the list. The first data type is used by doInBackgroung(),
    // the second by onRpogressUpdate(), and the 3rd by onPostExecute()
    private final class PostTask extends AsyncTask<String, Void, String> {

        @Override
        //2.3. doInBackground() is the callback that specifies the actual work to be done on the separate thread,
        // acting as if it's executing in the bg. The argument String... is the first of the three data types
        // that we defined in the list of generic for this PostTask inner class. The 3 dots indicate that this is an array of Strings,
        // and you have to declare it that way, even tho you want to pass only a single status
        protected String doInBackground(String... params) {
            YambaClient yambaCloud = new YambaClient("student", "password", "http://yamba.newcircle.com/api");
            //http://yamba.newcircle.com/

            try {
                //2.4. This is the call to the Yamba Client library that does all the magic of the encoding
                // it into a web service call. we're passing the first parameter of the input,
                // which is the actual text status that comes via execute() call from onClick()
                yambaCloud.postStatus(params[0]);
                return "Successfully posted";
            } catch (YambaClientException e) {
                e.printStackTrace();
                return "Failed to post to yamba service";
            }
        }

        @Override
        //2.5. onPostExecute() is called when our task completes. this is our callback method
        // to update the user interface and tell the user that the task is done.
        // It is important to know that this method executes on the application's main thread,
        // which we also refer to as the UI thread
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //2.6. In this particular case, we are using a Toast feature of the Android UI
            // to display a quick message on the screen. Notice that Toast uses the makeText() static method
            // to make the actual message. Also, do not forget to include show();
            // otherwise, your message will never be displayed, and no error will be reported - a hard bug to find.
            // The argument that this method gets is the value that doInBackground() returns, in this case a string.
            // This also corresponds to the 3rd generics data type in the PostTask class definition.
            // the reference to statusActivity.this represents the Context that our application is in.
            // As a rule of thumb, whenever in an activity, pass that activity as the context object.
            Toast.makeText(OldStatusActivity.this, result, Toast.LENGTH_LONG).show();
        }

//        @Override
//        public boolean onCreateMenu(Menu menu) {
//            //Inflate the menu; this adds items to the action bar if it's present
//            //getMenuInflater().inflate(R.menu.status, menu);
//            return true;
//        }
    }
}
