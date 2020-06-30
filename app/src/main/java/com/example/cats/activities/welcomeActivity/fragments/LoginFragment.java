package com.example.cats.activities.welcomeActivity.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

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

public class LoginFragment extends Fragment {

    private OnLoginListener mListener;
    UserViewModel userViewModel;
    AppDatabase database;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        userViewModel = UserViewModel.getInstance(getActivity());
        database = AppDatabase.getInstance(getContext());

        final TextInputLayout mailLayout = view.findViewById(R.id.mailInputLayout);
        final TextInputEditText mailEditText = view.findViewById(R.id.mailInputText);
        final TextInputLayout passwordLayout = view.findViewById(R.id.passwordInputLayout);
        final TextInputEditText passwordEditText = view.findViewById(R.id.passwordInputText);
        //final TextView forgotPassword = view.findViewById(R.id.forgotPassword);
        final TextView gotoRegister = view.findViewById(R.id.gotoRegister);
        final Button loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = mailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                if(mail.isEmpty()) mailLayout.setError("You must enter mail!");
                else{
                    if (!(Patterns.EMAIL_ADDRESS.matcher(mail).matches())) mailLayout.setError("Mail not valid!");
                    else mailLayout.setError(null);
                }
                if(password.isEmpty()) passwordLayout.setError("You must enter password!");
                else{
                    if(password.length() < 6) passwordLayout.setError("Password min. length is 6");
                    else passwordLayout.setError(null);
                }
                if(mailLayout.getError() != null || passwordLayout.getError() != null) return;

                try {
                    database.UserDao().login(mail, AESCrypt.encrypt(password)).observe(getActivity(), new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            if(user == null){
                                mailLayout.setError("Wrong username/password!");
                                passwordLayout.setError("Wrong username/password!");
                            }
                            else {
                                userViewModel.updateUser(user);
                                mListener.onLogin(user.id);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onForgotPassword();
            }
        });*/

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGotoRegister();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginListener) {
            mListener = (OnLoginListener) context;
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

    public interface OnLoginListener {
        // TODO: Update argument type and name
        void onLogin(int userId);
        void onGotoRegister();
        void onForgotPassword();
    }
}
