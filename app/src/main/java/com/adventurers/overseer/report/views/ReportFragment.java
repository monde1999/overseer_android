package com.adventurers.overseer.report.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adventurers.overseer.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportFragment extends BottomSheetDialogFragment {
    View view;
    ReportsRecyclerViewAdapter adapter;
    ActivityResultLauncher<Intent> galleryResultLauncher;
    ActivityResultLauncher<Intent> cameraResultLauncher;
    List<Uri> imagePath = new ArrayList<>();
    String currentImagePath = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            if (data != null) {
                                Toast.makeText(getContext(),data.getData().toString(),Toast.LENGTH_LONG).show();
                                imagePath.add(data.getData());
                                adapter.notifyItemInserted(imagePath.size() - 1);
                            }
                        }
                    }
                }
        );
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Uri uri = Uri.fromFile(new File(currentImagePath));
                            Toast.makeText(getContext(),uri.toString(),Toast.LENGTH_LONG).show();
                            imagePath.add(uri);
                            adapter.notifyItemInserted(imagePath.size() - 1);
                        }
                    }
                }
        );
    }

    // region Fragment...
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }
    // endregion


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                if(bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                view = getView();
                if (view != null) {
                    Button upload = view.findViewById(R.id.report_btn_upload);
                    upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Cancel" }; // create a menuOption Array
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(optionsMenu[i].equals("Take Photo")){
                                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if(takePicture.resolveActivity(getContext().getPackageManager())!=null) {
                                            File image = null;
                                            try {
                                                image = getImageFile();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            if(image!=null) {
                                                Uri imageUri = FileProvider.getUriForFile(getContext(), "com.adventurers.fileprovider", image);
                                                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                                cameraResultLauncher.launch(takePicture);
                                            }
                                        }
                                    }
                                    else if(optionsMenu[i].equals("Choose from Gallery")) {
                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        galleryResultLauncher.launch(pickPhoto);
                                    }
                                    else {
                                        dialogInterface.dismiss();
                                    }
                                }
                            });
                            builder.show();
                        }
                    });
                    RecyclerView recyclerView = view.findViewById(R.id.report_recycler);
                    LinearLayoutManager horizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(horizontal);

                    adapter = new ReportsRecyclerViewAdapter(imagePath);
                    recyclerView.setAdapter(adapter);
                }

            }
        });
        return dialog;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "jpg_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        currentImagePath = image.getAbsolutePath();
        return image;
    }
}
