package com.example

import com.example.data.Permission
import com.example.data.RoleAccessControl
import com.example.data.UserEntity
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testRbacFarmerCannotAccessBuyerPortal() {
    val farmer = UserEntity(id = 1, name = "Anil Kumar", role = "farmer", location = "Ernakulam", businessType = "Vegetable Farm")
    val result = RoleAccessControl.validatePortalLogin(farmer.role, "buyer")
    assertTrue(result.isFailure)
  }

  @Test
  fun testRbacBuyerCannotAccessFarmerPortal() {
    val buyer = UserEntity(id = 2, name = "Fresh Basket", role = "buyer", location = "Kochi", businessType = "Supermarket")
    val result = RoleAccessControl.validatePortalLogin(buyer.role, "farmer")
    assertTrue(result.isFailure)
  }

  @Test
  fun testRbacBuyerCanPlaceBid() {
    val buyer = UserEntity(id = 2, name = "Fresh Basket", role = "buyer", location = "Kochi", businessType = "Supermarket")
    assertTrue(RoleAccessControl.hasPermission(buyer, Permission.PLACE_LOT_BID))
    assertFalse(RoleAccessControl.hasPermission(buyer, Permission.CREATE_HARVEST_LISTING))
  }

  @Test
  fun testRbacFarmerCanCreateListing() {
    val farmer = UserEntity(id = 1, name = "Anil Kumar", role = "farmer", location = "Ernakulam", businessType = "Vegetable Farm")
    assertTrue(RoleAccessControl.hasPermission(farmer, Permission.CREATE_HARVEST_LISTING))
    assertFalse(RoleAccessControl.hasPermission(farmer, Permission.PLACE_LOT_BID))
  }
}

