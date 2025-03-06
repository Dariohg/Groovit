package com.example.groovit.login.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.groovit.ui.theme.*

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val username by loginViewModel.username.observeAsState("")
    val contraseña by loginViewModel.contraseña.observeAsState("")
    val isLoading by loginViewModel.isLoading.observeAsState(false)
    val errorMessage by loginViewModel.errorMessage.observeAsState(null)
    val isLoggedIn by loginViewModel.isLoggedIn.observeAsState(false)
    var passwordVisible by remember { mutableStateOf(false) }

    // Efecto para navegar cuando el login es exitoso
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navigateToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo o Título
            Text(
                text = "Groovit",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = NeonPurple,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { loginViewModel.onUsernameChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Username Icon",
                        tint = NeonPurple
                    )
                },
                label = { Text("Nombre de usuario", color = TextWhite) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = Color.Gray,
                    focusedLeadingIconColor = NeonPurple,
                    unfocusedLeadingIconColor = NeonPurple,
                    focusedLabelColor = NeonPurple,
                    unfocusedLabelColor = TextWhite,
                    cursorColor = NeonPurple,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                )
            )

            // Password Field
            OutlinedTextField(
                value = contraseña,
                onValueChange = { loginViewModel.onPasswordChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Icon",
                        tint = NeonPurple
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility",
                            tint = NeonPurple
                        )
                    }
                },
                label = { Text("Contraseña", color = TextWhite) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = Color.Gray,
                    focusedLeadingIconColor = NeonPurple,
                    unfocusedLeadingIconColor = NeonPurple,
                    focusedLabelColor = NeonPurple,
                    unfocusedLabelColor = TextWhite,
                    cursorColor = NeonPurple,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                )
            )

            // Login Button with gradient
            Button(
                onClick = { loginViewModel.onLoginClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(NeonPurple, NeonPurpleLight)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = TextWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "INICIAR SESIÓN",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }

            // Error message
            AnimatedVisibility(
                visible = errorMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Register option
            TextButton(
                onClick = navigateToRegister,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    color = NeonPurpleLight
                )
            }
        }
    }
}