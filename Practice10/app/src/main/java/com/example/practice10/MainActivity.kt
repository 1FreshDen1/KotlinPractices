package com.example.practice10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.practice10.ui.theme.Practice10Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practice10Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    // Стейт для хранения введенного URL
    var url by remember { mutableStateOf(TextFieldValue("")) }
    // Список загруженных изображений
    var imageList by remember { mutableStateOf(listOf<String>()) }
    // Стейт для отображения статуса загрузки
    var isLoading by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ввод URL
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            BasicTextField(
                value = url,
                onValueChange = { url = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .border(1.dp, Color.Gray)
                    .padding(16.dp)
            )
        }

        // Кнопка для загрузки изображения
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (url.text.isNotEmpty()) {
                    isLoading = true
                    // Добавление URL в список изображений
                    imageList = imageList + url.text
                    // Очистка поля ввода после загрузки
                    url = TextFieldValue("")
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Загрузить изображение")
        }

        // Отображение изображения
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(imageList.size) { index ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            val painter = rememberImagePainter(data = imageList[index])

                            // Отображаем изображение
                            Image(
                                painter = painter,
                                contentDescription = "Загруженное изображение",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Practice10Theme {
        MainScreen()
    }
}
