package com.example.faceid;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.faceid.databinding.RegisterBinding;
import com.example.faceid.databinding.RegisterBinding;

import java.io.File;

public class Register extends Fragment {

    private RegisterBinding binding;
    EditText username;
    EditText password;
    EditText retypePassword;
    Database database;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = RegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Activity activity = getActivity();
                database = new Database(activity, "User.sqlite", null, 1);

                String usernameData = username.getText().toString().trim();
                String passwordData = password.getText().toString().trim();
                String retypePasswordData = retypePassword.getText().toString().trim();

                if(TextUtils.isEmpty(usernameData)) {
                   Toast.makeText(activity, "Please enter username", Toast.LENGTH_SHORT).show();
                   return;
                }
                else {
                    if (TextUtils.isEmpty(passwordData)) {
                        Toast.makeText(activity, "Please enter password", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (TextUtils.isEmpty(retypePasswordData) || !passwordData.equals(retypePasswordData)) {
                            Toast.makeText(activity, "Re-entered password does not match", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Cursor users = database.getInfor("SELECT * FROM users");
                            boolean check = false;
                            while(users.moveToNext()) {
                                String username = users.getString(1);
                                if(usernameData.equals(username)) check = true;
                            }
                            if(check) Toast.makeText(activity, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                            else {
                                database.QueryData("INSERT INTO users VALUES (null, '" + usernameData + "', '" + passwordData + "', null)");
                                NavHostFragment.findNavController(Register.this)
                                        .navigate(R.id.action_to_Redirect);
                            }

                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void getData() {
        username = (EditText) getView().findViewById(R.id.username);
        password = (EditText) getView().findViewById(R.id.password);
        retypePassword = (EditText) getView().findViewById(R.id.retype);
    }
    public void draftRegister() {
        System.out.print("Create draft register --window")
    }


}