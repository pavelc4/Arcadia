package com.android.arcadia.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Window
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArcadiaSettingsScreen(onPerAppClick: () -> Unit = {}) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Arcadia",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Master Switch
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Enable Overlay",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            "Show performance stats in-game",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                    Switch(
                        checked = true,
                        onCheckedChange = { /* TODO: Implement global overlay service toggle */ },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.onPrimary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        )
                    )
                }
            }

            Text(
                "Overlay Features",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 12.dp)
            )
            
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Row 1: FPS & CPU
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FeatureTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Speed,
                        title = "FPS",
                        subtitle = "Frame Rate",
                        checked = true,
                        activeColor = MaterialTheme.colorScheme.primary
                    )
                    FeatureTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Memory,
                        title = "CPU",
                        subtitle = "Processor",
                        checked = false,
                        activeColor = MaterialTheme.colorScheme.secondary
                    )
                }

                // Row 2: GPU & RAM
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FeatureTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.GraphicEq,
                        title = "GPU",
                        subtitle = "Graphics",
                        checked = false,
                        activeColor = MaterialTheme.colorScheme.tertiary
                    )
                    FeatureTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Storage,
                        title = "RAM",
                        subtitle = "Memory",
                        checked = false,
                        activeColor = MaterialTheme.colorScheme.error
                    )
                }

                // Row 3: Battery Temp (Full Width)
                FeatureTile(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Thermostat,
                    title = "Battery Temperature",
                    subtitle = "Monitor device heat",
                    checked = true,
                    activeColor = MaterialTheme.colorScheme.secondary
                )
            }

            // Section: Preferences (Appearance + General)
            SettingsSection(title = "Preferences") {
                // Overlay Style
                val styles = listOf(
                    "Horizontal Pill" to "Round & Floating",
                    "Solid Box" to "Compact & Sharp",
                    "Minimal" to "Text Only"
                )
                var styleIndex by remember { mutableIntStateOf(0) }
                var showSliders by remember { mutableStateOf(false) }
                
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = Color.Transparent,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.animateContentSize()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { styleIndex = (styleIndex + 1) % styles.size }
                            ) {
                                Text(
                                    text = "Overlay Style",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${styles[styleIndex].first} • ${styles[styleIndex].second}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            IconButton(onClick = { showSliders = !showSliders }) {
                                Icon(
                                    imageVector = if (showSliders) Icons.Default.KeyboardArrowUp else Icons.Default.Tune,
                                    contentDescription = "Tune",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        
                        if (showSliders) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                AppearanceSlider(label = "Opacity", value = 0.8f, valueText = "80%")
                                AppearanceSlider(label = "Scale", value = 1.0f, valueText = "1.0x")
                                AppearanceSlider(label = "Radius", value = 1.0f, valueText = "28dp")
                            }
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Launcher Icon
                FeatureToggle(
                    icon = Icons.Default.Apps,
                    title = "Show Launcher Icon",
                    subtitle = "Show Arcadia in app drawer",
                    checked = true,
                    iconColor = MaterialTheme.colorScheme.primary
                )
            }

            // Section: Tools (Profiles + Monitor)
            SettingsSection(title = "Tools") {
                NavigationRow(
                    title = "Per-App Settings", 
                    subtitle = "Customize configuration for specific apps", 
                    icon = Icons.Default.Window,
                    onClick = onPerAppClick
                )
                
                NavigationRow(
                    title = "System Monitor", 
                    subtitle = "Real-time metrics & logged history", 
                    icon = Icons.Default.Analytics
                )
            }

            // Section: About
            SettingsSection(title = "About") {
                NavigationRow(
                    title = "Developer", 
                    subtitle = "Pavelc4", 
                    icon = Icons.Default.Person
                )
                NavigationRow(
                    title = "User Guide", 
                    subtitle = "Read official documentation", 
                    icon = Icons.AutoMirrored.Filled.Help
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsSection(
    title: String, 
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun FeatureTile(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    activeColor: Color
) {
    var isChecked by remember { mutableStateOf(checked) } // TODO: Connect to ViewModel
    
    val backgroundColor by animateColorAsState(
        if (isChecked) activeColor else activeColor.copy(alpha = 0.2f) // Tinted background
    )

    val contentColor by animateColorAsState(
        if (isChecked) MaterialTheme.colorScheme.contentColorFor(activeColor) else activeColor
    )

    Surface(
        onClick = { isChecked = !isChecked },
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        modifier = modifier.height(110.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.align(Alignment.TopStart).size(28.dp)
            )

            if (isChecked) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Active",
                    tint = contentColor.copy(alpha = 0.8f),
                    modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
                )
            }
            
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor.copy(alpha = 0.9f), // Higher alpha for readability on color
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun FeatureToggle(
    icon: ImageVector?,
    title: String,
    subtitle: String,
    checked: Boolean,
    iconColor: Color
) {
    var isChecked by remember { mutableStateOf(checked) }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (icon != null) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(32.dp)) 
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        
        Switch(
            checked = isChecked, 
            onCheckedChange = { isChecked = it },
            thumbContent = if (isChecked) {
                {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                null
            }
        )
    }
}

@Composable
fun AppearanceSlider(label: String, value: Float, valueText: String) {
    var sliderValue by remember { mutableStateOf(value) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = valueText, 
                    style = MaterialTheme.typography.labelLarge, 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest
            )
        )
    }
}

@Composable
fun NavigationRow(title: String, subtitle: String, icon: ImageVector? = null, onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // TODO: Implement navigation logic
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

