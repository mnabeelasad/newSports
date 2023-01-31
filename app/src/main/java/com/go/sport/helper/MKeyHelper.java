package com.go.sport.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.go.sport.BuildConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MKeyHelper {

    private static final String TAG = "MKeyHelper";

    /**
     * @param key string like: SHA1, SHA256, MD5.
     */
    @SuppressLint("PackageManagerGetSignatures") // test purpose
    public static void get(Context context, String key) {
        try {
            final PackageInfo info = context.getPackageManager()
                    .getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance(key);
                md.update(signature.toByteArray());

                final byte[] digest = md.digest();
                final StringBuilder toRet = new StringBuilder();
                for (int i = 0; i < digest.length; i++) {
                    if (i != 0) toRet.append(":");
                    int b = digest[i] & 0xff;
                    String hex = Integer.toHexString(b);
                    if (hex.length() == 1) toRet.append("0");
                    toRet.append(hex);
                }

                Log.d(TAG, key + " " + toRet.toString());
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e(TAG, "name not found " + e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "no such an algorithm " + e.toString());
        } catch (Exception e) {
            Log.e(TAG, "exception " + e.toString());
        }
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.d(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
}
