package com.example.faceid;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.faceid.databinding.RedirectBinding;
import com.example.faceid.databinding.RedirectBinding;


public class Redirect extends Fragment{
    private RedirectBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = RedirectBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();
       database = new Database(activity,"User.sqlite", null, 1);
        Cursor users = database.getInfor("SELECT * FROM users");
        while(users.moveToNext()) {
            String name = users.getString(1);
            Toast.makeText(activity, name, Toast.LENGTH_SHORT).show();
        }
    }*/

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Redirect.this)
                        .navigate(R.id.action_to_Login);
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
}
