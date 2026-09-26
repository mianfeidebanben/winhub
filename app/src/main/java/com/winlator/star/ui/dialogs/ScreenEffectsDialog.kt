package com.winlator.star.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import com.winlator.star.ui.XServerDialogState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenEffectsDialog(state: XServerDialogState) {
    val profiles       by state.seProfiles.collectAsState()
    val initProfile    by state.seSelectedProfile.collectAsState()
    val initBrightness by state.seBrightness.collectAsState()
    val initContrast   by state.seContrast.collectAsState()
    val initGamma      by state.seGamma.collectAsState()
    val initFxaa       by state.seFxaa.collectAsState()
    val initCrt        by state.seCrt.collectAsState()
    val initToon       by state.seToon.collectAsState()
    val initNtsc       by state.seNtsc.collectAsState()

    var profileIndex    by remember(initProfile)    { mutableIntStateOf(initProfile) }
    var brightness      by remember(initBrightness) { mutableFloatStateOf(initBrightness) }
    var contrast        by remember(initContrast)   { mutableFloatStateOf(initContrast) }
    var gamma           by remember(initGamma)      { mutableFloatStateOf(initGamma) }
    var fxaa            by remember(initFxaa)       { mutableStateOf(initFxaa) }
    var crt             by remember(initCrt)        { mutableStateOf(initCrt) }
    var toon            by remember(initToon)       { mutableStateOf(initToon) }
    var ntsc            by remember(initNtsc)       { mutableStateOf(initNtsc) }

    var profileDropdownExpanded by remember { mutableStateOf(false) }
    var showAddProfileDialog    by remember { mutableStateOf(false) }
    var showRemoveConfirm       by remember { mutableStateOf(false) }
    var newProfileName          by remember { mutableStateOf("") }

    val profileItems = listOf("-- 默认 --") + profiles

    fun resetToDefault() {
        brightness = 0f; contrast = 0f; gamma = 1.0f
        fxaa = false; crt = false; toon = false; ntsc = false
    }

    Dialog(
        onDismissRequest = { state.dismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("画面效果", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))

                // Profile selector
                ExposedDropdownMenuBox(
                    expanded = profileDropdownExpanded,
                    onExpandedChange = { profileDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = profileItems.getOrElse(profileIndex) { "-- 默认 --" },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("配置文件") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = profileDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = profileDropdownExpanded,
                        onDismissRequest = { profileDropdownExpanded = false }
                    ) {
                        profileItems.forEachIndexed { i, label ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = { profileIndex = i; profileDropdownExpanded = false }
                            )
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { showAddProfileDialog = true },
                        modifier = Modifier.weight(1f)
                    ) { Text("添加") }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { if (profileIndex > 0) showRemoveConfirm = true },
                        modifier = Modifier.weight(1f),
                        enabled = profileIndex > 0
                    ) { Text("删除") }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Color adjustment sliders
                Text("色彩调节", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(4.dp))

                LabeledSlider("Brightness: ${brightness.toInt()}", brightness, -100f..100f) { brightness = it }
                LabeledSlider("Contrast: ${contrast.toInt()}",     contrast,   -100f..100f) { contrast   = it }
                LabeledSlider("Gamma: ${"%.2f".format(gamma)}",    gamma,      0.5f..3.0f)  { gamma      = it }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Shader toggles
                Text("着色器", style = MaterialTheme.typography.labelMedium)
                SeCheckRow("启用 FXAA",        fxaa) { fxaa = it }
                SeCheckRow("启用 CRT 着色器",  crt)  { crt  = it }
                SeCheckRow("启用卡通着色器", toon) { toon = it }
                SeCheckRow("启用 NTSC 效果", ntsc) { ntsc = it }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Action buttons
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = { resetToDefault() }) { Text("重置") }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { state.dismiss() }) { Text("取消") }
                    TextButton(onClick = {
                        state.onScreenEffectsApply?.invoke(
                            brightness, contrast, gamma, fxaa, crt, toon, ntsc, profileIndex
                        )
                        state.dismiss()
                    }) { Text("应用") }
                }
            }
        }
    }

    // Add profile dialog
    if (showAddProfileDialog) {
        AlertDialog(
            onDismissRequest = { showAddProfileDialog = false },
            title = { Text("添加配置") },
            text = {
                OutlinedTextField(
                    value = newProfileName,
                    onValueChange = { newProfileName = it },
                    label = { Text("配置名称") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newProfileName.isNotBlank()) {
                        state.onSeAddProfile?.invoke(newProfileName.trim())
                        newProfileName = ""
                    }
                    showAddProfileDialog = false
                }) { Text("添加") }
            },
            dismissButton = {
                TextButton(onClick = { showAddProfileDialog = false; newProfileName = "" }) {
                    Text("取消")
                }
            }
        )
    }

    // Remove profile confirm
    if (showRemoveConfirm) {
        AlertDialog(
            onDismissRequest = { showRemoveConfirm = false },
            title = { Text("删除配置") },
            text = { Text("Remove '${profileItems.getOrElse(profileIndex) { "" }}'?") },
            confirmButton = {
                TextButton(onClick = {
                    val name = profiles.getOrNull(profileIndex - 1) ?: ""
                    state.onSeRemoveProfile?.invoke(name)
                    profileIndex = 0
                    showRemoveConfirm = false
                }) { Text("删除") }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveConfirm = false }) { Text("取消") }
            }
        )
    }
}

@Composable
private fun LabeledSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Text(label, style = MaterialTheme.typography.bodySmall)
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = range,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    )
}

@Composable
private fun SeCheckRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Spacer(Modifier.width(4.dp))
        Text(label)
    }
}
