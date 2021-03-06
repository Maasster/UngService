package app.ewtc.masterung.ungservice.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import app.ewtc.masterung.ungservice.R;
import app.ewtc.masterung.ungservice.utility.MyAlert;

/**
 * Created by masterung on 27/10/2017 AD.
 */

public class MainFragment extends Fragment {

    private String emailString, passwordString;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Register Controller
        registerController();

//        Login Controller
        loginController();

    }

    private void loginController() {
        Button button = getView().findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText emailEditText = getView().findViewById(R.id.edtUser);
                EditText passwordEditText = getView().findViewById(R.id.edtPassword);

                emailString = emailEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                if (emailString.isEmpty() || passwordString.isEmpty()) {
//                    Have Space
                    MyAlert myAlert = new MyAlert(getActivity());
                    myAlert.myDialog(getResources().getString(R.string.title_have_space),
                            getResources().getString(R.string.message_have_space));
                } else {
//                    No Space
                    checkEmailAndPass();
                }

            }
        });
    }

    private void checkEmailAndPass() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait Few Minus ...");
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
//                            SignIn Success
                            Toast.makeText(getActivity(), "Welcome To my Service",
                                    Toast.LENGTH_SHORT).show();

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFragmentMain, new ServiceFragment())
                                    .addToBackStack(null).commit();

                        } else {
//                            Cannot SignIn
                            MyAlert myAlert = new MyAlert(getActivity());
                            myAlert.myDialog("Cannot Sign In",
                                    "Because " + task.getException().getMessage());
                        }

                    }   // onConplete
                });

    }

    private void registerController() {
        TextView textView = getView().findViewById(R.id.txtRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentFragmentMain, new RegisterFragment())
                        .addToBackStack(null).commit();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }
}   // Main Class
