package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE role = :role ORDER BY id ASC")
    fun getUsersByRole(role: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE role = 'farmer' ORDER BY rating DESC, name ASC")
    fun getAllFarmers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT COUNT(*) FROM users")
    suspend fun countUsers(): Int

    @Query("DELETE FROM users")
    suspend fun clearAll()
}

@Dao
interface ListingDao {
    @Query("SELECT * FROM listings ORDER BY endsAt ASC")
    fun getAllListings(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE status = 'open' ORDER BY endsAt ASC")
    fun getOpenListings(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE farmerId = :farmerId ORDER BY id DESC")
    fun getListingsByFarmer(farmerId: Long): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE farmerId = :farmerId AND status = 'pending_confirmation'")
    fun getPendingConfirmationForFarmer(farmerId: Long): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE id = :id LIMIT 1")
    fun getListingById(id: Long): Flow<ListingEntity?>

    @Query("SELECT * FROM listings WHERE id = :id LIMIT 1")
    suspend fun getListingDirect(id: Long): ListingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: ListingEntity): Long

    @Update
    suspend fun updateListing(listing: ListingEntity)

    @Query("DELETE FROM listings")
    suspend fun clearAll()
}

@Dao
interface BidDao {
    @Query("SELECT * FROM bids WHERE listingId = :listingId ORDER BY pricePerKg DESC, id DESC")
    fun getBidsForListing(listingId: Long): Flow<List<BidEntity>>

    @Query("SELECT * FROM bids WHERE buyerId = :buyerId ORDER BY createdAt DESC")
    fun getBidsForBuyer(buyerId: Long): Flow<List<BidEntity>>

    @Query("SELECT * FROM bids ORDER BY createdAt DESC LIMIT 50")
    fun getAllRecentBids(): Flow<List<BidEntity>>

    @Query("SELECT * FROM bids ORDER BY createdAt DESC")
    fun getAllBids(): Flow<List<BidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBid(bid: BidEntity): Long

    @Query("DELETE FROM bids")
    suspend fun clearAll()
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE farmerId = :farmerId ORDER BY id DESC")
    fun getOrdersForFarmer(farmerId: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE buyerId = :buyerId ORDER BY id DESC")
    fun getOrdersForBuyer(buyerId: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: Long): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("DELETE FROM orders")
    suspend fun clearAll()
}
