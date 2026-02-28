package com.ashraf.vidown.domain.helpers

import kotlinx.serialization.json.Json

val json = Json { ignoreUnknownKeys = true }

const val BASENAME = "%(title).200B"

const val EXTENSION = ".%(ext)s"

const val OUTPUT_TEMPLATE_DEFAULT = BASENAME + EXTENSION