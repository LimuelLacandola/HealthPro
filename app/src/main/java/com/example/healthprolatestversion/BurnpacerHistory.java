package com.example.healthprolatestversion;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;

public class BurnpacerHistory extends AppCompatActivity {

    private TableLayout tableLayout;
    private ImageView historyback;

    private DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burnpacer_history);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        historyback = findViewById(R.id.historyback);
        tableLayout = findViewById(R.id.tableLayout);

        historyback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BurnpacerHistory.this, burnpacer.class);
                startActivity(intent);
                finish();
            }
        });


        // Inflate the header row layout
        TableRow headerRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.burnpacer_table_row, null);

        // Set the column header text
        TextView stepsHeader = headerRow.findViewById(R.id.stepsTextView);
        stepsHeader.setText("Steps");

        TextView caloriesHeader = headerRow.findViewById(R.id.caloriesTextView);
        caloriesHeader.setText("Cal.");

        TextView kmHeader = headerRow.findViewById(R.id.kmTextView);
        kmHeader.setText("Km");

        TextView dateHeader = headerRow.findViewById(R.id.dateTextView);
        dateHeader.setText("Date");

        TextView timeHeader = headerRow.findViewById(R.id.timeTextView);
        timeHeader.setText("Time");

        // Apply styling to the header row
        headerRow.setBackgroundResource(R.drawable.row_border);

        // Add the header row to the table layout
        tableLayout.addView(headerRow);



        // Retrieve data from Firebase and populate the table
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersRef = database.getReference("users").child(userId).child("burnpacerhistory");
        }

        if (usersRef != null) {
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int rowColor = 0; // Variable to keep track of row color
                    for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                        Long steps = entrySnapshot.child("steps").getValue(Long.class);
                        Long calories = entrySnapshot.child("calories").getValue(Long.class);
                        Long kilometers = entrySnapshot.child("kilometers").getValue(Long.class);
                        String date = entrySnapshot.child("date").getValue(String.class); // Retrieve the date
                        String time = entrySnapshot.child("time").getValue(String.class);

                        String stepsString = String.valueOf(steps);
                        String caloriesString = String.valueOf(calories);
                        String kilometersString = String.valueOf(kilometers);

                        // Inflate the table row layout
                        TableRow burnpacer_table_row = (TableRow) LayoutInflater.from(BurnpacerHistory.this).inflate(R.layout.burnpacer_table_row, null);

                        // Set row background color based on even/odd row
                        if (rowColor % 2 == 0) {
                            burnpacer_table_row.setBackgroundResource(R.drawable.even_row_background);
                        } else {
                            burnpacer_table_row.setBackgroundResource(R.drawable.odd_row_background);
                        }

                        rowColor++;

                        // Populate the table row with data
                        TextView stepsTextView = burnpacer_table_row.findViewById(R.id.stepsTextView);
                        stepsTextView.setText(stepsString);

                        TextView caloriesTextView = burnpacer_table_row.findViewById(R.id.caloriesTextView);
                        caloriesTextView.setText(caloriesString);

                        TextView kmTextView = burnpacer_table_row.findViewById(R.id.kmTextView);
                        kmTextView.setText(kilometersString);

                        TextView dateTextView = burnpacer_table_row.findViewById(R.id.dateTextView); // Find the date TextView
                        dateTextView.setText(date); // Set the date

                        TextView timeTextView = burnpacer_table_row.findViewById(R.id.timeTextView);
                        timeTextView.setText(time);

                        tableLayout.addView(burnpacer_table_row); // Add the table row to the table layout
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }

        ImageView saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStoragePermission();
            }
        });
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            } else {
                saveLayoutAsImage();
            }
        } else {
            saveLayoutAsImage();
        }
    }

    private static final int REQUEST_PERMISSION = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveLayoutAsImage();
            } else {
                Toast.makeText(this, "Permission denied. Unable to save image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveLayoutAsImage() {
        // Create a bitmap with the same dimensions as the layout
        Bitmap bitmap = Bitmap.createBitmap(tableLayout.getWidth(), tableLayout.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a canvas with the bitmap
        Canvas canvas = new Canvas(bitmap);

        // Draw the layout's content onto the canvas
        tableLayout.draw(canvas);

        // Get the content resolver
        ContentResolver contentResolver = getContentResolver();

        // Set the image details
        ContentValues values = new ContentValues();
        long currentTime = System.currentTimeMillis();
        String fileName = "BurnPacerHistory" + currentTime + ".png"; // Generate a unique file name
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        // Insert the image into the MediaStore
        Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (imageUri != null) {
            try {
                // Open an output stream for the image URI
                OutputStream outputStream = contentResolver.openOutputStream(imageUri);

                // Compress the bitmap and write it to the output stream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                // Flush and close the output stream
                outputStream.flush();
                outputStream.close();

                // Show a toast message to indicate that the image has been saved
                Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                // Show an error toast if there was an issue saving the image
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show an error toast if imageUri is null
            Toast.makeText(this, "Failed to create image", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BurnpacerHistory.this, burnpacer.class);
        startActivity(intent);
        finish();
    }
}





