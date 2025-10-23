package com.example.shoppingapp

import java.util.Locale


fun toCap(s:String) : String{
    return s.replace('_',' ').lowercase().replaceFirstChar { it.uppercase() }
}

val categories = listOf(
    "Outdoor",
    "Garden",
    "Clothing",
    "Automotive",
    "Electronics",
    "Home Appliances",
    "Sports Equipment",
    "Beauty Products",
    "Toys",
    "Books",
    "Furniture",
    "Groceries",
    "Health & Wellness",
    "Jewelry",
    "Pet Supplies",
    "Music & Instruments"
)
enum class Category(val description: String) {
    OUTDOOR("Products for outdoor activities, such as camping gear or hiking accessories."),
    GARDEN("Items related to gardening, including tools, seeds, and plants."),
    CLOTHING("Apparel for men, women, and children, covering all seasons and styles."),
    AUTOMOTIVE("Car accessories, maintenance tools, and spare parts."),
    ELECTRONICS("Devices such as phones, laptops, and home entertainment systems."),
    HOME_APPLIANCES("Appliances for home use, including kitchen gadgets and cleaning devices."),
    SPORTS_EQUIPMENT("Gear for various sports, like bikes, weights, and exercise equipment."),
    BEAUTY_PRODUCTS("Skincare, makeup, and personal care items."),
    TOYS("Play items for children of all ages, including educational toys."),
    BOOKS("Literature across genres, including novels, non-fiction, and children's books."),
    FURNITURE("Items for home or office, including tables, chairs, and storage solutions."),
    GROCERIES("Food and household essentials, including fresh produce and pantry staples."),
    HEALTH_WELLNESS("Products promoting physical health, including supplements and fitness gear."),
    JEWELRY("Decorative items made of precious materials, including rings, necklaces, and bracelets."),
    PET_SUPPLIES("Products for pets, such as food, toys, and grooming items."),
    MUSIC_INSTRUMENTS("Equipment related to music, including instruments and accessories.");

    val enam:String
        get() = toCap(this.name)

}