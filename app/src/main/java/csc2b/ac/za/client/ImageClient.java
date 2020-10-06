package csc2b.ac.za.client;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

import static android.content.Context.DOWNLOAD_SERVICE;

public class ImageClient extends AsyncTask<Void, Void, Void> {

    private static PrintWriter pw = null;
    private static BufferedReader br = null;
    private static DataOutputStream out = null;
    private static DataInputStream in = null;
    Socket severConnection = null;
    private static boolean connected = false;

    public ImageClient()
    {
        //Create client connection
        execute();
    }

    protected Void doInBackground(Void... voids){
        try {
            severConnection = new Socket("10.0.2.2", 7455);

            pw = new PrintWriter(severConnection.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(severConnection.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(severConnection.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(severConnection.getInputStream()));

            System.out.println("Connected to the server.");
            connected = true;

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //Get images list
    public String readImagesList(){
        pw.println("DATA");
        pw.flush();

        String serverResponse = null;
        try {
            serverResponse = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    //Method to download image
    public String downloadImage(String fileID){
        pw.println("IMGRET <" + fileID + ">");
        pw.flush();

        String severResponse = null;

        FileOutputStream fos = null;
        try{
            String fileName = br.readLine();
            int fileSize = Integer.parseInt(br.readLine());

            File dir = new File("//sdcard//Download//");
            File file = new File(dir, fileName);

            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int n = 0;
            int totalBytes = 0;

            while(totalBytes != fileSize)
            {
                n = in.read(buffer,0, buffer.length);
                fos.write(buffer,0,n);
                fos.flush();
                totalBytes += n;
            }
            System.out.println("World");
            fos.close();
            severResponse = br.readLine();
            System.out.println(fileName + " downloaded.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return severResponse;
    }
}
