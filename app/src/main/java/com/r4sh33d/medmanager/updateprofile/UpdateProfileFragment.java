package com.r4sh33d.medmanager.updateprofile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.r4sh33d.medmanager.base.BaseFragment;
import com.r4sh33d.medmanager.GlideApp;
import com.r4sh33d.medmanager.R;
import com.r4sh33d.medmanager.utility.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfileFragment extends BaseFragment implements  UpdateProfileContract.View{
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

    public UpdateProfileFragment() {
        // Required empty public constructor
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
        String displayName = String.format("%s %s", firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString());
        if (!displayName.equals(user.getDisplayName()) || (profilePicPath != null)) {
            updateContractPresenter.updateProfile(displayName , profilePicPath);
        }
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
            setProfilePic(data.getData());//update the choosen profile pic image as a preview
            profilePicPath = Utils.getRealPathFromURI(data.getData(), getContext());
        }
    }

    @OnClick(R.id.update_profile)
    void onClickUpdateProfilePic() {
        updateProfile();
    }

    void setProfilePic(Uri url) {
        GlideApp.with(this)
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(userProfilePic);
    }

    @Override
    public void onDestroyView() {
        updateContractPresenter.detachListeners();
        super.onDestroyView();
    }
}
