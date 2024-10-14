package ui.registros;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import model.Usuario;
import request.ApiClient;
import ui.login.MainActivity;

public class RegistrosActivityViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Usuario> mUsuario;
    private MutableLiveData<Uri> mUri;
    private String imagenPath;


    public RegistrosActivityViewModel(@NonNull Application application) {
        super(application);
        context =application.getApplicationContext();
    }
    public LiveData<Usuario> getUsuario()
    {
        if(mUsuario == null)
        {
            mUsuario = new MutableLiveData<>();
        }
        return mUsuario;
    }
    public LiveData<Uri> getMuri()
    {
        if(mUri == null)
        {
            mUri = new MutableLiveData<>();
        }
        return mUri;
    }

    public void guardar(String nombre, String apellido, String dni, String mail, String pass){

        Usuario u = new Usuario(dni, nombre, apellido, mail, pass,imagenPath);
        u.setImagen(imagenPath);
        if(ApiClient.Guardar(context, u)) {

            Toast.makeText(context, "Se guardaron los datos", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }else
        {
            Toast.makeText(context, "No se guardaron los datos", Toast.LENGTH_SHORT).show();

        }
    }
    public void recuperarUsuario()
    {
        Usuario us = ApiClient.leer(context);
        if(us != null)
        {
            mUsuario.setValue(us);
        }
        else{
            Toast.makeText(getApplication(), "No hay datos", Toast.LENGTH_LONG).show();
        }
    }
    public void recibirFoto(@NonNull ActivityResult result) {
        if(result.getResultCode() == RESULT_OK){
            Intent data = result.getData();
            Uri uri=ApiClient.redimensionarYGuardarImagen(context, data.getData());
            imagenPath = uri.toString();
            mUri.setValue(uri);
        }
    }


}
