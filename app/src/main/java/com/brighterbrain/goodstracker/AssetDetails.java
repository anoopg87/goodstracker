package com.brighterbrain.goodstracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AssetDetails extends AppCompatActivity {

    public static final String ITEM = "ITEM";

    DBhandler mDBhandler;
    final int ADD = 0;
    final int UPDATE = 1;
    int OperationMode = ADD;
    int GALLERY_PICTURE = 1111, CAMERA_PICTURE = 2222;
    String selectedImagePath = "";
    Asset asset;
    int assetid = 0;

    ImageView mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_details);
        mPreview = (ImageView) findViewById(R.id.imgAsset);
        mDBhandler = new DBhandler(AssetDetails.this);
        if (null != getIntent().getParcelableExtra(ITEM)) {
            asset = getIntent().getParcelableExtra(ITEM);
            OperationMode = UPDATE;
            setTitle("Update asset details");
            ((EditText) findViewById(R.id.edName)).setText(asset.getName());
            ((EditText) findViewById(R.id.edDescription)).setText(asset.getDescription());
            ((EditText) findViewById(R.id.edLocation)).setText(asset.getLocation());
            ((EditText) findViewById(R.id.edCost)).setText(asset.getCost());
            assetid = Integer.valueOf(asset.get_ID());
            Log.d("ID", asset.get_ID());


            if ("".equals(asset.getImageUrl())) {
                mPreview.setImageResource(R.drawable.noimage);
            } else {
                selectedImagePath = asset.getImageUrl();
                mPreview.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
            }

        }else{
            setTitle("Add new assets");
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePickerDialog();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.asset_menu, menu);
        if (OperationMode == 0) {
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(true);
        } else if (OperationMode == 1) {

            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_save).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_save) {

            asset = new Asset();
            asset.setName(((EditText) findViewById(R.id.edName)).getText().toString());
            asset.setImageUrl(selectedImagePath);
            asset.setDescription(((EditText) findViewById(R.id.edDescription)).getText().toString());
            asset.setLocation(((EditText) findViewById(R.id.edLocation)).getText().toString());
            asset.setCost(((EditText) findViewById(R.id.edCost)).getText().toString());

            if (OperationMode == ADD) {


                if (mDBhandler.addAsset(asset) > 0) {

                    startActivity(new Intent(AssetDetails.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                } else {

                    Toast.makeText(AssetDetails.this, "Error in insertion", Toast.LENGTH_LONG).show();

                }


            } else if (OperationMode == UPDATE) {


                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
                myAlertDialog.setTitle("Do you want to update the item ?");
                myAlertDialog.setMessage("");

                myAlertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        asset = new Asset();
                        asset.set_ID(String.valueOf(assetid));
                        asset.setName(((EditText) findViewById(R.id.edName)).getText().toString());
                        asset.setImageUrl(selectedImagePath);
                        asset.setDescription(((EditText) findViewById(R.id.edDescription)).getText().toString());
                        asset.setLocation(((EditText) findViewById(R.id.edLocation)).getText().toString());
                        asset.setCost(((EditText) findViewById(R.id.edCost)).getText().toString());


                        if (mDBhandler.updateAsset(asset) > 0) {
                            startActivity(new Intent(AssetDetails.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else {
                            Toast.makeText(AssetDetails.this, "Error in updation", Toast.LENGTH_LONG).show();
                        }


                    }
                });

                myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                myAlertDialog.show();




            }

            return true;
        } else if (id == R.id.action_delete) {



            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
            myAlertDialog.setTitle("Do you want to delete the item ?");
            myAlertDialog.setMessage("");

            myAlertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    mDBhandler.deleteAsset(asset);
                    startActivity(new Intent(AssetDetails.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                }
            });

            myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            myAlertDialog.show();



            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    public void ImagePickerDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Select Picture Mode");
        myAlertDialog.setMessage("");

        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                pictureActionIntent.setType("image/*");
                pictureActionIntent.putExtra("return-data", true);
                startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
            }
        });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(pictureActionIntent, CAMERA_PICTURE);
            }
        });
        myAlertDialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            selectedImagePath = picturePath;
            cursor.close();
            mPreview.setImageBitmap(BitmapFactory.decodeFile(picturePath));


        } else if (requestCode == CAMERA_PICTURE && resultCode == RESULT_OK && null != data) {


            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            selectedImagePath = destination.getPath();
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPreview.setImageBitmap(thumbnail);


        }


    }


}
