package com.example.han.adding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.han.adding.AddDialog.fileStr1;


public class UploadActivity extends AppCompatActivity {

    final int SELECT_FILE = 20;

    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "파일을 선택하세요."), 20);

        mStorageRef = FirebaseStorage.getInstance().getReference("file/");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {

            mStorageRef = FirebaseStorage.getInstance().getReference("file/");

            File file = Environment.getExternalStoragePublicDirectory(data.getData().getPath());
            String fileStr = file + "";
            int start = fileStr.indexOf("raw:/");
            int strLen = "raw:/".length();
            fileStr = fileStr.substring(start + strLen, fileStr.length());


            Log.v("유알엘:", file + "");

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();


            InputStream stream = null;
            try {
                stream = new FileInputStream(new File(fileStr));
                UploadTask uploadTask = mStorageRef.putStream(stream);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getApplicationContext(), "업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Toast.makeText(getApplicationContext(), "업로드 성공", Toast.LENGTH_SHORT).show();
                        Log.v("성공", taskSnapshot.getDownloadUrl() + "");
                        fileStr1 = taskSnapshot.getDownloadUrl().toString();
                        finish();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.v("과정1", e + "");
            }


//            Uri file = Uri.fromFile(new File(data.getData().getPath()));
//            UploadTask uploadTask = mStorageRef.putFile(file);
//
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(), "업로드 실패", Toast.LENGTH_SHORT).show();
//                    Log.v("실패", e.getMessage());
//                }
//            });
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(getApplicationContext(), "업로드 성공", Toast.LENGTH_SHORT).show();
//                    Log.v("성공", taskSnapshot.getDownloadUrl() + "");
//                }
//            });


        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            filePath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            if (idx == -1) {
                return "";
            }
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }

//    private void uploadFile() {
//        //if there is a file to upload
//        if (filePath != null) {
//            //displaying a progress dialog while upload is going on
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.show();
//
//            StorageReference riversRef = storageReference.child("images/pic.jpg");
//            riversRef.putFile(filePath)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            //if the upload is successfull
//                            //hiding the progress dialog
//                            progressDialog.dismiss();
//
//                            //and displaying a success toast
//                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            //if the upload is not successfull
//                            //hiding the progress dialog
//                            progressDialog.dismiss();
//
//                            //and displaying error message
//                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            //calculating progress percentage
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//
//                            //displaying percentage in progress dialog
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
//                        }
//                    });
//        }
//        //if there is not any file
//        else {
//            //you can display an error toast
//        }
//    }
}
