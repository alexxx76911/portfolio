package com.example.homework

class CardRepository {

    private val cardList = listOf(
            CardObject(
                    0,
                    "Michael Kors Men's Quartz Watch",
                    "https://m.media-amazon.com/images/I/81b63Ck2AiL._AC_UL320_.jpg",
                    "151$",
                    "description 1"
            ),
            CardObject(
                    1,
                    "Michael Kors Men's Quartz Watch",
                    "https://m.media-amazon.com/images/I/81b63Ck2AiL._AC_UL320_.jpg",
                    "200$",
                    "description 2"
            ),

            CardObject(
                    2,
                    "Michael Kors Men's Quartz Watch",
                    "https://m.media-amazon.com/images/I/81b63Ck2AiL._AC_UL320_.jpg",
                    "270$",
                    "description 3"
            ),

            CardObject(
                    3,
                    "Michael Kors Men's Quartz Watch",
                    "https://m.media-amazon.com/images/I/81b63Ck2AiL._AC_UL320_.jpg",
                    "490$",
                    "description 4"
            ),

            CardObject(
                    4,
                    "Michael Kors Men's Quartz Watch",
                    "https://m.media-amazon.com/images/I/81b63Ck2AiL._AC_UL320_.jpg",
                    "345$",
                    "description 5"
            ),

            CardObject(
                    5,
                    "Michael Kors Men's Quartz Watch",
                    "https://m.media-amazon.com/images/I/81b63Ck2AiL._AC_UL320_.jpg",
                    "765$",
                    "description 6"
            )
    )


    fun getCards(): List<CardObject> {
        return cardList
    }

    fun getCardById(id: Int): CardObject {
        return cardList[id]
    }
}