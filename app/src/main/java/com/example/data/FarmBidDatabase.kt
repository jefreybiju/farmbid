package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        ListingEntity::class,
        BidEntity::class,
        OrderEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class FarmBidDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun listingDao(): ListingDao
    abstract fun bidDao(): BidDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: FarmBidDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FarmBidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FarmBidDatabase::class.java,
                    "farmbid_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        seedDatabase(database)
                    }
                }
            }
        }

        suspend fun seedDatabase(database: FarmBidDatabase) {
            val userDao = database.userDao()
            val listingDao = database.listingDao()
            val bidDao = database.bidDao()
            val orderDao = database.orderDao()

            userDao.clearAll()
            listingDao.clearAll()
            bidDao.clearAll()
            orderDao.clearAll()

            // Seed Farmers with rich profiles, certifications, and produce offerings
            val u1 = userDao.insertUser(
                UserEntity(
                    name = "Anil Kumar",
                    role = "farmer",
                    location = "Kothamangalam, Ernakulam",
                    businessType = "Organic Vegetable Grower",
                    isVerified = true,
                    phone = "+91 98471 22340",
                    certifications = "Certified Organic (NPOP), Jaivik Bharat",
                    produceOffered = "Tomatoes, Okra, Brinjal, Beans, Snake Gourd",
                    farmSizeAcres = 4.5,
                    rating = 4.9,
                    experienceYears = 12,
                    pinCode = "686691",
                    username = "farmer_anil",
                    password = "farm123"
                )
            )
            val u2 = userDao.insertUser(
                UserEntity(
                    name = "Mariamma Joseph",
                    role = "farmer",
                    location = "Munnar, Idukki",
                    businessType = "Highland Fruit & Spice Plantation",
                    isVerified = true,
                    phone = "+91 94462 89100",
                    certifications = "Certified Organic (NPOP), Fair Trade, GAP Certified",
                    produceOffered = "Pineapples, Cardamom, Ginger, Pepper, Passion Fruit",
                    farmSizeAcres = 7.0,
                    rating = 4.95,
                    experienceYears = 18,
                    pinCode = "685612",
                    username = "farmer_mariamma",
                    password = "farm123"
                )
            )
            val u3 = userDao.insertUser(
                UserEntity(
                    name = "Suresh Nair",
                    role = "farmer",
                    location = "Palakkad",
                    businessType = "Banana & Vegetable Grower",
                    isVerified = true,
                    phone = "+91 97455 31200",
                    certifications = "Good Agricultural Practices (GAP), SPNF Natural",
                    produceOffered = "Nendran Bananas, Green Chillies, Tapioca, Coconut",
                    farmSizeAcres = 6.2,
                    rating = 4.8,
                    experienceYears = 9,
                    pinCode = "678001",
                    username = "farmer_suresh",
                    password = "farm123"
                )
            )
            val u4 = userDao.insertUser(
                UserEntity(
                    name = "Mathew Kurian",
                    role = "farmer",
                    location = "Kalpetta, Wayanad",
                    businessType = "Bio-Dynamic Spice & Coffee Estate",
                    isVerified = true,
                    phone = "+91 98472 88771",
                    certifications = "Certified Organic (NPOP), Jaivik Bharat, Export Grade",
                    produceOffered = "Black Pepper, Turmeric, Nutmeg, Robusta Coffee",
                    farmSizeAcres = 10.5,
                    rating = 5.0,
                    experienceYears = 22,
                    pinCode = "673121",
                    username = "farmer_mathew",
                    password = "farm123"
                )
            )
            val u5 = userDao.insertUser(
                UserEntity(
                    name = "Lakshmi Devi",
                    role = "farmer",
                    location = "Kuttanad, Alappuzha",
                    businessType = "Wetland Heritage & Tubers Farm",
                    isVerified = true,
                    phone = "+91 94460 77112",
                    certifications = "Zero Budget Natural, Traditional Heritage",
                    produceOffered = "Yam, Sweet Potato, Plantain, Pokkali Rice",
                    farmSizeAcres = 3.8,
                    rating = 4.85,
                    experienceYears = 15,
                    pinCode = "688504",
                    username = "farmer_lakshmi",
                    password = "farm123"
                )
            )

            // Seed Buyers
            val b1 = userDao.insertUser(
                UserEntity(
                    name = "Fresh Basket Supermarket",
                    role = "buyer",
                    location = "Kochi, Ernakulam",
                    businessType = "Supermarket Chain",
                    isVerified = true,
                    phone = "+91 98950 44551",
                    gstOrLicense = "32AABCF1234F1Z8",
                    username = "buyer_fresh",
                    password = "buyer123"
                )
            )
            val b2 = userDao.insertUser(
                UserEntity(
                    name = "Hotel Sagar Restaurant",
                    role = "buyer",
                    location = "Marine Drive, Kochi",
                    businessType = "Fine Dine Restaurant",
                    isVerified = true,
                    phone = "+91 94471 67890",
                    gstOrLicense = "32AACCH9876E1Z2",
                    username = "buyer_sagar",
                    password = "buyer123"
                )
            )
            val b3 = userDao.insertUser(
                UserEntity(
                    name = "Malabar Wholesale Traders",
                    role = "buyer",
                    location = "Thrissur Mandi",
                    businessType = "Wholesale Mandi",
                    isVerified = true,
                    phone = "+91 98460 11223",
                    gstOrLicense = "32AABMT5544B1Z9",
                    username = "buyer_malabar",
                    password = "buyer123"
                )
            )
            val b4 = userDao.insertUser(
                UserEntity(
                    name = "Green Mart Grocery",
                    role = "buyer",
                    location = "Kakkanad, Kochi",
                    businessType = "Retail Grocery Store",
                    isVerified = true,
                    phone = "+91 98953 77889",
                    gstOrLicense = "32AABGM8899C1Z4",
                    username = "buyer_green",
                    password = "buyer123"
                )
            )
            val b5 = userDao.insertUser(
                UserEntity(
                    name = "New Corner Shop",
                    role = "buyer",
                    location = "Aluva",
                    businessType = "Neighborhood Mart",
                    isVerified = false,
                    phone = "+91 97444 88990",
                    gstOrLicense = "Pending Application",
                    username = "buyer_corner",
                    password = "buyer123"
                )
            )

            val now = System.currentTimeMillis() / 1000

            // Seed Listings
            val l1 = listingDao.insertListing(
                ListingEntity(
                    farmerId = u1,
                    farmerName = "Anil Kumar",
                    farmerVerified = true,
                    produce = "Tomatoes (Country / Nattu)",
                    grade = "A",
                    quantityKg = 100.0,
                    harvestDate = "Today, 6:00 AM",
                    location = "Kothamangalam, Ernakulam",
                    description = "Certified Organic, firm deep red tomatoes, hand-picked at dawn. Spotless texture and uniform sizing.",
                    reservePrice = 20.0,
                    startPrice = 12.0,
                    endsAt = now + 45 * 60,
                    createdAt = now - 600,
                    status = "open",
                    topPrice = 26.0,
                    topBidderId = b2,
                    topBidderName = "Hotel Sagar Restaurant",
                    bidCount = 3
                )
            )

            val l2 = listingDao.insertListing(
                ListingEntity(
                    farmerId = u3,
                    farmerName = "Suresh Nair",
                    farmerVerified = true,
                    produce = "Nendran Bananas",
                    grade = "A",
                    quantityKg = 250.0,
                    harvestDate = "Yesterday evening",
                    location = "Palakkad",
                    description = "Full golden bunches, heavy yield, ready to naturally ripen in 3 to 4 days.",
                    reservePrice = 38.0,
                    startPrice = 24.0,
                    endsAt = now + 90 * 60,
                    createdAt = now - 1200,
                    status = "open",
                    topPrice = 39.0,
                    topBidderId = b1,
                    topBidderName = "Fresh Basket Supermarket",
                    bidCount = 2
                )
            )

            val l3 = listingDao.insertListing(
                ListingEntity(
                    farmerId = u2,
                    farmerName = "Mariamma Joseph",
                    farmerVerified = true,
                    produce = "Pineapples (Mauritius)",
                    grade = "B",
                    quantityKg = 80.0,
                    harvestDate = "This morning",
                    location = "Munnar, Idukki",
                    description = "Sweet, fragrant mountain Mauritius pineapple crop, GAP certified with high sugar content.",
                    reservePrice = 30.0,
                    startPrice = 18.0,
                    endsAt = now + 20 * 60,
                    createdAt = now - 900,
                    status = "open",
                    topPrice = 26.0,
                    topBidderId = b4,
                    topBidderName = "Green Mart Grocery",
                    bidCount = 1
                )
            )

            val l4 = listingDao.insertListing(
                ListingEntity(
                    farmerId = u1,
                    farmerName = "Anil Kumar",
                    farmerVerified = true,
                    produce = "Okra / Ladies Finger",
                    grade = "A",
                    quantityKg = 40.0,
                    harvestDate = "Today, 5:30 AM",
                    location = "Kothamangalam, Ernakulam",
                    description = "Crisp, tender, spotless green okra pods. Jaivik Bharat certified organic cultivation.",
                    reservePrice = 35.0,
                    startPrice = 20.0,
                    endsAt = now + 120 * 60,
                    createdAt = now - 300,
                    status = "open",
                    topPrice = null,
                    topBidderId = null,
                    topBidderName = null,
                    bidCount = 0
                )
            )

            val l5 = listingDao.insertListing(
                ListingEntity(
                    farmerId = u3,
                    farmerName = "Suresh Nair",
                    farmerVerified = true,
                    produce = "Green Chillies",
                    grade = "A",
                    quantityKg = 30.0,
                    harvestDate = "Yesterday",
                    location = "Palakkad",
                    description = "Fiery, dark green pungent chillies with crisp stems. Highly aromatic.",
                    reservePrice = 45.0,
                    startPrice = 28.0,
                    endsAt = now + 180 * 60,
                    createdAt = now - 1500,
                    status = "open",
                    topPrice = 32.0,
                    topBidderId = b3,
                    topBidderName = "Malabar Wholesale Traders",
                    bidCount = 1
                )
            )

            val l6 = listingDao.insertListing(
                ListingEntity(
                    farmerId = u4,
                    farmerName = "Mathew Kurian",
                    farmerVerified = true,
                    produce = "Black Pepper (Panniyur-1)",
                    grade = "A",
                    quantityKg = 60.0,
                    harvestDate = "3 days ago, sun-dried",
                    location = "Kalpetta, Wayanad",
                    description = "Sun-dried organic Malabar black pepper, bold heavy berries with high piperine content.",
                    reservePrice = 620.0,
                    startPrice = 400.0,
                    endsAt = now + 240 * 60,
                    createdAt = now - 700,
                    status = "open",
                    topPrice = 550.0,
                    topBidderId = b3,
                    topBidderName = "Malabar Wholesale Traders",
                    bidCount = 2
                )
            )

            // Seed Bids
            bidDao.insertBid(BidEntity(listingId = l1, buyerId = b1, buyerName = "Fresh Basket Supermarket", pricePerKg = 24.0, createdAt = now - 400))
            bidDao.insertBid(BidEntity(listingId = l1, buyerId = b3, buyerName = "Malabar Wholesale Traders", pricePerKg = 25.0, createdAt = now - 250))
            bidDao.insertBid(BidEntity(listingId = l1, buyerId = b2, buyerName = "Hotel Sagar Restaurant", pricePerKg = 26.0, createdAt = now - 100))

            bidDao.insertBid(BidEntity(listingId = l2, buyerId = b3, buyerName = "Malabar Wholesale Traders", pricePerKg = 36.0, createdAt = now - 800))
            bidDao.insertBid(BidEntity(listingId = l2, buyerId = b1, buyerName = "Fresh Basket Supermarket", pricePerKg = 39.0, createdAt = now - 500))

            bidDao.insertBid(BidEntity(listingId = l3, buyerId = b4, buyerName = "Green Mart Grocery", pricePerKg = 26.0, createdAt = now - 350))

            bidDao.insertBid(BidEntity(listingId = l5, buyerId = b3, buyerName = "Malabar Wholesale Traders", pricePerKg = 32.0, createdAt = now - 600))
            bidDao.insertBid(BidEntity(listingId = l6, buyerId = b3, buyerName = "Malabar Wholesale Traders", pricePerKg = 550.0, createdAt = now - 200))
        }
    }
}
