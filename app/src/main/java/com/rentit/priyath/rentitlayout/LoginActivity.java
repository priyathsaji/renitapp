package com.rentit.priyath.rentitlayout;

        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.util.ArrayList;

        import static android.R.attr.data;


public class LoginActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button loginButton;
    TextView tx1;
    String userName;
    String password;
    TextView tvRegister;
    Button signup;
    JSONObject params ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.loginButton);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        signup = (Button)findViewById(R.id.signup);



        // final String userName = etUsername.getText().toString();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = etUsername.getText().toString();
                password = etPassword.getText().toString();


                if(!checkfilled()) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Details", Toast.LENGTH_LONG).show();
                }
                else
                {
                    params = new JSONObject();
                    try {
                        params.put("emailId", userName);
                        params.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MyAsyncTask task = new MyAsyncTask();
                    task.execute(0);
                }
            }



        });
    }

    boolean checkfilled(){
        if(userName.length() == 0)
            return false;
        if(password.length() == 0 )
            return false;
        for(int i =0 ; i<userName.length(); i++)
        {
            if(userName.charAt(i) == '@')
                return true;
        }
        return false;
    }

    public class MyAsyncTask extends AsyncTask<Integer , Void , String>{

        @Override
        protected String doInBackground(Integer... integers) {
            HttpPost httpPost = new HttpPost();
            String response = httpPost.postData(params , "http://192.168.43.87:5000/authentication");
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("no")){
                Toast.makeText(getApplicationContext(), "Invalid User Details", Toast.LENGTH_LONG).show();
            }
            else{

                try {
                    JSONObject js = new JSONObject(s);
                    userDetails details = new userDetails();
                    details.username = js.getString("name");
                    details.phonenumber = js.getString("phoneNumber");
                    details.emailId = js.getString("emailId");
                    details.id = js.getString("_id");

                    FileOutputStream out = getApplicationContext().openFileOutput("userdetails",MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    oos.writeObject(details);
                    oos.close();
                    out.close();
                    Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
                    startActivity(intent);
                    Log.i("response", String.valueOf(js));
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}