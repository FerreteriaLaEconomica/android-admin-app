package com.smontiel.ferretera.admin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.reflect.TypeToken;
import com.smontiel.ferretera.admin.data.models.Inventario;
import com.smontiel.ferretera.admin.data.models.Sucursal;
import com.smontiel.ferretera.admin.data.network.ApiClient;
import com.smontiel.ferretera.admin.features.update_inventory.UpdateInventoryActivity;

import java.lang.reflect.Type;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Salvador Montiel on 24/11/18.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private ApiClient apiClient;

    public MyFirebaseMessagingService() {
        super();
        this.apiClient = Injector.provideApiClient();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String body = remoteMessage.getNotification().getBody();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Inventario inventario = Injector.provideGson().fromJson(body, Inventario.class);

        apiClient.getSucursalById(inventario.idSucursal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful())
                        showNotification(remoteMessage.getNotification().getTitle(), inventario, response.body());
                }, Timber::e);
    }

    private void showNotification(String title, Inventario inventario, Sucursal sucursal) {
        Intent notifyIntent = UpdateInventoryActivity.getStartIntent(this, inventario, sucursal);
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        String contentText = "Sólo quedan " + inventario.cantidad + " unidades de " + inventario.producto.nombre
                + " en la sucursal " + sucursal.nombre + ".";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle())
                //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(notifyPendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
