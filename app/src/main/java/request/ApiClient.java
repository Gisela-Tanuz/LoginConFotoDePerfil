package request;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.util.Log;
import android.widget.Toast;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.Usuario;

public class ApiClient {
    private static File file;

    public static File conectar(Context context){
        if(file == null){
            file = new File(context.getFilesDir(), "Usuario.dat");
        }
        return file;
    }
    public static Usuario login(Context context, String mail, String pass){
        Usuario u = leer(context);

        if(u != null){
            if(u.getMail().equals(mail) && u.getPass().equals(pass)) {
                return u;
            }
        }
        return null;

    }

    public static Usuario leer(Context context) {

        Usuario usuario = null;
         File file = conectar(context);

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            usuario = (Usuario) ois.readObject();
            usuario.setImagen(ois.readUTF());
            ois.close();
            bis.close();
            fis.close();


        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error al encontrar el archivo", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error entrada/salida", Toast.LENGTH_SHORT).show();

        } catch (ClassNotFoundException e) {
            Toast.makeText(context, "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
        }
        return usuario;
    }

    public static boolean Guardar(Context context, Usuario usuario){
       File  file = conectar(context);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(usuario);
            oos.writeUTF(usuario.getImagen());
            bos.flush();
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error de archivo", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IOException e) {
            Toast.makeText(context, "Error de entrada/salida", Toast.LENGTH_SHORT).show();
            Log.d("salida",usuario.getImagen());
            return false;
        }
    }
    public static Bitmap redimensionarBitmap(Context context, Uri imageUri) {
        Bitmap resizedBitmap= null;
        try(InputStream inputStream = context.getContentResolver().openInputStream(imageUri)) {

            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);
            resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 400, 400, true);
        }
        catch (IOException ex){
            Toast.makeText(context, "Error de FileNotFoundExeption", Toast.LENGTH_SHORT).show();
        }

        return resizedBitmap;
    }

    public static Uri guardarBitmapYObtenerUri(Context context, Bitmap bitmap)  {

      File directory = new File(context.getFilesDir(), "imagenes");
        if (!directory.exists()) {
            directory.mkdirs();
        }


         File  file = new File(directory, "fotoDePerfil.jpg");
        try(FileOutputStream outputStream = new FileOutputStream(file)){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }
        catch (IOException ex){
            Toast.makeText(context, "Error de IOException", Toast.LENGTH_SHORT).show();
        }

        return Uri.fromFile(file);
    }

    public static Uri redimensionarYGuardarImagen(Context context, Uri imageUri){

        Bitmap resizedBitmap = redimensionarBitmap(context, imageUri);
        return guardarBitmapYObtenerUri(context, resizedBitmap);
    }
}
