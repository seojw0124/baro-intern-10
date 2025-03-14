package com.jeongu.barointernapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeongu.barointernapp.detail.ProductDetailScreen
import com.jeongu.barointernapp.home.HomeScreen
import com.jeongu.barointernapp.ui.theme.BaroInternAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            BaroInternAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "product_list") {
                    composable("product_list") {
                        HomeScreen(navController = navController)
                    }
                    composable("product_detail/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                        ProductDetailScreen(productId = productId)
                    }
                }
            }
        }
    }
}