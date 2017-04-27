package com.rentit.priyath.rentitlayout;


        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Button ;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONException;
        import org.json.JSONObject;


public class SignUp extends AppCompatActivity {




    EditText firstName ;
    EditText etEmail;
    EditText password ;
    EditText cpassword ;
    EditText phoneNumber ;
    String gender ;
    EditText etAge ;
    RadioGroup radioGroup;
    String passError ;
    Button signupButton ;
    TextView passwordError ;
    String firstname ;
    String email ;
    String pass ;
    String cpass ;
    Integer age ;
    String phone ;
    JSONObject params ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);


        firstName = (EditText) findViewById(R.id.etfirstName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        //passwordError = (TextView) findViewById(R.id.passwordError);
        //passwordError.setVisibility(View.GONE);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        password = (EditText) findViewById(R.id.etPassword);

        cpassword = (EditText) findViewById(R.id.etcpassword);


        signupButton = (Button) findViewById(R.id.signupButton);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = firstName.getText().toString();
                email = etEmail.getText().toString();
                pass = password.getText().toString();
                cpass = cpassword.getText().toString();
                if (radioGroup.getCheckedRadioButtonId() == R.id.maleButton) {
                    gender = "Male";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.femaleButton) {
                    gender = "Female";
                }
                phone = phoneNumber.getText().toString();

                params = new JSONObject();

                if (checkData()) {
                    if(checkpassMatch()) {
                        try {
                            params.put("name", firstname);
                            params.put("emailId", email);
                            params.put("password", pass);
                            params.put("phoneNumber", phone);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MyAsyncTask task = new MyAsyncTask();
                        task.execute(0);
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "ENTER ALL DETAILS", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    Boolean checkData(){
        if(firstname.length() == 0 )
            return false;
        if(email.length() == 0 )
            return false;
        if(phoneNumber.length() == 0)
            return  false;
        if(pass.length() == 0)
            return  false;
        if(cpass.length() == 0)
        {   return false;
        }
        return true;
    }

    Boolean checkpassMatch(){
        if((pass.equals(cpass)))
        {
           return true;
        }
        else {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public class MyAsyncTask extends AsyncTask<Integer, Void , String >{

        @Override
        protected String doInBackground(Integer... integers) {
            HttpPost httpPost = new HttpPost() ;
            String response = httpPost.postData(params , "http://192.168.43.87:5000/new_user");
            return response;
        }

        protected void onPostExecute(String result){
            if(result.equals("Error")){
                Toast.makeText(getApplicationContext(),"Email already exists", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }


        }
    }




}




