package com.adventurers.overseer.report.views;

import static com.adventurers.overseer.Constants.preferencesKey;
import static com.adventurers.overseer.R.*;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adventurers.overseer.R;
import com.adventurers.overseer.floodforecast.models.DateTime;
import com.adventurers.overseer.helpers.GeocoderHelper;
import com.adventurers.overseer.helpers.ImageHelper;
import com.adventurers.overseer.helpers.PermissionHelper;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.report.presenters.ReportPresenter;
import com.adventurers.overseer.user.handlers.UserInfoHandler;
import com.adventurers.overseer.user.models.UserInfo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ReportActivity extends BottomSheetDialogFragment implements IReportView, EasyPermissions.PermissionCallbacks {
    private Location reportedLocation;
    private DateTime time;
    private int floodLevel;
    private int user;
    private String description;
    private SeekBar seekBar_flood_level;
    private ImageView iv_flood_level;

    private View view;
    private IReportView iReportView;
    private ReportRecyclerViewAdapter adapter;
    private ActivityResultLauncher<Intent> galleryIntentResultLauncher;
    private ActivityResultLauncher<Intent> cameraIntentResultLauncher;
    private List<File> imageFiles;
    private String takePhotoPath;

    public ReportActivity() {
    }

    public static ReportActivity newInstance(Location location) {
        ReportActivity reportActivity = new ReportActivity();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(location);
        args.putString("Location", json);
        reportActivity.setArguments(args);
        return reportActivity;
    }

    // region IReportView...
    @Override
    public void renderReportSuccess(String message) {
        Toast.makeText(getActivity(),  message, Toast.LENGTH_LONG).show();
        this.dismiss();
    }

    @Override
    public void renderReportFailure(int error_code , String message) {
        Toast.makeText(getActivity(),  error_code+":"+message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderReportProgressing(String message) {
        Toast.makeText(getActivity(),  message, Toast.LENGTH_LONG).show();
    }
    // endregion

    // region DialogFragment...
    public void submit(EditText et_description,SeekBar seekBar_flood_level) {
        description = et_description.getText().toString();
        floodLevel = seekBar_flood_level.getProgress() + 1;
        ReportPresenter presenter = new ReportPresenter(this);
        if(imageFiles==null || imageFiles.size()==0){
            Toast.makeText(getContext(),"Image is missing. Please Try again.",Toast.LENGTH_LONG).show();
        }
        else if(description.isEmpty()){
            Toast.makeText(getContext(),"Please add a description.",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getContext(),
                    "user: " + user
                            + "\nreportedLocation: " + reportedLocation.toString()
                            + "\ntime: " + time
                            + "\nfloodLevel: " + floodLevel
                            + "\nimageFiles: " + imageFiles.size()
                            + "\ndescription: " + description,
                    Toast.LENGTH_LONG).show();
            presenter.report(user, reportedLocation, time, floodLevel, imageFiles, description);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageFiles = new ArrayList<>();
        iReportView = (IReportView) getView();
        if(getArguments() != null){
            String json = getArguments().getString("Location");
            Gson gson = new Gson();
            reportedLocation = gson.fromJson(json, Location.class);
        }

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
    // endregion

    // region Fragment...
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layout.fragment_report, container, false);
    }
    // endregion

    // region BottomSheetDialogFragment...
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(id.design_bottom_sheet);
                if(bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                view = getView();
                if (view != null) {
                    Button btn_upload = view.findViewById(id.report_btn_upload);
                    btn_upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PermissionHelper.ensureCameraAndWriteExternalStorage(ReportActivity.this);
                            if(!PermissionHelper.isCameraGranted(getContext())) {
                                return;
                            }
                            if(!PermissionHelper.isWriteExternalStorageGranted(getContext())) {
                                return;
                            }
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

                    RecyclerView recyclerView = view.findViewById(id.report_recycler);
                    LinearLayoutManager horizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(horizontal);
                    adapter = new ReportRecyclerViewAdapter(imageFiles);
                    recyclerView.setAdapter(adapter);
                    seekBar_flood_level = (SeekBar) view.findViewById(id.seekBar4);
                    iv_flood_level = view.findViewById(id.report_iv_flood_level);
                    EditText et_description = view.findViewById(id.report_et_caption);

                    seekBar_flood_level.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            Drawable flooded_1  = getResources().getDrawable(drawable.flooded_level_model_1,null);
                            Drawable flooded_2  = getResources().getDrawable(drawable.flooded_level_model_2,null);
                            Drawable flooded_3  = getResources().getDrawable(drawable.flooded_level_model_3,null);
                            Drawable flooded_4  = getResources().getDrawable(drawable.flooded_level_model_4,null);
                            Drawable flooded_5  = getResources().getDrawable(drawable.flooded_level_model_5,null);
                            switch(seekBar_flood_level.getProgress()){
                                case 0: {
                                    iv_flood_level.setImageDrawable(flooded_1);
                                    break;
                                }
                                case 1: {
                                    iv_flood_level.setImageDrawable(flooded_2);
                                    break;
                                }
                                case 2:{
                                    iv_flood_level.setImageDrawable(flooded_3);
                                    break;
                                }
                                case 3:{
                                    iv_flood_level.setImageDrawable(flooded_4);
                                    break;
                                }
                                case 4:{
                                    iv_flood_level.setImageDrawable(flooded_5);
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) { }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) { }
                    });

                    Button btn_submit = view.findViewById(id.report_btn_submit);
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           submit(et_description,seekBar_flood_level);
                        }
                    });

                    TextView tv_name = view.findViewById(id.report_tv_username);
                    TextView tv_location = view.findViewById(id.report_tv_location);
                    ImageView iv_location = view.findViewById(id.report_iv_location);
                    TextView tv_time_location = view.findViewById(id.report_tv_time_location);

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(preferencesKey, Context.MODE_PRIVATE);
                    if(UserInfoHandler.hasAccountStored(sharedPreferences)){
                        UserInfo currentUser = UserInfoHandler.getCurrentUser(sharedPreferences);
                        String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
                        tv_name.setText(fullName);
                        user = currentUser.getId();
                    }
                    else {
                        tv_name.setText("Unknown User");
                        user = -1;
                    }

                    String address = GeocoderHelper.getLocationAddress(reportedLocation, getContext());
                    ImageHelper.loadStreetStaticView(reportedLocation, iv_location);
                    String timeLocation = "Just Now • " + address;
                    tv_location.setText(address);
                    tv_time_location.setText(timeLocation);
                }

            }
        });
        return dialog;
    }
    // endregion

    // region Permissions...

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // Some permissions have been granted
        PermissionHelper.ensureCameraAndWriteExternalStorage(this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // Some permissions have been denied

        // Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            String rationale = "Camera and Storage Permissions is required by the app to work properly. " +
                    "You can enable it in settings.";
            PermissionHelper.openApplicationInSettings(this, rationale);
        }
        else {
            PermissionHelper.ensureCameraAndWriteExternalStorage(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen.
            //PermissionHelper.ensureCameraAndWriteExternalStorage(this);
        }
    }

    // endregion

    private File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "jpg_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        takePhotoPath = image.getAbsolutePath();
        return image;
    }
}
