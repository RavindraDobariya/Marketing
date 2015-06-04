package com.ramsofttech.adpushlibrary.utility;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.ramsofttech.jokes.backend.registrationRecordApi.RegistrationRecordApi;
import com.ramsofttech.jokes.backend.registrationRecordApi.model.RegistrationRecord;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by Ravi on 4/11/2015.
 */
public class RegisterPushData {
    private String gmail = "";
    private Context context;
    private RegistrationRecord gcmData;

    public void registerData(Context context, String regId) {
        //getting gamilid of device
        this.context = context;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                gmail = account.name;
             // Toast.makeText(context, gmail, Toast.LENGTH_LONG).show();
                Log.i("--email", "nature---" + gmail);


            }
        }

        //getting country code number of sim
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String country_code = manager.getNetworkCountryIso();
        Log.i("--country code", country_code);


        //getting imei number of devices
        String imei = manager.getDeviceId();
        Log.i("--imei", "nature---" + imei);


        //gettig package name of application
        ApplicationInfo appInfo = context.getApplicationInfo();

        Log.i("--PackageName", "nature---" + appInfo.packageName);

        //getting application name
        String title = (String) (appInfo != null ? context.getPackageManager()
                .getApplicationLabel(appInfo) : "Unknown");

        Log.i("--appName", "nature---" + title);
        gcmData = new RegistrationRecord();
        gcmData.setAppName(title);
        gcmData.setPackageName(appInfo.packageName);
        gcmData.setCountry(country_code);
        gcmData.setGmailId(gmail);
        gcmData.setImei(imei);
        gcmData.setRegId(regId);
        gcmData.setActive(true);

        GcmRegistrationAsyncTask gcmRegistrationAsyncTask = new GcmRegistrationAsyncTask(context);
        gcmRegistrationAsyncTask.execute();

    }


    class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String> {
        private RegistrationRecordApi regService = null;

        private Context context;

        // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above


        public GcmRegistrationAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            if (regService == null) {
                RegistrationRecordApi.Builder builder = new RegistrationRecordApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                        // otherwise they can be skipped
                        .setRootUrl(Constant.BASE_URL)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                    throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end of optional local run code

                regService = builder.build();
            }


            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.

            try {
                regService.insert(gcmData).execute();

            } catch (IOException ex) {
                ex.printStackTrace();

            }

            return "success";
        }

        @Override
        protected void onPostExecute(String msg) {
        //    Toast.makeText(context,  msg, Toast.LENGTH_LONG).show();
            Log.i("RegisterPushData", "0000000" + msg);
            Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
        }
    }
}
