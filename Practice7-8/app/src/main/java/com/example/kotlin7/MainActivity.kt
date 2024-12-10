package com.example.kotlin7

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MainActivity : AppCompatActivity() {

    // Определение элементов интерфейса
    lateinit var editTextUrl: EditText // Поле для ввода URL
    lateinit var buttonDownload: Button // Кнопка для запуска загрузки
    lateinit var imageView: ImageView // Элемент для отображения загруженного изображения

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация элементов интерфейса
        editTextUrl = findViewById(R.id.editTextUrl)
        buttonDownload = findViewById(R.id.buttonDownload)
        imageView = findViewById(R.id.imageView)

        // Установка слушателя на кнопку для запуска загрузки изображения
        buttonDownload.setOnClickListener {
            val imageUrl = editTextUrl.text.toString() // Получение URL из текстового поля
            downloadAndSaveImage(imageUrl, this) // Вызов функции загрузки и сохранения изображения
        }
    }

    // Функция для загрузки и сохранения изображения
    fun downloadAndSaveImage(imageUrl: String, context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            // Запуск асинхронной загрузки изображения
            val bitmapDeferred = downloadImage(imageUrl)
            val bitmap = bitmapDeferred.await() // Ожидание завершения загрузки
            if (bitmap != null) {
                // Установка изображения в ImageView и сохранение на диск
                imageView.setImageBitmap(bitmap)
                saveImageToDisk(bitmap, context)
            } else {
                // Отображение сообщения об ошибке загрузки
                Toast.makeText(context, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Функция для загрузки изображения с заданного URL
    fun downloadImage(imageUrl: String): Deferred<Bitmap?> {
        return CoroutineScope(Dispatchers.IO).async {
            try {
                val url = URL(imageUrl) // Создание объекта URL
                val connection = url.openConnection() // Открытие соединения
                connection.doInput = true // Разрешение на входные данные
                connection.connect() // Установка соединения
                val input = connection.getInputStream() // Получение потока данных
                BitmapFactory.decodeStream(input) // Декодирование изображения из потока
            } catch (e: Exception) {
                e.printStackTrace()
                null // Возврат null в случае ошибки
            }
        }
    }

    // Функция для сохранения изображения на диск
    fun saveImageToDisk(bitmap: Bitmap, context: Context): Job {
        return CoroutineScope(Dispatchers.IO).async {
            try {
                // Определение пути для сохранения изображения
                val file = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "downloaded_image.jpg"
                )
                // Сохранение изображения в формате JPEG
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                }
                // Отображение сообщения об успешном сохранении
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Изображение сохранено",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Отображение сообщения об ошибке сохранения
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Ошибка сохранения изображения", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
