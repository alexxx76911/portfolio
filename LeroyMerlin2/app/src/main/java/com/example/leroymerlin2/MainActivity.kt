package com.example.leroymerlin2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.leroymerlin2.ui.theme.LeroyMerlin2Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                bottomBar = { BottomBar() },
                content = { MainContent() },
                topBar = { MakeToolBar() }
            )
        }
    }
}


@Preview
@Composable
fun MainContent() {
    LeroyMerlin2Theme {
        val shapes = MaterialTheme.shapes
        val products = mutableListOf<Product>()
        for (i in 0 until 20) {
            products.add(
                Product(
                    "name $i",
                    "price $i",
                    R.drawable.ic_launcher_foreground
                )
            )
        }

        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp)
            ) {
                Card(
                    modifier = Modifier
                        .size(Dp(100f))
                        .padding(horizontal = Dp(4f)),
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "каталог",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(3.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Image(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.ic_baseline_menu_24),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                }

                for (i in 0 until 7) {
                    when (i) {
                        1 -> MakeCatalogCard(name = "сад")
                        2 -> MakeCatalogCard(name = "освещение")
                        3 -> MakeCatalogCard(name = "инструменты")
                        4 -> MakeCatalogCard(name = "стройматериалы")
                        5 -> MakeCatalogCard(name = "декор")

                    }
                }

                Card(
                    modifier = Modifier
                        .size(Dp(100f))
                        .padding(horizontal = Dp(4f)),
                    backgroundColor = Color.LightGray,
                    shape = shapes.large
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Image(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(id = R.drawable.ic_baseline_navigate_next_24),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(), contentAlignment = Alignment.TopCenter
                        ) {
                            Text(
                                text = "еще",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(3.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
            Offer(productList = products, "ограниченное предложение")
            Offer(productList = products, name = "лучшая цена")
            Spacer(modifier = Modifier.height(30.dp))
        }


    }
}


@Composable
fun Offer(productList: List<Product>, name: String) {
    LeroyMerlin2Theme {
        Column(modifier = Modifier.padding(vertical = 30.dp)) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = name,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp)
            ) {
                for (i in productList) {
                    MakeProductCard(product = i)
                }


            }
        }
    }

}

@Composable
fun MakeCatalogCard(name: String) {
    LeroyMerlin2Theme {

        Card(
            modifier = Modifier
                .size(Dp(100f))
                .padding(horizontal = Dp(4f)),
            backgroundColor = Color.LightGray,
            shape = MaterialTheme.shapes.large
        ) {
            Column(Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(3.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .requiredWidth(105.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Image(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }

        }
    }


}

@Composable
fun MakeProductCard(product: Product) {
    LeroyMerlin2Theme {
        Card(
            modifier = Modifier
                .size(200.dp)
                .padding(horizontal = 4.dp),
            shape = MaterialTheme.shapes.large,
            backgroundColor = Color.LightGray
        ) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = product.price)
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = product.name,
                    style = MaterialTheme.typography.body1
                )

            }

        }
    }

}

@Composable
fun BottomBar() {
    LeroyMerlin2Theme {

        BottomNavigation {
            BottomNavigationItem(
                selected = true,
                onClick = {},
                label = { Text(text = "главная", style = MaterialTheme.typography.body2) },
                icon = { MakeImage(res = R.drawable.ic_baseline_search_24) })

            BottomNavigationItem(
                selected = false,
                onClick = {},
                label = { Text(text = "мой список", style = MaterialTheme.typography.body2) },
                icon = { MakeImage(res = R.drawable.ic_baseline_view_list_24) })

            BottomNavigationItem(
                selected = false,
                onClick = {},
                label = { Text(text = "магазины", style = MaterialTheme.typography.body2) },
                icon = { MakeImage(res = R.drawable.ic_baseline_other_houses_24) })

            BottomNavigationItem(
                selected = false,
                onClick = {},
                label = { Text(text = "профиль", style = MaterialTheme.typography.body2) },
                icon = { MakeImage(res = R.drawable.ic_baseline_account_box_24) })

            BottomNavigationItem(
                selected = false,
                onClick = {},
                label = { Text(text = "корзина", style = MaterialTheme.typography.body2) },
                icon = { MakeImage(res = R.drawable.ic_baseline_shopping_cart_24) })
        }
    }

}

@Composable
fun MakeImage(res: Int) {
    Image(painter = painterResource(id = res), contentDescription = null)

}

@Composable
@Preview
fun MakeToolBar() {
    LeroyMerlin2Theme {
        TopAppBar(modifier = Modifier.height(108.dp), content = {
            Column(Modifier.fillMaxHeight()) {
                Text(text = "Поиск товаров", style = MaterialTheme.typography.h4)
                Row {
                    MakeTextField()
                }
            }
        })
    }


}


@Composable
fun MakeTextField() {
    var text by remember { mutableStateOf("поиск") }

    TextField(
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = Color.Black
        ),
        value = text,
        onValueChange = { text = it }

    )
}

