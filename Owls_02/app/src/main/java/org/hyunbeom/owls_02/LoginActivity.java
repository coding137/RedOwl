package org.hyunbeom.owls_02;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Dialog;
import  android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
public class LoginActivity extends AppCompatActivity{

    private boolean loggedIn = false;

    private EditText editTextEmail;
    private EditText editTextPass;

    public static final String EMAIL = "email";
    public static final String PARTENER = "";
    String email;
    String pass;
    JSONArray jsonArray=null;


    private String jsonemail="";
    private String jsonpartner="";
    private String jsonstatus="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPass = (EditText) findViewById(R.id.pass);



        TextView registerButton = (TextView) findViewById(R.id.signupButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
               startActivity(registerIntent);
            }
        });//instant end

        Button loginButton = (Button) findViewById(R.id.loginButton);


    }// onCreate end

    public void invokeLogin(View view){


        email = editTextEmail.getText().toString();
        pass = editTextPass.getText().toString();

        login(email,pass);

    }

    private  void login(final  String email, String  pass){


        class  LoginAsync extends AsyncTask<String, Void, String>{

            Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Loading...");
            }//on PreExecute end in login async


            @Override
            protected String doInBackground(String... params) {
                String email = params[0];
                String pass = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("pass", pass));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            Config.LOGIN_URL+"newLogin.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString().trim();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }//
            @Override
            protected void onPostExecute(String result) {
                SharedPreferences preferences  = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
//                SaveSharedPreference.clearShredPreference(getApplicationContext());
                String s = result;
                getPartner(s);
                Log.i("fault is ","on post");


//                try{
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONArray jsonArray = jsonObject.getJSONArray("response");
//                    Log.i("fault is ","try");
//                    for(int i =0 ; i<jsonArray.length();i++){
//
//
//                        JSONObject c= jsonArray.getJSONObject(i);
//                        jsonemail = c.getString("email");
//                        jsonstatus = c.getString("status");
//                        jsonpartner= c.getString("partner");
//
//                    }
//
//
//
//                }catch (Exception e){
//                    e.getMessage();
//                }




                Log.i("fault is ",jsonstatus);
                SaveSharedPreference.setSharedPreferenceString(getApplicationContext(),"email",jsonemail);
                SaveSharedPreference.setSharedPreferenceString(getApplicationContext(),"partner",jsonpartner);
                SaveSharedPreference.setSharedPreferenceString(getApplicationContext(),"status",jsonstatus);
                loadingDialog.dismiss();
                if(jsonstatus.toString().equals("matched")){
                    Intent mainintent = new Intent(LoginActivity.this, MainActivity.class);
                    SaveSharedPreference.setSharedPreferenceString(getApplicationContext(),"email",email);

                  // mainintent.putExtra(EMAIL, email);
                   // intent.putExtra(PARTENER, email);
                    startActivity(mainintent);
                    finish();

                }else if(jsonstatus.equalsIgnoreCase("unmatched")) {
                    Intent intent = new Intent(LoginActivity.this, Unmatched.class);
                    intent.putExtra(EMAIL, email);
                    SaveSharedPreference.setSharedPreferenceString(getApplicationContext(),"email",email);

//                    editor.putString("email",email);
//                    editor.commit();
                    startActivity(intent);

                    finish();

                }else if(jsonstatus.equalsIgnoreCase("await")){
                    Intent intent = new Intent(LoginActivity.this, Await.class);

                    startActivity(intent);

                    finish();

                }else if(jsonstatus.equalsIgnoreCase("invited")){
                    Intent intent = new Intent(LoginActivity.this, Invited.class);
                    intent.putExtra(EMAIL, email);
                    editor.putString("email",email);
                    editor.commit();
                    startActivity(intent);

                    finish();
                }else {
                    SaveSharedPreference.clearShredPreference(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }//end

        }// Login Async innerclass end

        LoginAsync la = new LoginAsync();
        la.execute(email, pass);
    }//login end
 protected  void getPartner(String myjson){
        try{
            JSONObject jsonObject = new JSONObject(myjson);
            jsonArray = jsonObject.getJSONArray("response");

            for(int i =0 ; i<jsonArray.length();i++){
                JSONObject c= jsonArray.getJSONObject(i);
                jsonemail = c.getString("email");
                jsonpartner= c.getString("partner");
                jsonstatus = c.getString("status");
            }



        }catch (Exception e){
            e.getMessage();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //    getMenuInflater().inflate(R.);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.signupButton) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
