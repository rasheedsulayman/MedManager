package com.r4sh33d.medmanager.updateprofile;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class UpdateProfilePresenter implements UpdateProfileContract.Presenter {
    private StorageReference profilePicref;
    private FirebaseStorage storageInstance;
    FirebaseUser user;
    private UpdateProfileContract.View view;
    UploadTask uploadImageTask;
    Task updateProfileDetailsTask;
    String userDisplayName;

    public UpdateProfilePresenter(UpdateProfileContract.View view) {
        this.view = view;
        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        storageInstance = FirebaseStorage.getInstance();
        profilePicref = storageInstance.getReference().child(uid).child("profilepic.jpg");
    }

    @Override
    public void start() {

    }


    void uploadImage(String path) {

    }

    @Override
    public void updateProfile(String displayName, String path) {
        userDisplayName = displayName;
        view.showLoading("Updating Profile . . .");
        if (!TextUtils.isEmpty(path)) {
            Uri file = Uri.fromFile(new File(path));
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();
            uploadImageTask = profilePicref.putFile(file, metadata);
            uploadImageTask.addOnFailureListener(fileUploadFailureListener)
                    .addOnSuccessListener(fileUploadSuccessListener);
        } else {
            updateProfileDetails(displayName, null);
        }
    }

    private OnSuccessListener fileUploadSuccessListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            updateProfileDetails(userDisplayName, downloadUrl);
        }
    };

    private OnFailureListener fileUploadFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // We still want to update the display name if image upload is not successful;
            updateProfileDetails(userDisplayName, null);
        }
    };

    @Override
    public void updateProfileDetails(String displayName, Uri photoUri) {
        UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();
        if (!TextUtils.isEmpty(displayName)) {
            profileUpdatesBuilder.setDisplayName(displayName);
        }
        if (photoUri != null) {
            profileUpdatesBuilder.setPhotoUri(photoUri);
        }
        updateProfileDetailsTask = user.updateProfile(profileUpdatesBuilder.build())
                .addOnCompleteListener(updateProfileCompleteListener)
                .addOnFailureListener(updateProfileFailureListener);
    }

    private OnCompleteListener updateProfileCompleteListener = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            view.dismissLoading();
            //the might be killed before we return here
            if (view != null) {
                if (task.isSuccessful()) {
                    view.onProfileSuccesfullyUpdated();
                    view.showSuccessDialog("Profile successfully updated",
                            (dialog, which) -> {});
                } else {
                    view.showError("Update Profile failed");
                }
            }
        }
    };

    private OnFailureListener updateProfileFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            view.dismissLoading();
            if (view != null) {
                view.showError("Update Profile failed");
            }
        }
    };


    @Override
    public void detachListeners() {
        if (uploadImageTask != null){
            uploadImageTask.removeOnSuccessListener(fileUploadSuccessListener)
                    .removeOnFailureListener(fileUploadFailureListener);
        }
    }
}
