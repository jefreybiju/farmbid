package com.example.data

enum class UserRole(val roleKey: String, val displayName: String) {
    FARMER("farmer", "Agricultural Producer / Farmer"),
    BUYER("buyer", "Commercial Wholesale Buyer");

    companion object {
        fun fromString(key: String): UserRole {
            return when (key.lowercase()) {
                "farmer" -> FARMER
                "buyer" -> BUYER
                else -> throw IllegalArgumentException("Unknown role: $key")
            }
        }
    }
}

enum class Permission {
    // Farmer permissions
    CREATE_HARVEST_LISTING,
    EDIT_HARVEST_LISTING,
    CLOSE_AUCTION_EARLY,
    CONFIRM_SALE,
    DECLINE_SALE,
    DISPATCH_ORDER,
    RECORD_PAYMENT_RECEIVED,
    VIEW_CONFIDENTIAL_RESERVE,

    // Buyer permissions
    SEARCH_FARMERS_DIRECTORY,
    BROWSE_LIVE_MARKETPLACE,
    PLACE_LOT_BID,
    CONFIRM_DELIVERY_ACCEPTANCE,
    REPORT_ORDER_DISPUTE
}

object RoleAccessControl {
    private val farmerPermissions = setOf(
        Permission.CREATE_HARVEST_LISTING,
        Permission.EDIT_HARVEST_LISTING,
        Permission.CLOSE_AUCTION_EARLY,
        Permission.CONFIRM_SALE,
        Permission.DECLINE_SALE,
        Permission.DISPATCH_ORDER,
        Permission.RECORD_PAYMENT_RECEIVED,
        Permission.VIEW_CONFIDENTIAL_RESERVE
    )

    private val buyerPermissions = setOf(
        Permission.SEARCH_FARMERS_DIRECTORY,
        Permission.BROWSE_LIVE_MARKETPLACE,
        Permission.PLACE_LOT_BID,
        Permission.CONFIRM_DELIVERY_ACCEPTANCE,
        Permission.REPORT_ORDER_DISPUTE
    )

    fun hasPermission(userRole: String, permission: Permission): Boolean {
        return when (userRole.lowercase()) {
            "farmer" -> farmerPermissions.contains(permission)
            "buyer" -> buyerPermissions.contains(permission)
            else -> false
        }
    }

    fun hasPermission(user: UserEntity, permission: Permission): Boolean {
        return hasPermission(user.role, permission)
    }

    fun assertPermission(user: UserEntity?, permission: Permission) {
        if (user == null) {
            throw SecurityException("RBAC Access Denied: User is not authenticated.")
        }
        if (!hasPermission(user.role, permission)) {
            throw SecurityException(
                "RBAC Access Denied: Role '${user.role}' lacks permission '${permission.name}'."
            )
        }
    }

    fun validatePortalLogin(userRole: String, intendedPortalRole: String): Result<Unit> {
        if (!userRole.equals(intendedPortalRole, ignoreCase = true)) {
            val portalName = if (intendedPortalRole.equals("farmer", true)) "Farmer Portal" else "Buyer Portal"
            val actualRoleName = if (userRole.equals("farmer", true)) "Farmer" else "Commercial Buyer"
            return Result.failure(
                SecurityException(
                    "RBAC Role Mismatch: This account is registered as a $actualRoleName. You cannot log in through the $portalName."
                )
            )
        }
        return Result.success(Unit)
    }
}
