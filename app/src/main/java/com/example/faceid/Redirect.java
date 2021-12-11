package com.example.faceid;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.faceid.databinding.RedirectBinding;
import com.example.faceid.databinding.RedirectBinding;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Redirect extends Fragment{
    private RedirectBinding binding;
    Database database;
    ImageView imageView;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = RedirectBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                database = new Database(activity,"User.sqlite", null, 1);
                Cursor users = database.getInfor("SELECT * FROM users ");
                if(users.moveToLast()) {
                    String username = users.getString(1);
                    String hashUsername = hashUsername(username);
                    openCamera(activity, hashUsername);
                }
            }
        });
        binding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Redirect.this)
                        .navigate(R.id.action_to_Login);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    String currentPhotoPATH;
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
        database.QueryData("UPDATE users SET image = '" + currentPhotoPATH + "' where username = '" + username + "'");
        return image;
    }
    public String hashUsername(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
