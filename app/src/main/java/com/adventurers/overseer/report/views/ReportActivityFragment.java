package com.adventurers.overseer.report.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.adventurers.overseer.R;
import com.adventurers.overseer.helpers.FileUtil;
import com.adventurers.overseer.report.presenters.ReportPresenter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class ReportActivityFragment extends BottomSheetDialogFragment implements IReportView {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101 ;
    private static final int REQUEST_LOCATION = 1;
    private EditText description;
    protected LocationManager locationManager;
    ImageButton upload;
    Button submit;
    ReportPresenter rp;
    ImageView imageView;
    Date datetime;
    List<File> pictureList = new ArrayList<File>();
    LinearLayout imageContainer;
    int userID;
    TextView displayName,displayLocation1,l2, displayLocation;
    String name;
    double latitude, longitude;
    int floodLevel;
    int imageCount;
    Address address;

    public static ReportActivityFragment newInstance() {
        return new ReportActivityFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.AppBottomSheetDialogTheme);
        userID = 1; //static
        name = "Juan Dela Cruz"; //static
        floodLevel = 4; //flood level to be calculated
        imageCount =0;
    }

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_activity, container, false);
        ActivityCompat.requestPermissions( getActivity(),
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableGPS();
        } else {
            getLocationLocationManager();
        }
        Toast.makeText(getActivity(),  "lat=" + latitude, Toast.LENGTH_LONG).show();
        address = getAddress(latitude,longitude);
        description = (EditText) view.findViewById(R.id.descript);
        upload = (ImageButton) view.findViewById(R.id.uploadpic);
        submit = (Button) view.findViewById(R.id.submit);
        imageView = (ImageView) view.findViewById(R.id.imageView2);
        imageContainer = (LinearLayout) view.findViewById(R.id.imageContainer);
        displayName = (TextView) view.findViewById(R.id.name);
        displayLocation1 = (TextView) view.findViewById(R.id.locationToBeReported);
        displayLocation = (TextView) view.findViewById(R.id.dateAndLocation);
        displayLocation1.setText(address.getFeatureName()+"\n"+address.getLocality());
        displayName.setText(name);
        displayLocation.setText(address.getFeatureName() + "." + address.getLocality());
        rp = new ReportPresenter(this);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datetime = new Date();
                String desc = description.getText().toString();
                if(desc==null){
                    Toast.makeText(getActivity(),  "Please enter a description", Toast.LENGTH_LONG).show();
                }

                else if(pictureList==null){
                    Toast.makeText(getActivity(),  "Please upload an image", Toast.LENGTH_LONG).show();
                }
                else {
                    rp.report(userID, latitude, longitude, datetime, floodLevel, pictureList, desc);
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkAndRequestPermissions(getActivity())) {
                    chooseImage(getActivity());
                }
            }
        });
        return view;
    }

    @Override
    public void renderReportSuccess() {
        Toast.makeText(getActivity(),  "Success", Toast.LENGTH_LONG).show();
    }
    @Override
    public void renderReportFailure(int errorCode, String errorMessage) {
        Toast.makeText(getActivity(),  "Error "+errorCode+ ":" + errorMessage, Toast.LENGTH_LONG).show();
    }
    @Override
    public void renderReportProgressing() {
        Toast.makeText(getActivity(),  "In progress...", Toast.LENGTH_LONG).show();
    }

    //helpers
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path.toString());
    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                } else if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(getActivity());
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        if (imageCount != 0){
                            CardView card = new CardView (getActivity());
                            ImageView image = new ImageView(getActivity());
                            image.setScaleType(ImageView.ScaleType.FIT_XY);
                            image.setImageBitmap(selectedImage);
                            image.setMinimumWidth(dpToPx(165));
                            image.setMinimumHeight(dpToPx(137));
                            card.addView(image);
                            card.setBackgroundResource(R.drawable.rounded_rec);
                            imageContainer.addView(card);
                            try {
                                pictureList.add(FileUtil.from(getActivity(), getImageUri(getContext(),selectedImage)));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            try {
                                pictureList.add(FileUtil.from(getActivity(), getImageUri(getContext(),selectedImage)));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            imageView.setImageBitmap(selectedImage);
                            imageCount++;
                        }
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                if(imageCount!=0) {
                                    CardView card = new CardView (getActivity());
                                    ImageView image = new ImageView(getActivity());
                                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                                    image.setMaxWidth(dpToPx(165));
                                    image.setImageURI(selectedImage);
                                    card.addView(image);
                                    card.setMinimumHeight(dpToPx(2));
                                    card.setBackgroundResource(R.drawable.upload_images);
                                    imageContainer.addView(card);

                                    try {
                                        pictureList.add(FileUtil.from(getActivity(), selectedImage));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    try {
                                        pictureList.add(FileUtil.from(getActivity(),selectedImage));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                    imageCount++;
                                }
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    public Address getAddress(double lat, double lng) {
        Address obj = null;
        Geocoder geocoder = new Geocoder(getActivity(),
                Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            String currentAddress = obj.getSubAdminArea() + ","
                    + obj.getAdminArea();
            double latitude = obj.getLatitude();
            double longitude = obj.getLongitude();
            String currentCity = obj.getSubAdminArea();
            String currentState = obj.getAdminArea();
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return obj;
    }
    private void enableGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocationLocationManager() {
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                latitude = locationGPS.getLatitude();
                longitude = locationGPS.getLongitude();
            }
            else {
                latitude=9.6447;
                longitude=123.3711;
                Toast.makeText(getActivity(), "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}