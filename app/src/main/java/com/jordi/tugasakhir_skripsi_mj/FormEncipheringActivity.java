package com.jordi.tugasakhir_skripsi_mj;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

public class FormEncipheringActivity extends AppCompatActivity {

    private Button btn_inputFile, btn_Encrypt;
    private EditText edt_InputFileName, edt_InputKey;
    private TextView txt_fileDirectory;
    private static final int READ_REQUEST_CODE = 42;

    File myExternalFile;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_enciphering);

        btn_inputFile = findViewById(R.id.btn_inFile);
        btn_Encrypt = findViewById(R.id.btn_Enciphering);
        edt_InputFileName = findViewById(R.id.edt_inFileName);
        edt_InputKey = findViewById(R.id.edt_inKey);
        txt_fileDirectory = findViewById(R.id.txt_fileDir);

        btn_inputFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edt_InputFileName.getText())) {
                    edt_InputFileName.setError("Nama file baru harus diisi!");
                } else if(TextUtils.isEmpty((edt_InputKey.getText()))||edt_InputKey.length() < 16) {
                    edt_InputKey.setError("Kunci harus diisi sebanyak 16 karakter!");
                }else {
                    performFileSearch();
                }
            }
        });

        //Progress Dialog Configuration.
        progressDialog = new ProgressDialog(FormEncipheringActivity.this);
        progressDialog.setTitle("Enciphering");
        progressDialog.setMessage("Harap Tunggu, Sedang melakukan enkripsi");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                final Uri finalUri = uri;
                txt_fileDirectory.setText(finalUri.getPath());

                //Enciphering process.
                btn_Encrypt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    //Mendapatkan bytes file video.
                                    InputStream is = getContentResolver().openInputStream(finalUri);

                                    //Mendapatkan karakter kunci.
                                    String value_edtKey;
                                    value_edtKey = edt_InputKey.getText().toString();
                                    byte[] bytesKey = value_edtKey.getBytes();

                                    //Setting penyimpanan file baru.
                                    String filename = "Encrypted - " + edt_InputFileName.getText().toString() + ".mp4";
                                    String filepath = "MyFileStorage";

                                    if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                                        btn_inputFile.setEnabled(false);
                                    } else {
                                        myExternalFile = new File(getExternalFilesDir(filepath), filename);
                                    }

                                    //Proses Enkripsi.
                                    Enciphering enc = new Enciphering();
                                    enc.encipher(is, bytesKey, myExternalFile);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //Close progress dialog.
                                progressDialog.dismiss();

                                Thread thread = new Thread() {
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Completed Message
                                                Toast.makeText(getApplicationContext(), "Saved to " + getFilesDir() + "/" +
                                                        "Encrypted - " + edt_InputFileName.getText().toString() + ".mp4", Toast.LENGTH_LONG).show();

                                                //Clear EditText.
                                                edt_InputFileName.setText("");
                                                edt_InputKey.setText("");
                                                txt_fileDirectory.setText("");
                                            }
                                        });
                                    }
                                };
                                thread.start();
                            }
                        }).start();
                    }
                });
            }
        }
    }

    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/mp4");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
