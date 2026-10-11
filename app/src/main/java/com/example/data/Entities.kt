package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val role: String, // "farmer" or "buyer"
    val location: String,
    val businessType: String, // e.g., "Vegetable Farmer", "Supermarket Chain"
    val isVerified: Boolean = true,
    val phone: String = "",
    val certifications: String = "", // e.g. "Certified Organic (NPOP), Jaivik Bharat, GAP Certified"
    val produceOffered: String = "", // e.g. "Tomatoes, Okra, Chillies, Bananas"
    val farmSizeAcres: Double = 0.0,
    val rating: Double = 4.8,
    val experienceYears: Int = 5,
    val pinCode: String = "",
    val gstOrLicense: String = "",
    val username: String = "",
    val password: String = "pass123"
)

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val farmerId: Long,
    val farmerName: String,
    val farmerVerified: Boolean,
    val produce: String,
    val grade: String, // "A", "B", "C"
    val quantityKg: Double,
    val harvestDate: String,
    val location: String,
    val description: String,
    val reservePrice: Double, // kept confidential from buyers
    val startPrice: Double,
    val endsAt: Long, // unix seconds
    val createdAt: Long, // unix seconds
    val status: String, // "open", "pending_confirmation", "sold", "unsold", "declined"
    val topPrice: Double? = null,
    val topBidderId: Long? = null,
    val topBidderName: String? = null,
    val bidCount: Int = 0
)

@Entity(tableName = "bids")
data class BidEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listingId: Long,
    val buyerId: Long,
    val buyerName: String,
    val pricePerKg: Double,
    val createdAt: Long
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listingId: Long,
    val farmerId: Long,
    val farmerName: String,
    val buyerId: Long,
    val buyerName: String,
    val produce: String,
    val quantityKg: Double,
    val pricePerKg: Double,
    val totalAmount: Double,
    val status: String, // "confirmed", "dispatched", "delivered", "disputed"
    val paymentStatus: String, // "pending", "paid"
    val disputeReason: String? = null,
    val createdAt: Long
)
