package com.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.UnknownHostException
import androidx.browser.customtabs.CustomTabsIntent
import java.io.File

suspend fun isNetworkConnected(): Boolean =
    try {
        withContext(IO) {
            val address = InetAddress.getByName("www.google.com")
            address.hostName.isNotEmpty()
        }
    } catch (e: UnknownHostException) {
        false
    }

fun Context.openExternalUrl(url: String) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}

fun Context.getImageOutputUri(fileName: String, mimeType: String): Uri?{
    var outputFileUri: Uri? = null
    val getImage = externalCacheDir
    if(getImage != null){
        outputFileUri = Uri.fromFile(File(getImage.path, "$fileName.$mimeType"))
    }
    return outputFileUri
}

fun Context.getImageUri(data: Intent?): Uri?{
    return data?.data
}