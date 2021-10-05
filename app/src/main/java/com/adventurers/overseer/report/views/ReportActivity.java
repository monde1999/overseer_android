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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
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
import com.adventurers.overseer.floodforecast.models.DateTime;
import com.adventurers.overseer.helpers.GeocoderHelper;
import com.adventurers.overseer.helpers.ImageHelper;
import com.adventurers.overseer.helpers.PermissionHelper;
import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.report.presenters.ReportPresenter;
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

    private View view;
    private IReportView iReportView;
    private ReportRecyclerViewAdapter adapter;
    private ActivityResultLauncher<Intent> galleryIntentResultLauncher;
    private ActivityResultLauncher<Intent> cameraIntentResultLauncher;
    private List<File> imageFiles;
    private String takePhotoPath;

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
    // region DialogFragment...
    public void submit(EditText et_description,NumberPicker np_flood_level) {
        user = 1;
        description = et_description.getText().toString();
        floodLevel = np_flood_level.getValue();
        ReportPresenter presenter = new ReportPresenter(this);
        Toast.makeText(getContext(),
                "user: " + user
                        + "\nreportedLocation: " + reportedLocation.toString()
                        + "\ntime: "
                        + "\nfloodLevel: " + floodLevel
                        + "\nimageFiles: " + imageFiles.size()
                        + "\ndescription: " + description,
                Toast.LENGTH_LONG).show();
        presenter.report(user, reportedLocation, time, floodLevel, imageFiles, description);
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
        return inflater.inflate(R.layout.fragment_report, container, false);
    }
    // endregion

    // region BottomSheetDialogFragment...
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
                    Button btn_upload = view.findViewById(R.id.report_btn_upload);
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

                    RecyclerView recyclerView = view.findViewById(R.id.report_recycler);
                    LinearLayoutManager horizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(horizontal);
                    adapter = new ReportRecyclerViewAdapter(imageFiles);
                    recyclerView.setAdapter(adapter);

                    NumberPicker np_flood_level = view.findViewById(R.id.report_np_flood_level);
                    np_flood_level.setMinValue(1);
                    np_flood_level.setMaxValue(5);
                    np_flood_level.setWrapSelectorWheel(false);
                    String[] pickerVals = new String[] {"1","2","3","4","5"};
                    np_flood_level.setDisplayedValues(pickerVals);

                    EditText et_description = view.findViewById(R.id.report_et_caption);

                    Button btn_submit = view.findViewById(R.id.report_btn_submit);
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           submit(et_description,np_flood_level);
                        }
                    });

                    TextView tv_location = view.findViewById(R.id.report_tv_location);
                    ImageView iv_location = view.findViewById(R.id.report_iv_location);
                    TextView tv_time_location = view.findViewById(R.id.report_tv_time_location);

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
