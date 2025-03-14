package com.jeongu.barointernapp

object Storage {

    val productList = listOf(
        Product(
            id = 1,
            name = "Product 1",
            price = 1000,
            description = "This is product 1",
            imageUrl = "https://picsum.photos/200/300"
        ),
        Product(
            id = 2,
            name = "Product 2",
            price = 2000,
            description = "This is product 2",
            imageUrl = "https://picsum.photos/200/300"
        ),
        Product(
            id = 3,
            name = "Product 3",
            price = 3000,
            description = "This is product 3",
            imageUrl = "https://picsum.photos/200/300"
        ),
        Product(
            id = 4,
            name = "Product 4",
            price = 4000,
            description = "This is product 4",
            imageUrl = "https://picsum.photos/200/300"
        ),
        Product(
            id = 5,
            name = "Product 5",
            price = 5000,
            description = "This is product 5",
            imageUrl = "https://picsum.photos/200/300"
        ),
        Product(
            id = 6,
            name = "Product 6",
            price = 6000,
            description = "This is product 6",
            imageUrl = "https://picsum.photos/200/300"
        ),
        Product(
            id = 7,
            name = "Product 7",
            price = 7000,
            description = "This is product 7",
            imageUrl = "https://picsum.photos/200/300"
        ),
        Product(
            id = 8,
            name = "Product 8",
            price = 8000,
            description = "This is product 8",
            imageUrl = "https://picsum.photos/200/300"
        ),
        Product(
            id = 9,
            name = "Product 9",
            price = 9000,
            description = "This is product 9",
            imageUrl = "https://picsum.photos/200/300"
        ),
        Product(
            id = 10,
            name = "Product 10",
            price = 10000,
            description = "This is product 10",
            imageUrl = "https://picsum.photos/200/300"
        )
    )

    fun getProductById(id: Int): Product? {
        return productList.find { it.id == id }
    }
}

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    val imageUrl: String
)