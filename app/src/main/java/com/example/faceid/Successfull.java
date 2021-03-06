package com.example.faceid;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.faceid.databinding.LoginBinding;
import com.example.faceid.databinding.SuccessfulBinding;
import com.example.faceid.databinding.SuccessfulBinding;


public class Successfull extends Fragment{
    private SuccessfulBinding binding;
    SharedPreferences sharedpreferences;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = SuccessfulBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        sharedpreferences = activity.getSharedPreferences("MyPrefs", Activity.MODE_PRIVATE);
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
                NavHostFragment.findNavController(Successfull.this)
                        .navigate(R.id.action_to_Login);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

}
