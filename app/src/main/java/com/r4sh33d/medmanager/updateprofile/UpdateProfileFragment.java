package com.r4sh33d.medmanager.updateprofile;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.r4sh33d.medmanager.R;
import com.r4sh33d.medmanager.base.BaseFragment;
import com.r4sh33d.medmanager.utility.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfileFragment extends BaseFragment implements UpdateProfileContract.View {
    private static final String TAG = UpdateProfileFragment.class.getSimpleName();
    @BindView(R.id.first_name_edittext)
    TextInputEditText firstNameEditText;
    @BindView(R.id.last_name_edittext)
    TextInputEditText lastNameEditText;
    @BindView(R.id.user_profile_pic)
    CircleImageView userProfilePic;
    @BindView(R.id.image_upload_progressbar)
    ProgressBar imageUploadProgressbar;
    FirebaseUser user;
    public static final int PICK_IMAGE = 1;
    String profilePicPath;
    UpdateProfileContract.Presenter updateContractPresenter;
    String userEnteredDisplayName;
    ProfileUpdateListener profileUpdateListener;

    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileUpdateListener){
            profileUpdateListener = (ProfileUpdateListener)context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbarTitle("Update Profile");
        updateContractPresenter = new UpdateProfilePresenter(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String[] names = user.getDisplayName().split(" ");
            firstNameEditText.setText(names[0]);
            lastNameEditText.setText(names[1]);
            setProfilePic(user.getPhotoUrl());
        }
    }

    void updateProfile() {
        userEnteredDisplayName = String.format("%s %s", firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString());
        if (!isDisplayNameChanged(userEnteredDisplayName) && (profilePicPath == null)) {
            showToast("Please make changes to update profile");
            return;
        }
        if (profilePicPath != null) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                updateContractPresenter.updateProfile(userEnteredDisplayName, profilePicPath);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        } else {
            updateContractPresenter.updateProfile(userEnteredDisplayName, null);
        }
    }

    boolean isDisplayNameChanged(String displayName) {
        return !displayName.equals(user.getDisplayName());
    }

    @OnClick(R.id.user_profile_pic)
    void onProfilePictureClicked() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                setProfilePic(data.getData());//update the choosen profile pic image as a preview
                profilePicPath = Utils.getRealPathFromURI(data.getData(), getContext());
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            updateContractPresenter.updateProfile(userEnteredDisplayName, profilePicPath);
        }
    }


    @OnClick(R.id.update_profile)
    void onClickUpdateProfilePic() {
        updateProfile();
    }

    void setProfilePic(Uri url) {
        Picasso.get()
                .load(url)
                .noFade()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(userProfilePic);
    }

    @Override
    public void onDestroyView() {
        updateContractPresenter.detachListeners();
        super.onDestroyView();
    }

    @Override
    public void onProfileSuccesfullyUpdated() {
        profileUpdateListener.onProfileUpdated();
    }

    public  interface  ProfileUpdateListener {
        void onProfileUpdated();
    }
}
