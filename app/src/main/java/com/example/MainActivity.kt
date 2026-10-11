package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.BuyerHomeScreen
import com.example.ui.BuyerLoginScreen
import com.example.ui.BuyerRegisterScreen
import com.example.ui.FarmBidViewModel
import com.example.ui.FarmerHomeScreen
import com.example.ui.FarmerLoginScreen
import com.example.ui.FarmerRegisterScreen
import com.example.ui.ListingDetailScreen
import com.example.ui.RoleSelectScreen
import com.example.ui.Screen
import com.example.ui.UnifiedSignupScreen
import com.example.ui.theme.FarmBidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: FarmBidViewModel = viewModel()
            val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
            val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
            val farmers by viewModel.farmers.collectAsStateWithLifecycle()
            val buyers by viewModel.buyers.collectAsStateWithLifecycle()
            val openListings by viewModel.openListings.collectAsStateWithLifecycle()
            val farmerListings by viewModel.farmerListings.collectAsStateWithLifecycle()
            val pendingDecisions by viewModel.farmerPendingDecisions.collectAsStateWithLifecycle()
            val userOrders by viewModel.userOrders.collectAsStateWithLifecycle()
            val userBids by viewModel.userBids.collectAsStateWithLifecycle()
            val farmerTab by viewModel.farmerTab.collectAsStateWithLifecycle()
            val buyerTab by viewModel.buyerTab.collectAsStateWithLifecycle()
            val selectedListing by viewModel.selectedListing.collectAsStateWithLifecycle()
            val selectedBids by viewModel.selectedListingBids.collectAsStateWithLifecycle()
            val allRecentBids by viewModel.allRecentBids.collectAsStateWithLifecycle()
            val allBids by viewModel.allBids.collectAsStateWithLifecycle()
            val snackMessage by viewModel.snackMessage.collectAsStateWithLifecycle()

            // Farmer Search & Filter State
            val filteredFarmers by viewModel.filteredFarmers.collectAsStateWithLifecycle()
            val farmerSearchQuery by viewModel.farmerSearchQuery.collectAsStateWithLifecycle()
            val farmerCertFilter by viewModel.farmerCertFilter.collectAsStateWithLifecycle()
            val farmerProduceFilter by viewModel.farmerProduceFilter.collectAsStateWithLifecycle()
            val farmerLocationFilter by viewModel.farmerLocationFilter.collectAsStateWithLifecycle()

            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(snackMessage) {
                snackMessage?.let {
                    snackbarHostState.showSnackbar(it)
                    viewModel.clearMessage()
                }
            }

            // Determine active theme: Farmer palette used for Farmer mode & Farmer auth
            val isFarmerTheme = when (val s = currentScreen) {
                is Screen.FarmerLogin, is Screen.FarmerRegister, is Screen.FarmerMain -> true
                is Screen.BuyerLogin, is Screen.BuyerRegister, is Screen.BuyerMain -> false
                is Screen.UnifiedSignup -> s.initialRole == "farmer"
                is Screen.ListingDetail -> currentUser?.role == "farmer"
                is Screen.RoleSelect -> true
            }

            FarmBidTheme(isFarmerTheme = isFarmerTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState, modifier = Modifier.navigationBarsPadding()) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (val screen = currentScreen) {
                            is Screen.RoleSelect -> {
                                RoleSelectScreen(
                                    farmers = farmers,
                                    buyers = buyers,
                                    onSelectRole = { role ->
                                        if (role == "farmer") {
                                            viewModel.navigateTo(Screen.FarmerLogin)
                                        } else {
                                            viewModel.navigateTo(Screen.BuyerLogin)
                                        }
                                    },
                                    onNavigateToSignup = { role ->
                                        viewModel.navigateTo(Screen.UnifiedSignup(role))
                                    },
                                    onQuickLogin = { user ->
                                        viewModel.loginAs(user)
                                    },
                                    onThreeColumnLogin = { role, userId, pass ->
                                        viewModel.loginWithThreeFields(role, userId, pass)
                                    },
                                    onResetDemo = {
                                        viewModel.resetDemoData()
                                    }
                                )
                            }

                            is Screen.UnifiedSignup -> {
                                BackHandler { viewModel.navigateTo(Screen.RoleSelect) }
                                UnifiedSignupScreen(
                                    initialRole = screen.initialRole,
                                    onRegister = { role, name, loc, type, phone, certs, produce, size ->
                                        viewModel.registerWithRole(role, name, loc, type, phone, certs, produce, size)
                                    },
                                    onNavigateToLogin = { role ->
                                        if (role == "farmer") {
                                            viewModel.navigateTo(Screen.FarmerLogin)
                                        } else {
                                            viewModel.navigateTo(Screen.BuyerLogin)
                                        }
                                    },
                                    onBack = { viewModel.navigateTo(Screen.RoleSelect) }
                                )
                            }

                            is Screen.FarmerLogin -> {
                                BackHandler { viewModel.navigateTo(Screen.RoleSelect) }
                                FarmerLoginScreen(
                                    farmers = farmers,
                                    onLogin = { user -> viewModel.loginWithRoleValidation(user, "farmer") },
                                    onNavigateToRegister = { viewModel.navigateTo(Screen.UnifiedSignup("farmer")) },
                                    onBack = { viewModel.navigateTo(Screen.RoleSelect) }
                                )
                            }

                            is Screen.FarmerRegister -> {
                                BackHandler { viewModel.navigateTo(Screen.FarmerLogin) }
                                FarmerRegisterScreen(
                                    onRegister = { name, loc, type, phone ->
                                        viewModel.registerNewFarmer(name, loc, type, phone, "Certified Organic", "Vegetables", 4.0)
                                    },
                                    onBack = { viewModel.navigateTo(Screen.FarmerLogin) }
                                )
                            }

                            is Screen.BuyerLogin -> {
                                BackHandler { viewModel.navigateTo(Screen.RoleSelect) }
                                BuyerLoginScreen(
                                    buyers = buyers,
                                    onLogin = { user -> viewModel.loginWithRoleValidation(user, "buyer") },
                                    onNavigateToRegister = { viewModel.navigateTo(Screen.UnifiedSignup("buyer")) },
                                    onBack = { viewModel.navigateTo(Screen.RoleSelect) }
                                )
                            }

                            is Screen.BuyerRegister -> {
                                BackHandler { viewModel.navigateTo(Screen.BuyerLogin) }
                                BuyerRegisterScreen(
                                    onRegister = { name, loc, type, phone ->
                                        viewModel.registerNewBuyer(name, loc, type, phone)
                                    },
                                    onBack = { viewModel.navigateTo(Screen.BuyerLogin) }
                                )
                            }

                            is Screen.FarmerMain -> {
                                BackHandler { viewModel.logout() }
                                currentUser?.let { farmer ->
                                    FarmerHomeScreen(
                                        farmer = farmer,
                                        currentTab = farmerTab,
                                        listings = farmerListings,
                                        pendingDecisions = pendingDecisions,
                                        orders = userOrders,
                                        allBids = allBids,
                                        onTabSelected = { viewModel.setFarmerTab(it) },
                                        onOpenListing = { id ->
                                            viewModel.openListingDetail(id, Screen.FarmerMain)
                                        },
                                        onPublishListing = { produce, grade, qty, date, loc, desc, reserve, duration ->
                                            viewModel.publishNewListing(produce, grade, qty, date, loc, desc, reserve, duration)
                                        },
                                        onConfirmSale = { viewModel.confirmSale(it) },
                                        onDeclineSale = { viewModel.declineSale(it) },
                                        onDispatchOrder = { viewModel.dispatchOrder(it) },
                                        onMarkOrderPaid = { viewModel.markOrderPaid(it) },
                                        onLogout = { viewModel.logout() },
                                        onResetDemo = { viewModel.resetDemoData() }
                                    )
                                }
                            }

                            is Screen.BuyerMain -> {
                                BackHandler { viewModel.logout() }
                                currentUser?.let { buyer ->
                                    BuyerHomeScreen(
                                        buyer = buyer,
                                        currentTab = buyerTab,
                                        openListings = openListings,
                                        userBids = userBids,
                                        orders = userOrders,
                                        filteredFarmers = filteredFarmers,
                                        farmerSearchQuery = farmerSearchQuery,
                                        onFarmerSearchQueryChange = { viewModel.farmerSearchQuery.value = it },
                                        selectedCertification = farmerCertFilter,
                                        onCertificationChange = { viewModel.farmerCertFilter.value = it },
                                        selectedProduce = farmerProduceFilter,
                                        onProduceChange = { viewModel.farmerProduceFilter.value = it },
                                        selectedLocation = farmerLocationFilter,
                                        onLocationChange = { viewModel.farmerLocationFilter.value = it },
                                        onTabSelected = { viewModel.setBuyerTab(it) },
                                        onOpenListing = { id ->
                                            viewModel.openListingDetail(id, Screen.BuyerMain)
                                        },
                                        onQuickRaiseBid = { id, inc -> viewModel.quickRaiseBid(id, inc) },
                                        onSimulateRivalBid = { id -> viewModel.simulateRivalBid(id) },
                                        recentActivityBids = allRecentBids,
                                        allBids = allBids,
                                        onDeliverOrder = { viewModel.deliverOrder(it) },
                                        onDisputeOrder = { id, reason ->
                                            viewModel.disputeOrder(id, reason)
                                        },
                                        onLogout = { viewModel.logout() },
                                        onResetDemo = { viewModel.resetDemoData() }
                                    )
                                }
                            }

                            is Screen.ListingDetail -> {
                                ListingDetailScreen(
                                    listing = selectedListing,
                                    bids = selectedBids,
                                    currentUser = currentUser,
                                    onBack = { viewModel.navigateTo(screen.fromScreen) },
                                    onPlaceBid = { id, price -> viewModel.placeBid(id, price) },
                                    onSimulateRivalBid = { id -> viewModel.simulateRivalBid(id) },
                                    onEndAuctionEarly = { id -> viewModel.endAuctionNow(id) },
                                    onConfirmSale = { id -> viewModel.confirmSale(id) },
                                    onDeclineSale = { id -> viewModel.declineSale(id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
