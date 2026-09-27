package com.example.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.OwlAccessory
import com.example.data.OwlEyeColor
import com.example.designsystem.background.OwlPatternLayer
import com.example.designsystem.components.InformCard
import com.example.designsystem.theme.BlueBase
import com.example.designsystem.theme.BlueBgTop
import com.example.onboarding.R
import com.example.onboarding.domain.PetNameValidator
import com.example.designsystem.R as DesignSystemR

enum class OnboardingStep { GREETING, NAME, PET_VIEW }

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onNameConfirmed: (String) -> Unit,
    onPetViewConfirmed: (OwlEyeColor, OwlAccessory?) -> Unit,
    initialEyeColor: OwlEyeColor = OwlEyeColor.YELLOW,
    initialAccessory: OwlAccessory? = null,
) {
    var step by rememberSaveable { mutableStateOf(OnboardingStep.GREETING) }
    var name by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BlueBgTop, BlueBase)
                )
            )
    ) {
        OwlPatternLayer(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            alpha = 0.08f,
            cellSize = 96.dp,
            owlSize = 64.dp,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(DesignSystemR.string.app_name),
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(128.dp))
            InformCard(
                title = stringResource(R.string.onboarding_title),
                content = {
                    Text(
                        text = stringResource(R.string.onboarding_text),
                        fontSize = 18.sp,
                    )
                },
                okText = stringResource(R.string.start_text_button),
                visible = true,
                onOkClick = { step = OnboardingStep.NAME },
            )
        }

        when (step) {
            OnboardingStep.NAME -> PetNameDialog(
                name = name,
                onNameChange = { name = it },
                onDismiss = { step = OnboardingStep.GREETING },
                onConfirm = {
                    onNameConfirmed(name.trim())
                    step = OnboardingStep.PET_VIEW
                },
            )

            OnboardingStep.PET_VIEW -> PetViewDialog(
                initialEyeColor = initialEyeColor,
                initialAccessory = initialAccessory,
                onDismiss = { step = OnboardingStep.NAME },
                onConfirm = { eyes, acc ->
                    onPetViewConfirmed(eyes, acc)
                },
            )

            OnboardingStep.GREETING -> Unit
        }
    }
}

@Composable
private fun PetNameDialog(
    name: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val isValid = PetNameValidator.validate(name)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                )
        ) {
            InformCard(
                title = stringResource(R.string.pet_name_title),
                content = {
                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        singleLine = true,
                        isError = name.isNotEmpty() && !isValid,
                        supportingText = {
                            if (name.isNotEmpty() && !isValid) {
                                Text(
                                    stringResource(
                                        R.string.pet_name_error,
                                        PetNameValidator.MIN,
                                        PetNameValidator.MAX,
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    )
                },
                okEnabled = isValid,
                visible = true,
                onOkClick = onConfirm,
                onDismissClick = onDismiss,
            )
        }
    }
}

@Composable
private fun PetViewDialog(
    initialEyeColor: OwlEyeColor,
    initialAccessory: OwlAccessory?,
    onDismiss: () -> Unit,
    onConfirm: (OwlEyeColor, OwlAccessory?) -> Unit,
) {
    var eyeColor by rememberSaveable { mutableStateOf(initialEyeColor) }
    var accessory by rememberSaveable { mutableStateOf(initialAccessory) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                )
        ) {
            InformCard(
                title = stringResource(R.string.pet_view_title),
                content = {
                    PetViewDialogContent(
                        eyeColor = eyeColor,
                        accessory = accessory,
                        onEyeColorChange = { eyeColor = it },
                        onAccessoryChange = { accessory = it },
                    )
                },
                visible = true,
                onOkClick = { onConfirm(eyeColor, accessory) },
                onDismissClick = onDismiss,
            )
        }
    }
}