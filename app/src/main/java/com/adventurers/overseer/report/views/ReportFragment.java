package com.adventurers.overseer.report.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportFragment extends BottomSheetDialogFragment {
    private View view;
    private ReportsRecyclerViewAdapter adapter;
    private ActivityResultLauncher<Intent> galleryIntentResultLauncher;
    private ActivityResultLauncher<Intent> cameraIntentResultLauncher;
    private List<File> imageFiles;
    private String takePhotoPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageFiles = new ArrayList<>();

        galleryIntentResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Cursor cursor = null;
                                try {
                                    // Convert Uri to file pathname
                                    String[] proj = { MediaStore.Images.Media.DATA };
                                    cursor = getContext().getContentResolver().query(data.getData(),  proj, null, null, null);
                                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                    cursor.moveToFirst();
                                    String pickPhotoPath =  cursor.getString(column_index);

                                    File image = new File(pickPhotoPath);
                                    imageFiles.add(image);
                                    adapter.notifyItemInserted(imageFiles.size() - 1);
                                } finally {
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                }
                            }
                        }
                    }
                }
        );
        cameraIntentResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            File image = new File(takePhotoPath);
                            imageFiles.add(image);
                            adapter.notifyItemInserted(imageFiles.size() - 1);
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
                                        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if(takePhoto.resolveActivity(getContext().getPackageManager())!=null) {
                                            File image = null;
                                            try {
                                                image = getImageFile();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            if(image!=null) {
                                                Uri imageUri = FileProvider.getUriForFile(getContext(), "com.adventurers.fileprovider", image);
                                                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                                cameraIntentResultLauncher.launch(takePhoto);
                                            }
                                        }
                                    }
                                    else if(optionsMenu[i].equals("Choose from Gallery")) {
                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        galleryIntentResultLauncher.launch(pickPhoto);
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
                    adapter = new ReportsRecyclerViewAdapter(imageFiles);
                    recyclerView.setAdapter(adapter);
                }

            }
        });
        return dialog;
    }

    private File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "jpg_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        takePhotoPath = image.getAbsolutePath();
        return image;
    }
}
