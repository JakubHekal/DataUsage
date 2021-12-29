package com.jakubhekal.datausage.managers;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

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

    public long getAllBytesMobile(long startTime, long endTime, int byteType) {
        try {
            NetworkStats.Bucket bucket;
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, getSubscriberId(), startTime, endTime);
            return getBytes(bucket, byteType);
        } catch (Exception e) {
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
            return -1;
        }
    }

    public long getPackageBytesMobile(int uid, long startTime, long endTime) {
        return getPackageBytesMobile(uid, startTime, endTime, BYTES_ALL);
    }

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

    private String getSubscriberId() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (PermissionManager.hasPermissionToReadPhoneStats(context)) {
            return tm.getSubscriberId();
        }
        return "";
    }
}
