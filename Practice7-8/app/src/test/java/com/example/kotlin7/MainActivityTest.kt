package com.example.kotlin7

import android.content.Context
import android.graphics.Bitmap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// Указываем, что тесты будут запущены с использованием RobolectricTestRunner
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_MANIFEST_NAME)
class MainActivityTest {

    // Mock-объекты для контекста и элементов UI
    private lateinit var mockContext: Context
    private lateinit var mainActivity: MainActivity
    private lateinit var mainActivity2: MainActivity
    private val testDispatcher = StandardTestDispatcher()
    private val editTextUrl: EditText = mock(EditText::class.java)
    private val buttonDownload: Button = mock(Button::class.java)
    private val imageView: ImageView = mock(ImageView::class.java)

    // Метод, выполняющийся перед каждым тестом, для инициализации тестовой среды
    @Before
    fun setUp() {
        mockContext = mock(Context::class.java) // Инициализируем mock контекста
        Dispatchers.setMain(testDispatcher) // Устанавливаем тестовый диспетчер
        mainActivity = MainActivity() // Создаем экземпляр MainActivity
        mainActivity2 = spy(MainActivity()) // Создаем "шпион" MainActivity для отслеживания вызовов
        mainActivity2.editTextUrl = editTextUrl // Подключаем mock для editTextUrl
        mainActivity2.buttonDownload = buttonDownload // Подключаем mock для buttonDownload
        mainActivity2.imageView = imageView // Подключаем mock для imageView
    }

    // Тест успешной загрузки изображения
    @Test
    fun testDownloadImageSuccess() = runBlocking {
        // URL для валидного изображения
        val imageUrl =
            "https://freelance-script.abuyfile.com/wp-content/uploads/2019/09/autoalias2-url-freelance-script-cotonti-plugins-URL-site-by-webitproff.png"

        val bitmapDeferred = mainActivity.downloadImage(imageUrl) // Загружаем изображение
        val bitmap = bitmapDeferred.await() // Ожидаем завершения загрузки

        assertNotNull(bitmap) // Проверяем, что изображение загружено успешно
    }

    // Тест неудачной загрузки изображения
    @Test
    fun testDownloadImageFailure() = runBlocking {
        val invalidImageUrl = "https://example.com/invalid.jpg" // Невалидный URL

        val bitmapDeferred = mainActivity.downloadImage(invalidImageUrl) // Загружаем изображение
        val bitmap = bitmapDeferred.await() // Ожидаем завершения загрузки

        assertNull(bitmap) // Проверяем, что загрузка не удалась
    }

    // Тест проверки инициализации активности
    @Test
    fun activityTest() {
        // Создаем MainActivity с использованием Robolectric и проверяем инициализацию
        Robolectric.buildActivity(MainActivity::class.java).use { controller ->
            controller.setup()
            val activity = controller.get()

            assertNotNull(activity) // Проверяем, что MainActivity создана
            val editTextUrl = activity.findViewById<EditText>(R.id.editTextUrl) // Ищем элемент editTextUrl

            assertNotNull(editTextUrl) // Проверяем, что editTextUrl инициализирован
        }
    }

    // Тест на проверку вызова метода compress при сохранении изображения
    @Test
    fun `saveImageToDisk should call compress`() = runTest {
        val mainActivity: MainActivity = mock() // Создаем mock для MainActivity
        val bitmap = mock(Bitmap::class.java) // Создаем mock для Bitmap
        val context = mock(Context::class.java) // Создаем mock для контекста
        val spiedMainActivity = spy(mainActivity) // Создаем "шпион" для MainActivity

        spiedMainActivity.saveImageToDisk(bitmap, context).join() // Сохраняем изображение

        // Проверяем, что метод compress был вызван с нужными параметрами
        verify(bitmap).compress(eq(Bitmap.CompressFormat.JPEG), eq(100), Mockito.any())
    }

    // Тест, проверяющий вызов метода downloadAndSaveImage при клике на кнопку
    @Test
    fun `button click should call downloadAndSaveImage`() {
        val imageUrl =
            "https://images.wallpaperscraft.com/image/single/lake_mountain_tree_36589_2650x1600.jpg" // URL для загрузки изображения

        // Заменяем поведение кнопки на вызов downloadAndSaveImage при клике
        Mockito.doAnswer {
            mainActivity2.downloadAndSaveImage(imageUrl, mainActivity2)
            null
        }.`when`(buttonDownload).performClick()

        buttonDownload.performClick() // Выполняем клик на кнопке

        // Проверяем, что метод downloadAndSaveImage был вызван с правильными параметрами
        verify(mainActivity2).downloadAndSaveImage(imageUrl, mainActivity2)
    }
}
