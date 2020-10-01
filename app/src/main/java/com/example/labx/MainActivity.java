package com.example.labx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mQueue = null;
    private String token = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
    }

    //IrMenuPrincipal desplaza la vista del teléfono entre actividades
    public void irMenuPrincipal(View v){
        final EditText usuario = (EditText) findViewById(R.id.tx_usuario);
        final EditText password = (EditText) findViewById(R.id.tx_pass);
        String str_usuario = usuario.getText().toString();
        String str_password = password.getText().toString();

        iniciarSesion(str_usuario,str_password);
    }


    private void iniciarSesion(String usuario, String password) {
        Map<String, String> params = new HashMap();
        params.put("username", usuario);
        params.put("password", password);
        JSONObject parametros = new JSONObject(params);

        String login_url = "https://amst-labx.herokuapp.com/db/nuevo-jwt";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, login_url, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            token = response.getString("token");
                            Intent menuPrincipal = new Intent(getBaseContext(), Menu.class);
                            menuPrincipal.putExtra("token", token);
                            startActivity(menuPrincipal);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Credenciales Incorrectas");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        mQueue.add(request);
    }
}