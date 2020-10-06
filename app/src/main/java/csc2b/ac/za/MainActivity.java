package csc2b.ac.za;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.StringTokenizer;
import java.util.concurrent.Executors;

import csc2b.ac.za.client.ImageClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button downloadButton = (Button) findViewById(R.id.downloadButton);
        Button listButton = (Button) findViewById(R.id.imagesListButton);
        final EditText idView = (EditText) findViewById(R.id.ImageID);
        final EditText idList = (EditText) findViewById(R.id.imagesList);
        final ImageView imageView = findViewById(R.id.imageView);

        final ImageClient clientConnection = new ImageClient();

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Executors.newSingleThreadExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        try  {
                            final String id = idView.getText().toString();
                            String response = clientConnection.downloadImage(id);
                            StringTokenizer responseTokenizer = new StringTokenizer(response, ".");
                            final String imageName = responseTokenizer.nextToken();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(imageName);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] message = {""};

                Executors.newSingleThreadExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        try  {
                            message[0] = clientConnection.readImagesList();System.out.println(message[0]);
                            StringTokenizer messageTokens = new StringTokenizer(message[0], "@");
                            String list = "";
                            while (messageTokens.hasMoreTokens()){
                                list += messageTokens.nextToken() + "\r\n";
                            }

                            final String finalList = list;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    idList.setText(finalList);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}