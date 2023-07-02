package com.test.palmapi.bottombar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.textColor

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp),
        colors = CardDefaults.cardColors(CardColor),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        NavigationBar(
            modifier = Modifier
                .height(80.dp),
            containerColor = CardColor,
            tonalElevation = 0.dp,
        ) {
            items.forEach {
                val isYellow = currentRoute?.hierarchy?.any { nav ->
                    nav.route == it.route
                } == true
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = it.icon),
                            contentDescription = "",
                            modifier = Modifier.size(35.dp),
                            tint = Color.Unspecified
                        )
                    },
                    label = {
                        it.title?.let {
                            Text(
                                text = it,
                                color = if (isYellow) Color.Yellow else textColor
                            )
                        }
                    },
                    selected = isYellow,
                    onClick = {
                        it.route?.let { it1 ->
                            navController.navigate(it1) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}