package com.foodswaha.foodswaha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MoreFragment extends Fragment implements
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private GoogleApiClient mGoogleApiClient;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more, container, false);
        String[] values = {"signIn"};
        //view.findViewById(R.id.signOut).setOnClickListener(this);
        mGoogleApiClient = DisplayHotelsActivity.getGoogleApiClient();
        return view;
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        view.findViewById(R.id.signOut).setVisibility(View.GONE);
                        ((TextView) view.findViewById(R.id.status)).setText("signout successfull");
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signOut:
                signOut();
                break;

        }
    }
}