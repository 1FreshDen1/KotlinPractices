package com.example.pr_5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.pr_5.ProductApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.tv)
        val b = findViewById<Button>(R.id.button)
        val buttonList = findViewById<Button>(R.id.buttonList)

        db = AppDatabase.getDatabase(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val productApi = retrofit.create(ProductApi::class.java)

        b.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val product = productApi.getProductById(1)

                // Сохранение в базу данных
                val productEntity = Product(
                    id = product.id,
                    title = product.title,
                    description = product.description,
                    price = product.price,
                    discountPercentage = product.discountPercentage,
                    rating = product.rating,
                    stock = product.stock,
                    brand = product.brand,
                    category = product.category,
                    thumbnail = product.thumbnail,
                    availabilityStatus = product.availabilityStatus,
                    dimensions = product.dimensions,
                    images = product.images,
                    meta = product.meta,
                    minimumOrderQuantity = product.minimumOrderQuantity,
                    returnPolicy = product.returnPolicy,
                    reviews = product.reviews,
                    shippingInformation = product.shippingInformation,
                    sku = product.sku,
                    tags = product.tags,
                    warrantyInformation = product.warrantyInformation,
                    weight = product.weight)
                db.productDao().insertProduct(productEntity)

                runOnUiThread {
                    tv.text = product.title
                }
            }
        }
        buttonList.setOnClickListener {
            startActivity(Intent(this, ProductListActivity::class.java))
        }

    }
}
