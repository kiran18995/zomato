package io.mountblue.zomato.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mountblue.zomato.MainActivity;
import io.mountblue.zomato.R;
import io.mountblue.zomato.RoundedTransformation;
import io.mountblue.zomato.view.activity.LoginActivity;

public class ProfileFragment extends Fragment {

    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.profile_email)
    TextView profileEmail;
    @BindView(R.id.log_out)
    TextView logOut;
    @BindView(R.id.profile_image)
    ImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            profileName.setText(account.getDisplayName());
            profileEmail.setText(account.getEmail());
            Picasso.with(getContext()).load(account.getPhotoUrl())
                    .placeholder(R.drawable.profile_placeholder)
                    .transform(new RoundedTransformation(200, 0))
                    .fit().centerCrop()
                    .into(profileImage);
        }

        Fragment fragment = new BookmarkFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment)
                .commit();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return view;
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignIn.getClient(getContext(), gso).signOut()
                .addOnCompleteListener(getActivity(), task -> {
                    Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_LONG).show();
                });
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
