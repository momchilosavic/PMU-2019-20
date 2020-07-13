package com.example.cats.activities.welcomeActivity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.room.Database;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cats.R;
import com.example.cats.database.AppDatabase;
import com.example.cats.database.entities.User;
import com.example.cats.utils.AESCrypt;
import com.example.cats.viewmodels.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment {

    private OnRegisterListener mListener;
    UserViewModel userViewModel;
    AppDatabase database;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        userViewModel = UserViewModel.getInstance(getActivity());
        database = AppDatabase.getInstance(getContext());

        final TextInputLayout mailLayout = view.findViewById(R.id.mailInputLayout);
        final TextInputEditText mailEditText = view.findViewById(R.id.mailInputText);
        final TextInputLayout passwordLayout = view.findViewById(R.id.passwordInputLayout);
        final TextInputEditText passwordEditText = view.findViewById(R.id.passwordInputText);
        final TextInputLayout confirmPasswordLayout = view.findViewById(R.id.confirmPasswordInputLayout);
        final TextInputEditText confirmPasswordEditText = view.findViewById(R.id.confirmPasswordInputText);
        final TextInputLayout usernameLayout = view.findViewById(R.id.usernameInputLayout);
        final TextInputEditText usernameEditText = view.findViewById(R.id.usernameInputText);
        final TextView gotoLogin = view.findViewById(R.id.gotoLogin);
        final Button registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = mailEditText.getText().toString().toLowerCase();
                final String password = passwordEditText.getText().toString();
                final String confirmPassword = confirmPasswordEditText.getText().toString();
                final String username = usernameEditText.getText().toString();

                if(mail.isEmpty()) mailLayout.setError("You must enter mail!");
                else{
                    if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) mailLayout.setError("Mail not valid!");
                    else mailLayout.setError(null);
                }

                if(password.isEmpty()) passwordLayout.setError("You must enter password!");
                else{
                    if(password.length() < 6) passwordLayout.setError("Password min. length is 6");
                    else passwordLayout.setError(null);
                }

                if(confirmPassword.isEmpty()) confirmPasswordLayout.setError("You must confirm password!");
                else{
                    if(!confirmPassword.equals(password)) confirmPasswordLayout.setError("Passwords do not match!");
                    else confirmPasswordLayout.setError(null);
                }

                if(username.isEmpty()) usernameLayout.setError("You must enter username!");
                else usernameLayout.setError(null);

                if(mailLayout.getError() != null || passwordLayout.getError() != null || confirmPasswordLayout.getError() != null || usernameLayout.getError() != null)
                    return;

                database.UserDao().getCount(mail).observe(getActivity(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if(integer > 0)
                            mailLayout.setError("User " + mail + " already exists!");
                        else{
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            database.UserDao().register(new User(mail, username, AESCrypt.encrypt(password)));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            try {
                                database.UserDao().login(mail, AESCrypt.encrypt(password)).observe(getActivity(), new Observer<User>() {
                                    @Override
                                    public void onChanged(User user) {
                                        if(user != null) {
                                            userViewModel.updateUser(user);
                                            mListener.onRegister(user.id);
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGotoLogin();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterListener) {
            mListener = (OnRegisterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRegisterListener {
        // TODO: Update argument type and name
        void onRegister(int userId);
        void onGotoLogin();
    }
}
