package com.example.practice4

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    // View для отображения предварительного просмотра камеры
    private lateinit var previewView: PreviewView

    // Executor для выполнения задач камеры в фоновом потоке
    private lateinit var cameraExecutor: ExecutorService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        previewView = view.findViewById(R.id.previewView)
        val captureButton = view.findViewById<Button>(R.id.button_capture)
        val navigateButton = view.findViewById<Button>(R.id.button_to_list)
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Установка обработчика клика для кнопки захвата фото
        captureButton.setOnClickListener {
            takePhoto()
        }

        // Обработчик клика для перехода на фрагмент списка фотографий
        navigateButton.setOnClickListener {
            findNavController().navigate(R.id.action_cameraFragment_to_listFragment)
        }

        // Проверка, есть ли у приложения разрешение на использование камеры
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            // Если разрешений нет, запрашиваются необходимые разрешения
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        return view
    }

    // Метод для запуска камеры
    private fun startCamera() {
        // Получение CameraProvider для управления камерой
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        // Добавляем слушатель для инициализации камеры, когда CameraProvider будет готов
        cameraProviderFuture.addListener({
            // Получение экземпляра CameraProvider
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Настройка предварительного просмотра камеры
            val preview = Preview.Builder().build().also {
                // Привязка Preview к PreviewView для отображения изображения
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Выбор задней камеры по умолчанию
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Отключение всех предыдущих привязок камеры перед настройкой новой
                cameraProvider.unbindAll()

                // Привязка жизненного цикла фрагмента к камере, чтобы камера отключалась при уничтожении фрагмента
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (exc: Exception) {
                // Логирование ошибки, если произошла ошибка при настройке камеры
                Log.e("CameraFragment", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext())) // Запуск на основном потоке
    }

    // Метод для захвата фотографии
    private fun takePhoto() {
        val timestamp = System.currentTimeMillis()
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(timestamp))

        // Ссоздание директории для фотографий
        val photoDir = File(requireContext().filesDir, "photos")
        if (!photoDir.exists()) {
            photoDir.mkdir()
        }

        // Создание файла для записи времени захвата фотографий
        val file = File(photoDir, "date.txt")
        file.appendText("$date\n")
    }

    // Метод для проверки, все ли необходимые разрешения предоставлены
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
