package com.example.faceid;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.faceid.databinding.LoginBinding;
import com.example.faceid.databinding.LoginBinding;

import java.io.File;
import java.io.IOException;

public class Login extends Fragment {

    private LoginBinding binding;
    Database database;
    EditText username;
    EditText password;
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
        getData();
        binding.faceUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openCamera();
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
        username = (EditText) getView().findViewById(R.id.username_login);
        password = (EditText) getView().findViewById(R.id.password_login);
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
                    if(function.equals("add_face")) {
                        openCamera();
                    }
                    NavHostFragment.findNavController(Login.this)
                            .navigate(R.id.action_to_Successfull);
                }
                else Toast.makeText(activity, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void openCamera(){
        Activity activity = getActivity();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(intent , 999);
        }catch (ActivityNotFoundException e) {
            Toast.makeText(activity , "No camera software",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public String getImagePath(String username, Database database) {
        Cursor users = database.getInfor("Select * from users where username = '" + username + "' ");
        String imagePath = "";
        if(users.moveToLast()) {
            imagePath = users.getString(3);
        }
        return  imagePath;
    }

}