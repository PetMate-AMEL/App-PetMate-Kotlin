package com.example.patitasid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.patitasid.ui.theme.PetMateTheme

// RNF-15: touch targets of at least 48 dp; the design uses 52 dp.
private val MinTouchHeight = 52.dp
private val PetShape = RoundedCornerShape(12.dp)

@Composable
fun PetPrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = PetShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = modifier.fillMaxWidth().heightIn(min = MinTouchHeight)
    ) { Text(text, style = MaterialTheme.typography.labelLarge) }
}

@Composable
fun PetOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.primary
) {
    OutlinedButton(
        onClick = onClick,
        shape = PetShape,
        border = BorderStroke(1.dp, contentColor),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor),
        modifier = modifier.fillMaxWidth().heightIn(min = MinTouchHeight)
    ) { Text(text, style = MaterialTheme.typography.labelLarge) }
}

@Composable
fun PetTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = errorMessage != null,
        supportingText = errorMessage?.let { message -> { Text(message) } },
        singleLine = true,
        shape = PetShape,
        modifier = modifier.fillMaxWidth().heightIn(min = MinTouchHeight)
    )
}

@Composable
fun PetCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = PetShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}

@Preview(showBackground = true)
@Composable
private fun PetComponentsPreview() {
    PetMateTheme {
        PetCard {
            PetTextField(value = "Firulais", onValueChange = {}, label = "Nombre de la mascota")
            PetPrimaryButton(text = "Guardar", onClick = {})
            PetOutlinedButton(text = "Cancelar", onClick = {})
        }
    }
}
