package com.home.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.home.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

// Função para baixar a mídia do URL e compartilhar o arquivo
fun compartilharMidia(url: String, context: Context) {
    // Usar uma thread separada para executar operações de rede
    Thread {
        try {
            // Fazer o download do arquivo
            val file = downloadFile(url, context)

            // Verificar se o arquivo foi baixado com sucesso
            if (file != null) {
                // Obter o tipo de mídia do arquivo
                val type = getMimeTypeFromExtension(getFileExtension(url))

                // Criar um URI para o arquivo
                val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)

                // Criar uma intent de compartilhamento
                val intent = Intent(Intent.ACTION_SEND)

                // Definir o tipo de conteúdo da intent
                intent.type = type

                // Adicionar o URI do arquivo como conteúdo extra
                intent.putExtra(Intent.EXTRA_STREAM, uri)

                // Adicionar a flag de permissão de leitura ao URI
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                // Iniciar a intent de compartilhamento
                context.startActivity(Intent.createChooser(intent, "Compartilhar mídia"))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }.start()
}

// Função para baixar o arquivo da URL e retornar o arquivo baixado
@Throws(IOException::class)
private fun downloadFile(urlString: String, context: Context): File? {
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connect()

    val inputStream: InputStream = connection.inputStream

    // Obter o nome do arquivo a partir da URL
    val fileName = getFileName(urlString)

    // Verificar a extensão do arquivo para determinar o tipo MIME
    val type = getMimeTypeFromExtension(getFileExtension(urlString))

    // Criar o arquivo para salvar a mídia
    val mediaFile = File(context.getExternalFilesDir(null), "meme" +"."+ type!!.split("/")[1])

    // Copiar o conteúdo do InputStream para o arquivo de mídia
    val outputStream: OutputStream = FileOutputStream(mediaFile)
    val buffer = ByteArray(4 * 1024) // 4KB buffer
    var bytesRead: Int
    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
        outputStream.write(buffer, 0, bytesRead)
    }
    outputStream.flush()

    // Fechar os streams
    outputStream.close()
    inputStream.close()

    return mediaFile
}

// Função para obter o nome do arquivo a partir da URL
private fun getFileName(urlString: String): String {
    val uri = Uri.parse(urlString)
    return uri.lastPathSegment ?: ""
}

// Função para obter a extensão do arquivo
 fun getFileExtension(fileName: String): String {
    val dotIndex = fileName.lastIndexOf('.')
    return if (dotIndex != -1) fileName.substring(dotIndex + 1) else ""
}

// Função para obter o tipo MIME da mídia com base na extensão do arquivo
 fun getMimeTypeFromExtension(extension: String): String? {
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase())
}

// Função para obter o tipo MIME da mídia com base no arquivo
private fun getMimeType(file: File, context: Context): String? {
    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    return context.contentResolver.getType(uri)
}


