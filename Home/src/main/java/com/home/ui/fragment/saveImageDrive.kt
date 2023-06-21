package com.home.ui.fragmentimport

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.home.ui.fragmentimport.SaveUtilImg.Companion.buffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Arrays

class SaveUtilImg(){
companion object{
    private val sizeInBytes = 2 * 1024 * 1024
    var buffer = ByteArray(sizeInBytes)
}
}
suspend fun saveImageToFirebaseStorage(imageUrl: String, fileName: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = BufferedInputStream(connection.inputStream)
            val data = ByteArrayOutputStream()
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                data.write(buffer, 0, bytesRead)
                Arrays.fill(buffer, 0.toByte()) // Limpa a memória do buffer
            }
            data.flush()

            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val imageRef: StorageReference = storageRef.child(fileName)
            imageRef.putStream(data.toByteArray().inputStream()).await()

            input.close()
            data.close()
            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }
}
