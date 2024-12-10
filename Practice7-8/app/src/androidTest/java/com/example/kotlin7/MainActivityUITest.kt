package com.example.kotlin7

import android.os.Handler
import android.os.Looper
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Аннотация для запуска тестов с использованием AndroidJUnit4
@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    // Правило для запуска MainActivity перед выполнением тестов
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // Тест проверки видимости элементов UI
    @Test
    fun checkUiElementsVisibility() {
        onView(withId(R.id.editTextUrl)).check(matches(isDisplayed())) // Проверка видимости поля для URL
        onView(withId(R.id.buttonDownload)).check(matches(isDisplayed())) // Проверка видимости кнопки загрузки
    }

    // Тест проверки начального состояния поля ввода
    @Test
    fun checkEditTextInitialState() {
        onView(withId(R.id.editTextUrl)).check(matches(withText(""))) // Проверка, что текстовое поле пустое при запуске
    }

    // Тест проверки текста на кнопке
    @Test
    fun checkButtonText() {
        onView(withId(R.id.buttonDownload)).check(matches(withText("Загрузить изображение"))) // Проверка текста на кнопке
    }

    // Тест проверки нажатия на кнопку и отображения сообщения об ошибке
    @Test
    fun checkButtonClick() {
        // Ввод невалидного URL в текстовое поле
        onView(withId(R.id.editTextUrl)).perform(typeText("https://example.com/invalid.jpg"), closeSoftKeyboard())

        // Нажатие на кнопку для загрузки изображения
        onView(withId(R.id.buttonDownload)).perform(click())

        // Задержка для ожидания завершения загрузки
        Handler(Looper.getMainLooper()).postDelayed({
            activityRule.scenario.onActivity { activity ->
                // Проверка отображения Toast-сообщения об ошибке
                onView(withText("Ошибка загрузки изображения"))
                    .inRoot(RootMatchers.withDecorView(not(activity.window.decorView)))
                    .check(matches(isDisplayed()))
            }
        }, 1000)
    }

    // Тест проверки отображения изображения после успешной загрузки
    @Test
    fun checkImageViewDisplayAfterDownload() {
        // Ввод валидного URL изображения в текстовое поле
        onView(withId(R.id.editTextUrl)).perform(
            typeText("https://images.wallpaperscraft.com/image/single/lake_mountain_tree_36589_2650x1600.jpg"),
            closeSoftKeyboard()
        )

        // Нажатие на кнопку для загрузки изображения
        onView(withId(R.id.buttonDownload)).perform(click())

        // Задержка для ожидания завершения загрузки изображения
        Handler(Looper.getMainLooper()).postDelayed({
            // Проверка, что изображение отображается в ImageView
            onView(withId(R.id.imageView)).check(matches(isDisplayed()))
        }, 3000)
    }
}
