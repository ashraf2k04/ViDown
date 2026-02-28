package com.ashraf.vidown.ui.screens.homescreen.components.modalsheet.quality

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashraf.vidown.ui.screens.homescreen.helpers.QualityTier
import com.ashraf.vidown.ui.screens.homescreen.components.modalsheet.AnimatedFilterChip


@Composable
fun QualityTierChipRow(
    selectedTier: QualityTier?,
    onTierSelected: (QualityTier) -> Unit
) {
    val tiers = listOf(
        QualityTier.Best,
        QualityTier.High,
        QualityTier.Medium,
        QualityTier.Low
    )

    Column {
        Text(
            text = "Select Quality",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tiers.chunked(2).forEach { rowTiers ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowTiers.forEach { tier ->
                            AnimatedFilterChip(
                                modifier = Modifier.weight(1f),
                                selected = selectedTier == tier,
                                onClick = { onTierSelected(tier) },
                                label = { Text(tier.label) }
                            )
                    }

                    // 👇 keeps spacing if last row has only 1 item
                    if (rowTiers.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}