package sada.com.br.projetofinal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    static String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Seja bem Vindo !!!");


        //Button btnEntrar = findViewById(R.id.btnEntrar);
        //final EditText time = findViewById(R.id.in_time);

        final Button btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                makePostCall();


            }
        });


        Button btnCriarConta = findViewById(R.id.btnCriarConta);
        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);

            }
        });
    }

        private void makePostCall() {

        String url = "http://ec2-34-230-46-185.compute-1.amazonaws.com:8080/v1/users/authenticate";// url do webservice 21/10/2019
            RequestQueue queue = Volley.newRequestQueue(this);

            /**
             JsonObjectRequest espera 5 parâmetros
             Request Type - Tipo da requisição: GET,POST
             URL          - URL da API
             JSONObject   - Objeto JSON da requisição (parameters.null se a requisição for do tipo GET)
             Listener     - Implementação de um Response.Listener() com um callback de sucesso e de erro
             **/

            JSONObject postRequest = new JSONObject();

            EditText email = findViewById(R.id.edtEmail);
            EditText senha = findViewById(R.id.edtSenha);
            //Toast.makeText(getBaseContext(),email + " " + senha,Toast.LENGTH_LONG).show(); desta forma manda um objeto como se fosse sem um toString
            //Toast.makeText(getBaseContext(),"ENVIANDO" + " " + email.getText() + " " + senha.getText(),Toast.LENGTH_LONG).show();

            try {

                postRequest.put("email", email.getText());
                postRequest.put("pass", senha.getText());
                //postRequest.put("appid","4fa74572c6b3268a6ae5bd1150d7a748");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    postRequest,
                    new Response.Listener<JSONObject>() {

                    /* Callback chamado em caso de sucesso */

                        @Override
                        public void onResponse(JSONObject response) {

                            progressDialog.dismiss();
                            Log.d(TAG, "API Response: " + response);
                            String message = response.optString("message");
                            showDialog("Informação", message);

                            String sucesso = response.optString("sucess");
                            //Toast.makeText(getBaseContext(),sucesso,Toast.LENGTH_SHORT).show();

                            if (sucesso.equals("true")) {
                                String nomeUserCadastrado = response.optString("userName");
                                String emailCadastrado = response.optString("email");
                                //Toast.makeText(getBaseContext(),nomeUserCadastrado + "" + emailCadastrado ,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, TelaPrincipalActivity.class);

                                intent.putExtra("nomeCapturado",nomeUserCadastrado);
                                intent.putExtra("emailCapturado",emailCadastrado);

                                startActivity(intent);




                            }

                        }
                    },

                /* Callback chamado em caso de erro */

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e(TAG, "Ocorreu um erro ao chamar a API " + error);
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    //add params <key,value>
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<String, String>();
                    String auth = "Basic MzgxNTc5ZmEtZDI0MC00Mzg3LTkyNTMtZWY2YjgwYTdhMWEwOmM4NDM4M2Y0LTJiMDgtNGJiYy04MjQwLWI0YjQ5YTFlYWQzZQ==";
                    headers.put("Authorization", auth);
                    return headers;
                }
            };

            queue.add(jsonObjReq);
            showProgressDialog();
        }

        private void showProgressDialog() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Por favor, aguarde");
            progressDialog.show();
        }

        private void showDialog(String title, String message) {
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
}