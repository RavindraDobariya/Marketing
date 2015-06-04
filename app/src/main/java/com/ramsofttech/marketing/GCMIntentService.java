/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ramsofttech.marketing;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.ramsofttech.pushlibrary.GCMBaseIntentService;


/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {
	    private static final String TAG = "GCMIntentService";
   // private String SENDER_ID=getApplicationContext().getResources().getString(R.string.gcm_sender_id);

    public GCMIntentService() {
        super("115083367553");
    }

		@Override
		protected void onMessage(Context context, Intent intent) {
			// TODO Auto-generated method stub
			 String message = intent.getExtras().getString("message");
		 //    displayMessage(context, message);
		        // notifies user
		        generateNotification(context, message);
		}

		  private static void generateNotification(Context context, String message) {
			//  int icon = R.drawable.ic_launcher;
		        long when = System.currentTimeMillis();
		    //    NotificationManager notificationManager = (NotificationManager)
		      //          context.getSystemService(Context.NOTIFICATION_SERVICE);
//              Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//		        Notification notification = new Notification(icon, message, when);
//		        String title = context.getString(R.string.app_name);
//
		        Intent notificationIntent = new Intent(context, MainActivity.class);
//		        // set intent so it does not start a new activity
//		        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//		                Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		        PendingIntent intent =
//		                PendingIntent.getActivity(context, 0, notificationIntent, 0);
//		        notification.setLatestEventInfo(context, title, message, intent);
//              notification.
//		        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		        notificationManager.notify(0, notification);

              // this is it, we'll build the notification!
              // in the addAction method, if you don't want any icon, just set the first param to 0

              // define sound URI, the sound to be played when there's a notification
              Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

              // intent triggered, you can add other intent for other actions
              Intent intent = new Intent(context, MainActivity.class);
              PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
              Notification mNotification = new Notification.Builder(context)
                      .setContentTitle(context.getString(R.string.app_name))
                      .setContentText(message)
                      .setSmallIcon(R.mipmap.ic_launcher)
                      .setContentIntent(pIntent)
                      .setSound(soundUri).setAutoCancel(true)
                      .build();

              NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

              // If you want to hide the notification after it was selected, do the code below
              // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

              notificationManager.notify(0, mNotification);
		    }
	}
