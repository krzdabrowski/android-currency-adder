package eu.krzdabrowski.currencyadder.core.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.rememberNavController
import eu.krzdabrowski.currencyadder.core.R
import eu.krzdabrowski.currencyadder.core.auth.BiometricPrompt
import eu.krzdabrowski.currencyadder.core.navigation.NavigationDestination
import eu.krzdabrowski.currencyadder.core.navigation.NavigationFactory
import eu.krzdabrowski.currencyadder.core.navigation.NavigationHost
import eu.krzdabrowski.currencyadder.core.navigation.NavigationManager
import eu.krzdabrowski.currencyadder.core.utils.collectWithLifecycle

@Composable
fun MainScreen(
    navigationFactories: Set<NavigationFactory>,
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    var showBiometricPrompt by rememberSaveable { mutableStateOf(true) }
    var showBiometricErrorSnackbar by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = { MainTopAppBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        if (showBiometricPrompt) {
            BiometricPrompt(
                title = stringResource(R.string.dialog_title),
                subtitleBiometric = stringResource(R.string.dialog_biometric_subtitle),
                subtitleCredentials = stringResource(R.string.dialog_credentials_subtitle),
                negativeButton = stringResource(R.string.dialog_negative_button),
                onAuthenticationSucceeded = {
                    showBiometricPrompt = false
                },
                onCancel = {
                    showBiometricErrorSnackbar = true
                },
            )
        } else {
            NavigationHost(
                modifier = Modifier
                    .padding(it),
                navController = navController,
                factories = navigationFactories,
            )

            navigationManager
                .navigationEvent
                .collectWithLifecycle(
                    key = navController,
                ) { navigationCommand ->
                    when (navigationCommand.destination) {
                        NavigationDestination.Back.route -> navController.navigateUp()
                        else -> navController.navigate(navigationCommand.destination, navigationCommand.configuration)
                    }
                }
        }

        if (showBiometricErrorSnackbar) {
            MainSnackbar(
                snackbarHostState = snackbarHostState,
            )
        }
    }
}

@Composable
private fun MainTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Medium,
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}

@Composable
private fun MainSnackbar(
    snackbarHostState: SnackbarHostState,
) {
    val errorMessage = stringResource(R.string.dialog_cancelled_snackbar_text)

    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(
            message = errorMessage,
        )
    }
}
