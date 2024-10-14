package ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import model.Usuario;
import request.ApiClient;
import ui.registros.RegistrosActivity;

public class MainActivityViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Integer> ok;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        context =application.getApplicationContext();
    }
    public LiveData<Integer> getOk()
    {
        if(ok == null)
        {
            ok = new MutableLiveData<>();
        }
        return ok;
    }
    public void login(String mail, String pass)
    {
        Usuario usuario = ApiClient.login(context, mail, pass);
        if( usuario != null)
        {
            ok.setValue(View.INVISIBLE);
            Intent i = new Intent(context, RegistrosActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("registro", false);
            ApiClient.leer(context);
            context.startActivity(i);
        }else {

            ok.setValue(View.VISIBLE);
        }

    }

}
