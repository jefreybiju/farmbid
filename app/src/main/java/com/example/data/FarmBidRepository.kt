package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.roundToInt

class FarmBidRepository(private val database: FarmBidDatabase) {
    private val userDao = database.userDao()
    private val listingDao = database.listingDao()
    private val bidDao = database.bidDao()
    private val orderDao = database.orderDao()

    val platformFeePct = 2.0 // 2%
    val transportPerKg = 1.5 // ₹1.5/kg
    val minIncrement = 0.5   // ₹0.50/kg

    fun getFarmers(): Flow<List<UserEntity>> = userDao.getAllFarmers()
    fun getBuyers(): Flow<List<UserEntity>> = userDao.getUsersByRole("buyer")

    suspend fun getUser(id: Long): UserEntity? = userDao.getUserById(id)

    suspend fun registerUser(
        name: String,
        role: String,
        location: String,
        businessType: String,
        phone: String,
        certifications: String = "",
        produceOffered: String = "",
        farmSizeAcres: Double = 0.0,
        experienceYears: Int = 3
    ): UserEntity {
        val user = UserEntity(
            name = name.trim(),
            role = role.lowercase().trim(),
            location = location.trim(),
            businessType = businessType.trim(),
            isVerified = true,
            phone = phone.trim(),
            certifications = certifications.trim(),
            produceOffered = produceOffered.trim(),
            farmSizeAcres = farmSizeAcres,
            rating = 4.8,
            experienceYears = experienceYears
        )
        val id = userDao.insertUser(user)
        return user.copy(id = id)
    }

    /**
     * Robust Search and Filtering for Farmers (Used by Buyers).
     * Filters farmers by name, location, produce offered, and certifications.
     */
    fun searchAndFilterFarmers(
        searchQuery: String = "",
        selectedCertification: String = "ALL",
        selectedProduce: String = "ALL",
        selectedLocation: String = "ALL"
    ): Flow<List<UserEntity>> {
        return userDao.getAllFarmers().map { farmers ->
            farmers.filter { farmer ->
                val q = searchQuery.trim().lowercase()
                val matchesQuery = if (q.isEmpty()) true else {
                    farmer.name.lowercase().contains(q) ||
                    farmer.location.lowercase().contains(q) ||
                    farmer.produceOffered.lowercase().contains(q) ||
                    farmer.certifications.lowercase().contains(q) ||
                    farmer.businessType.lowercase().contains(q)
                }

                val matchesCert = if (selectedCertification.equals("ALL", true)) true else {
                    farmer.certifications.contains(selectedCertification, ignoreCase = true)
                }

                val matchesProduce = if (selectedProduce.equals("ALL", true)) true else {
                    farmer.produceOffered.contains(selectedProduce, ignoreCase = true)
                }

                val matchesLocation = if (selectedLocation.equals("ALL", true)) true else {
                    farmer.location.contains(selectedLocation, ignoreCase = true)
                }

                matchesQuery && matchesCert && matchesProduce && matchesLocation
            }
        }
    }

    fun getAllListings(): Flow<List<ListingEntity>> = listingDao.getAllListings()
    fun getOpenListings(): Flow<List<ListingEntity>> = listingDao.getOpenListings()
    fun getListingsForFarmer(farmerId: Long): Flow<List<ListingEntity>> = listingDao.getListingsByFarmer(farmerId)
    fun getPendingForFarmer(farmerId: Long): Flow<List<ListingEntity>> = listingDao.getPendingConfirmationForFarmer(farmerId)
    fun getListing(id: Long): Flow<ListingEntity?> = listingDao.getListingById(id)

    suspend fun createListing(
        caller: UserEntity,
        produce: String,
        grade: String,
        quantityKg: Double,
        harvestDate: String,
        location: String,
        description: String,
        reservePrice: Double,
        durationMinutes: Int
    ): Long = withContext(Dispatchers.IO) {
        // RBAC validation
        RoleAccessControl.assertPermission(caller, Permission.CREATE_HARVEST_LISTING)

        val now = System.currentTimeMillis() / 1000
        val startPrice = max(1.0, ((reservePrice * 0.6) * 100).roundToInt() / 100.0)
        val listing = ListingEntity(
            farmerId = caller.id,
            farmerName = caller.name,
            farmerVerified = caller.isVerified,
            produce = produce.trim(),
            grade = grade,
            quantityKg = quantityKg,
            harvestDate = harvestDate.trim().ifEmpty { "Today" },
            location = location.trim().ifEmpty { caller.location },
            description = description.trim(),
            reservePrice = reservePrice,
            startPrice = startPrice,
            endsAt = now + (durationMinutes * 60L),
            createdAt = now,
            status = "open",
            topPrice = null,
            topBidderId = null,
            topBidderName = null,
            bidCount = 0
        )
        listingDao.insertListing(listing)
    }

    fun getBidsForListing(listingId: Long): Flow<List<BidEntity>> = bidDao.getBidsForListing(listingId)
    fun getBidsForBuyer(buyerId: Long): Flow<List<BidEntity>> = bidDao.getBidsForBuyer(buyerId)
    fun getAllRecentBids(): Flow<List<BidEntity>> = bidDao.getAllRecentBids()
    fun getAllBids(): Flow<List<BidEntity>> = bidDao.getAllBids()

    suspend fun placeBid(listingId: Long, buyer: UserEntity, pricePerKg: Double): Result<Boolean> = withContext(Dispatchers.IO) {
        // RBAC validation
        try {
            RoleAccessControl.assertPermission(buyer, Permission.PLACE_LOT_BID)
        } catch (e: SecurityException) {
            return@withContext Result.failure(e)
        }

        val listing = listingDao.getListingDirect(listingId) ?: return@withContext Result.failure(Exception("Listing not found"))
        val now = System.currentTimeMillis() / 1000

        if (listing.status != "open" || now >= listing.endsAt) {
            return@withContext Result.failure(Exception("Auction has ended"))
        }

        val minBid = if (listing.topPrice != null) listing.topPrice + minIncrement else listing.startPrice
        if (pricePerKg < minBid) {
            return@withContext Result.failure(Exception("Bid must be at least ₹${"%.2f".format(minBid)}/kg"))
        }

        if (listing.topBidderId == buyer.id) {
            return@withContext Result.failure(Exception("You are already the leading bidder!"))
        }

        // Anti-sniping
        val timeRemaining = listing.endsAt - now
        var newEndsAt = listing.endsAt
        var extended = false
        if (timeRemaining in 1..120) {
            newEndsAt = now + 120
            extended = true
        }

        bidDao.insertBid(
            BidEntity(
                listingId = listingId,
                buyerId = buyer.id,
                buyerName = buyer.name,
                pricePerKg = pricePerKg,
                createdAt = now
            )
        )

        listingDao.updateListing(
            listing.copy(
                topPrice = pricePerKg,
                topBidderId = buyer.id,
                topBidderName = buyer.name,
                bidCount = listing.bidCount + 1,
                endsAt = newEndsAt
            )
        )

        Result.success(extended)
    }

    suspend fun simulateRivalBid(listingId: Long, currentBuyerId: Long): Result<Double> = withContext(Dispatchers.IO) {
        val listing = listingDao.getListingDirect(listingId) ?: return@withContext Result.failure(Exception("Listing not found"))
        val now = System.currentTimeMillis() / 1000
        if (listing.status != "open" || now >= listing.endsAt) {
            return@withContext Result.failure(Exception("Auction is closed"))
        }

        val pool = listOf(
            Pair(6L, "Fresh Basket Supermarket"),
            Pair(7L, "Hotel Sagar Restaurant"),
            Pair(8L, "Malabar Wholesale Traders"),
            Pair(9L, "Green Mart Grocery")
        ).filter { it.first != currentBuyerId && it.first != listing.topBidderId }

        val rival = pool.randomOrNull() ?: Pair(10L, "Metro Agro Wholesalers")
        val currentTop = listing.topPrice ?: (listing.startPrice - minIncrement)
        val incrementStep = listOf(0.5, 1.0, 1.5, 2.0).random()
        val newPrice = ((currentTop + incrementStep) * 100).roundToInt() / 100.0

        bidDao.insertBid(
            BidEntity(
                listingId = listingId,
                buyerId = rival.first,
                buyerName = rival.second,
                pricePerKg = newPrice,
                createdAt = now
            )
        )

        var newEndsAt = listing.endsAt
        if (listing.endsAt - now in 1..120) {
            newEndsAt = now + 120
        }

        listingDao.updateListing(
            listing.copy(
                topPrice = newPrice,
                topBidderId = rival.first,
                topBidderName = rival.second,
                bidCount = listing.bidCount + 1,
                endsAt = newEndsAt
            )
        )

        Result.success(newPrice)
    }

    suspend fun endAuctionEarly(caller: UserEntity, listingId: Long) = withContext(Dispatchers.IO) {
        RoleAccessControl.assertPermission(caller, Permission.CLOSE_AUCTION_EARLY)
        val listing = listingDao.getListingDirect(listingId) ?: return@withContext
        if (listing.farmerId != caller.id) throw SecurityException("You do not own this harvest listing.")

        val now = System.currentTimeMillis() / 1000
        val isReserveMet = listing.topPrice != null && listing.topPrice >= listing.reservePrice
        val newStatus = if (isReserveMet) "pending_confirmation" else "unsold"
        listingDao.updateListing(listing.copy(endsAt = now - 1, status = newStatus))
    }

    suspend fun confirmSale(caller: UserEntity, listingId: Long): Long = withContext(Dispatchers.IO) {
        RoleAccessControl.assertPermission(caller, Permission.CONFIRM_SALE)
        val listing = listingDao.getListingDirect(listingId) ?: return@withContext -1L
        if (listing.farmerId != caller.id) throw SecurityException("You do not own this harvest listing.")
        if (listing.topPrice == null || listing.topBidderId == null) return@withContext -1L

        val order = OrderEntity(
            listingId = listingId,
            farmerId = listing.farmerId,
            farmerName = listing.farmerName,
            buyerId = listing.topBidderId,
            buyerName = listing.topBidderName ?: "Buyer",
            produce = listing.produce,
            quantityKg = listing.quantityKg,
            pricePerKg = listing.topPrice,
            totalAmount = ((listing.topPrice * listing.quantityKg) * 100).roundToInt() / 100.0,
            status = "confirmed",
            paymentStatus = "pending",
            createdAt = System.currentTimeMillis() / 1000
        )
        val orderId = orderDao.insertOrder(order)
        listingDao.updateListing(listing.copy(status = "sold"))
        orderId
    }

    suspend fun declineSale(caller: UserEntity, listingId: Long) = withContext(Dispatchers.IO) {
        RoleAccessControl.assertPermission(caller, Permission.DECLINE_SALE)
        val listing = listingDao.getListingDirect(listingId) ?: return@withContext
        if (listing.farmerId != caller.id) throw SecurityException("You do not own this harvest listing.")
        listingDao.updateListing(listing.copy(status = "declined"))
    }

    fun getOrdersForFarmer(farmerId: Long): Flow<List<OrderEntity>> = orderDao.getOrdersForFarmer(farmerId)
    fun getOrdersForBuyer(buyerId: Long): Flow<List<OrderEntity>> = orderDao.getOrdersForBuyer(buyerId)

    suspend fun dispatchOrder(caller: UserEntity, orderId: Long) = withContext(Dispatchers.IO) {
        RoleAccessControl.assertPermission(caller, Permission.DISPATCH_ORDER)
        val order = orderDao.getOrderById(orderId) ?: return@withContext
        if (order.farmerId != caller.id) throw SecurityException("Unauthorized: You are not the farmer for this order.")
        orderDao.updateOrder(order.copy(status = "dispatched"))
    }

    suspend fun deliverOrder(caller: UserEntity, orderId: Long) = withContext(Dispatchers.IO) {
        RoleAccessControl.assertPermission(caller, Permission.CONFIRM_DELIVERY_ACCEPTANCE)
        val order = orderDao.getOrderById(orderId) ?: return@withContext
        if (order.buyerId != caller.id) throw SecurityException("Unauthorized: You are not the buyer for this order.")
        orderDao.updateOrder(order.copy(status = "delivered"))
    }

    suspend fun reportOrderDispute(caller: UserEntity, orderId: Long, reason: String) = withContext(Dispatchers.IO) {
        RoleAccessControl.assertPermission(caller, Permission.REPORT_ORDER_DISPUTE)
        val order = orderDao.getOrderById(orderId) ?: return@withContext
        if (order.buyerId != caller.id) throw SecurityException("Unauthorized: You are not the buyer for this order.")
        orderDao.updateOrder(order.copy(status = "disputed", disputeReason = reason.trim()))
    }

    suspend fun markOrderPaid(caller: UserEntity, orderId: Long) = withContext(Dispatchers.IO) {
        RoleAccessControl.assertPermission(caller, Permission.RECORD_PAYMENT_RECEIVED)
        val order = orderDao.getOrderById(orderId) ?: return@withContext
        if (order.farmerId != caller.id) throw SecurityException("Unauthorized: You are not the farmer for this order.")
        orderDao.updateOrder(order.copy(paymentStatus = "paid"))
    }

    suspend fun resetDatabase() = withContext(Dispatchers.IO) {
        FarmBidDatabase.seedDatabase(database)
    }
}
