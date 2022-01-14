package com.jakubhekal.datausage.managers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.jakubhekal.datausage.Utils;

import java.util.HashMap;
import java.util.Map;

public class NetworkUsageManager {

    Context context;
    NetworkStatsManager networkStatsManager;

    public static final int BYTES_RX = 0;
    public static final int BYTES_TX = 1;
    public static final int BYTES_ALL = 2;

    public NetworkUsageManager(Context context) {
        this.context = context;
        this.networkStatsManager = (NetworkStatsManager) this.context.getSystemService(Context.NETWORK_STATS_SERVICE);
    }

    /*public long getAllBytesMobile(long startTime, long endTime, int byteType) {
        return getPackageBytesMobile(-1, startTime, endTime, byteType);
    }

    public long getAllBytesMobile(long startTime, long endTime) {
        return getAllBytesMobile(startTime, endTime, BYTES_ALL);
    }

    public long getPackageBytesMobile(int uid, long startTime, long endTime, int byteType) {
        try {
            NetworkStats networkStats;
            networkStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, null, startTime, endTime);
            long bytes = 0;
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            while (networkStats.hasNextBucket()) {
                networkStats.getNextBucket(bucket);
                if(uid == bucket.getUid() || uid == -1) {
                    Log.e("RUNTIME_BUCKET", "UID: " + bucket.getUid() + ", RX: " + Utils.convertFromBytes(bucket.getRxBytes()) + ", TX: " + Utils.convertFromBytes(bucket.getRxBytes()) + ", AX: " + Utils.convertFromBytes(bucket.getRxBytes() + bucket.getTxBytes()));
                    bytes += getBytes(bucket, byteType);
                }
            }
            networkStats.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long getPackageBytesMobile(int uid, long startTime, long endTime) {
        return getPackageBytesMobile(uid, startTime, endTime, BYTES_ALL);
    }*/

    private long getBytes(NetworkStats.Bucket bucket, int byteType) {
        switch (byteType) {
            case BYTES_RX:
                return bucket.getRxBytes();

            case BYTES_TX:
                return bucket.getTxBytes();

            case BYTES_ALL:
                return bucket.getRxBytes() + bucket.getTxBytes();
        }

        return 0;
    }

    public long getAllBytesMobile(long startTime, long endTime, int byteType) {
        try {
            NetworkStats.Bucket bucket;
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, null, startTime, endTime);
            return getBytes(bucket, byteType);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long getAllBytesMobile(long startTime, long endTime) {
        return getAllBytesMobile(startTime, endTime, BYTES_ALL);
    }

    public long getPackageBytesMobile(int uid, long startTime, long endTime, int byteType) {
        try {
            NetworkStats networkStats;
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, getSubscriberId(), startTime, endTime, uid);

            long bytes = 0;
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            while (networkStats.hasNextBucket()) {
                networkStats.getNextBucket(bucket);
                bytes += getBytes(bucket, byteType);
            }
            networkStats.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long getPackageBytesMobile(int uid, long startTime, long endTime) {
        return getPackageBytesMobile(uid, startTime, endTime, BYTES_ALL);
    }

    @SuppressLint("MissingPermission")
    private String getSubscriberId() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (PermissionManager.hasPermissionToReadPhoneStats(context)) {
            return tm.getSubscriberId();
        }
        return "";
    }
}
