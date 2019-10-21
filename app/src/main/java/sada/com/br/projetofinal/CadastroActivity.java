package sada.com.br.projetofinal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CadastroActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    static String TAG = LoginActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        EditText edtNome = findViewById(R.id.edtNome);
        edtNome.setText("JOSEMAR SABINO");
        EditText edtEmail = findViewById(R.id.edtEmail);
        edtEmail.setText("josemarsabino@hotmail.com");
        EditText edtSenha = findViewById(R.id.edtSenha);
        edtSenha.setText("123456");

        final String nome = edtNome.getText().toString();
        final String email = edtEmail.getText().toString();
        final String senha = edtSenha.getText().toString();

        Button btnCriarConta = findViewById(R.id.btnCriarConta);
        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePostCall();

            }
        });

    }

    private void makePostCall() {

        String url = "http://ec2-34-230-46-185.compute-1.amazonaws.com:8080/v1/users/";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject postRequest = new JSONObject();

        EditText nome = findViewById(R.id.edtNome);
        EditText email = findViewById(R.id.edtEmail);
        EditText senha = findViewById(R.id.edtSenha);
        //Toast.makeText(getBaseContext(),nome.getText() + " " + email.getText() + " " + senha.getText(),Toast.LENGTH_LONG).show();

        try {

            postRequest.put("userName", nome.getText());
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
        progressDialog = new ProgressDialog(CadastroActivity.this);
        progressDialog.setMessage("Por favor, aguarde");
        progressDialog.show();
    }

    private void showDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(CadastroActivity.this).create();
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

