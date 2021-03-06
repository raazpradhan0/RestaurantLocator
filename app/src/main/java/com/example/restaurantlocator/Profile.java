package com.example.restaurantlocator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantlocator.Model.User;
import com.example.restaurantlocator.Pravelent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Profile extends Fragment
{
    private CircleImageView profileImageView;
    private EditText fullNameEditText, userphoneEditText, addressEditText;
    private TextView profileChangeTextBtn;
    private ImageView closeTextBtn;
    private ImageView saveTextButton;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    private static final int GalleryPick =1;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        profileImageView = (CircleImageView) view.findViewById(R.id.settings_profile_image);
        fullNameEditText = (EditText) view.findViewById(R.id.settings_full_name);
        userphoneEditText = (EditText) view.findViewById(R.id.settings_phone);
        addressEditText = (EditText) view.findViewById(R.id.settings_address);
        profileChangeTextBtn = (TextView) view.findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (ImageView) view.findViewById(R.id.close_settings_btn);
        saveTextButton = (ImageView) view.findViewById(R.id.update_account_settings_btn);


        userInfoDisplay(profileImageView, fullNameEditText, userphoneEditText, addressEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               getActivity().finish();
               startActivity(new Intent(getActivity(), NavigationDrawer.class));
            }
            });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(getActivity()) ;
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                CropImage.activity(imageUri).setAspectRatio(1, 1);
                startActivityForResult(galleryIntent,GalleryPick);


            }
        });
        return view;
    }

    private void updateOnlyUserInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", fullNameEditText.getText().toString());
        userMap. put("address", addressEditText.getText().toString());
        userMap. put("phone", userphoneEditText.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(getActivity(), NavigationDrawer.class));
        Toast.makeText(getActivity(), "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        getActivity().finish();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            //CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = data.getData();
            //imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);

            //profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(getActivity(), "Error, Try Again.", Toast.LENGTH_SHORT).show();

        }
    }




    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(userphoneEditText.getText().toString()))
        {
            userphoneEditText.setError("Phone is Required");
            userphoneEditText.requestFocus();
        }

        else if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            fullNameEditText.setError("Name is Required");
            fullNameEditText.requestFocus();
        }


        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            addressEditText.setError("Address is Required");
            addressEditText.requestFocus();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage(); 
        }




    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(Prevalent.currentOnlineUser.getPhone()+ ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", fullNameEditText.getText().toString());
                        userMap.put("address", addressEditText.getText().toString());
                        userMap.put("phone", userphoneEditText.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(getActivity(), NavigationDrawer.class));
                        Toast.makeText(getActivity(), "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                    }
                    else
                        {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else
        {
            Toast.makeText(getActivity(), "Image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView  profileImageView, final EditText fullNameEditText, final EditText userphoneEditText, final EditText addressEditText)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userphoneEditText.setText(phone);
                        addressEditText.setText(address);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}