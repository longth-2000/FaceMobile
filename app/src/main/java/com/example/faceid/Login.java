package com.example.faceid;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.faceid.databinding.LoginBinding;
import com.example.faceid.databinding.LoginBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Login extends Fragment {

    private LoginBinding binding;
    String currentPhotoPATH;// link image when login
    Database database;
    EditText username;
    EditText password;
    SharedPreferences sharedpreferences;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = LoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        sharedpreferences = activity.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE);
        getData();
        binding.faceUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              checkLogin("add_face");
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               NavHostFragment.findNavController(Login.this)
                        .navigate(R.id.action_to_Register);

            }
        });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin("normal_login");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void getData() {
        String userName  = sharedpreferences.getString("username_key", "");
        String passWord  = sharedpreferences.getString("password_key", "");
        username = (EditText) getView().findViewById(R.id.username_login);
        username.setText(userName);
        password = (EditText) getView().findViewById(R.id.password_login);
        password.setText(passWord);
    }
    public void checkLogin(String function) {
        String usernameData = username.getText().toString().trim();
        String passwordData = password.getText().toString().trim();
        Activity activity = getActivity();
        database = new Database(activity,"User.sqlite", null, 1);
        Cursor users = database.getInfor("SELECT * FROM users");
        if(TextUtils.isEmpty(usernameData)) {
            Toast.makeText(activity, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if(TextUtils.isEmpty(passwordData)) {
                Toast.makeText(activity, "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                boolean check = false;
                while(users.moveToNext()) {
                    String username = users.getString(1);
                    String password = users.getString(2);
                    if(usernameData.equals(username) && passwordData.equals(password)) check = true;
                }
                if(check) {
                    saveData(usernameData, passwordData);
                    if(function.equals("add_face")) {
                        openCamera(activity, usernameData);
                    }
                    else {
                       NavHostFragment.findNavController(Login.this)
                                .navigate(R.id.action_to_Successfull);
                    }

                }
                else Toast.makeText(activity, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void openCamera(Activity activity, String username){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File file = create_imageFIile(activity, username);
            if (file != null){
                Uri photoURI = FileProvider.getUriForFile(
                        activity,
                        "com.example.faceid.fileprovider",
                        file
                );
                intent.putExtra(MediaStore.EXTRA_OUTPUT , photoURI );
                startActivityForResult(intent , 999);
            }
        }catch (ActivityNotFoundException e) {
            Toast.makeText(activity , "No camera",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public File create_imageFIile(Activity activity, String username) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String file_Name = username ;
        File storage = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                file_Name,
                ".jpg",
                storage
        );
        currentPhotoPATH = image.getAbsolutePath();
        return image;
    }
    public String getImagePath(String username, Database database) {
        Cursor users = database.getInfor("Select * from users where username = '" + username + "' ");
        String imagePath = "";
        if(users.moveToLast()) {
            imagePath = users.getString(3);
        }
        return  imagePath;
    }
    public void deleteImage(String image_path) {
       File image = new File(image_path);
       if(image.exists()) {
           image.delete();
       }
    }
    public void saveData(String username, String password) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("username_key", username);
        editor.putString("password_key", password);
        editor.commit();
    }
    public void draftLogin(){
        System.out.print("Create draft login --linux")
    }
}

//link ảnh khi đăng nhập ở biến currentPhotoPATH