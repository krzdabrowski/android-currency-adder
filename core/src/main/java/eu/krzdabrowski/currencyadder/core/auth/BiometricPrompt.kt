package eu.krzdabrowski.currencyadder.core.auth

import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricManager.from
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import eu.krzdabrowski.currencyadder.core.utils.findActivity

@Composable
fun BiometricPrompt(
    title: String,
    subtitleBiometric: String,
    subtitleCredentials: String,
    negativeButton: String,
    onAuthenticationSuccess: () -> Unit,
    onCancel: () -> Unit,
) {
    val context = LocalContext.current
    val activity = remember { context.findActivity() } as FragmentActivity
    val executor = remember { ContextCompat.getMainExecutor(context) }
    val biometricManager = remember { from(context) }

    val callback = remember {
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                onAuthenticationSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

                if (errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                    errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                    errorCode == BiometricPrompt.ERROR_CANCELED
                ) {
                    onCancel()
                }
            }
        }
    }

    val biometricPrompt = remember {
        BiometricPrompt(activity, executor, callback)
    }

    val promptInfo = remember {
        val secureOption = bestSecureOption(biometricManager)
        val isBiometricFlow = secureOption and DEVICE_CREDENTIAL == 0

        BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(
                if (isBiometricFlow) {
                    subtitleBiometric
                } else {
                    subtitleCredentials
                },
            )
            .apply {
                if (isBiometricFlow) {
                    setNegativeButtonText(negativeButton)
                }
            }
            .setAllowedAuthenticators(secureOption)
            .build()
    }

    DisposableEffect(biometricPrompt) {
        biometricPrompt.authenticate(promptInfo)

        onDispose {
            biometricPrompt.cancelAuthentication()
        }
    }
}

// source: https://github.com/gematik/E-Rezept-App-Android/blob/master/android/src/main/java/de/gematik/ti/erp/app/userauthentication/ui/BiometricPrompt.kt
private fun bestSecureOption(biometricManager: BiometricManager): Int {
    when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
        BIOMETRIC_SUCCESS,
        BIOMETRIC_ERROR_NONE_ENROLLED,
        -> return BIOMETRIC_STRONG
    }
    when (biometricManager.canAuthenticate(BIOMETRIC_WEAK)) {
        BIOMETRIC_SUCCESS,
        BIOMETRIC_ERROR_NONE_ENROLLED,
        -> return BIOMETRIC_WEAK
    }
    when (biometricManager.canAuthenticate(DEVICE_CREDENTIAL)) {
        BIOMETRIC_SUCCESS,
        BIOMETRIC_ERROR_NONE_ENROLLED,
        -> return DEVICE_CREDENTIAL
    }
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        DEVICE_CREDENTIAL or BIOMETRIC_WEAK
    } else {
        DEVICE_CREDENTIAL
    }
}
