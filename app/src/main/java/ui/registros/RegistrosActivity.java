package ui.registros;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fotodeperfil.databinding.ActivityRegistrosBinding;

import model.Usuario;

public class RegistrosActivity extends AppCompatActivity {
    private ActivityRegistrosBinding binding;
    private RegistrosActivityViewModel vm;
    private Intent intent;
    private ActivityResultLauncher<Intent> arl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RegistrosActivityViewModel.class);
        abrirGaleria();
        vm.getUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.etNombre.setText(usuario.getNombre());
                binding.etApellido.setText(usuario.getApellido());
                binding.etDni.setText(usuario.getDni());
                binding.etMail.setText(usuario.getMail());
                binding.etPass.setText(usuario.getPass());
                binding.IVfoto.setImageURI(Uri.parse(usuario.getImagen().toString()));

            }
        });

        if(getIntent().getFlags() == Intent.FLAG_ACTIVITY_NEW_TASK)
        {
            vm.recuperarUsuario();
        }

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = binding.etNombre.getText().toString();
                String apellido = binding.etApellido.getText().toString();
                String dni = binding.etDni.getText().toString();
                String mail = binding.etMail.getText().toString();
                String pass = binding.etPass.getText().toString();

                vm.guardar(nombre, apellido, dni, mail, pass);

            }
        });

        binding.btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch(intent);
            }
        });
        vm.getMuri().observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.IVfoto.setImageURI(uri);
            }
        });
    }
    private void abrirGaleria(){
       intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                vm.recibirFoto(result);
            }
        });

    }
}