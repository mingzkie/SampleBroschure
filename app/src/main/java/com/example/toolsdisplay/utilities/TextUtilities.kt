package com.example.toolsdisplay.utilities

class TextUtilities {

    companion object {
        @JvmStatic
        fun isNullOrEmpty(value: String?): Boolean {
            return value == null || value.isEmpty()
        }
    }
}