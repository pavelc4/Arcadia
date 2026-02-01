package com.android.arcadia.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlin.random.Random
import com.android.arcadia.ui.components.SummaryCard
import com.android.arcadia.ui.components.ChartCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogDetailScreen(
    packageName: String,
    onBack: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    
    // TODO: Replace with real data from LogRepository
    val fpsData = remember { List(50) { Random.nextInt(45, 61).toFloat() } } // 60fps target
    val tempData = remember { List(50) { Random.nextInt(38, 45).toFloat() } } // Celsius
    
    val avgFps = fpsData.average().toInt()
    val lowFps = fpsData.sorted().take(5).average().toInt() // 1% Low approximation
    val maxTemp = tempData.maxOrNull()?.toInt() ?: 0

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Performance Analysis",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = packageName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    // TODO: Implement CSV Export logic
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Download, "Export CSV")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    title = "Avg FPS",
                    value = "$avgFps",
                    icon = Icons.Default.Speed,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "1% Low",
                    value = "$lowFps",
                    icon = Icons.Default.Warning,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Max Temp",
                    value = "$maxTemp°C",
                    icon = Icons.Default.Thermostat,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
            }

            // Charts
            ChartCard(
                title = "FPS History", 
                data = fpsData, 
                color = MaterialTheme.colorScheme.primary,
                gridSteps = listOf(0f, 45f, 60f, 90f, 120f)
            )
            ChartCard(
                title = "Temperature History", 
                data = tempData, 
                color = MaterialTheme.colorScheme.tertiary, 
                suffix = "°C",
                gridSteps = listOf(30f, 40f, 50f)
            )
            
            // Spacer for FAB
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
