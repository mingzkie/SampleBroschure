package com.example.toolsdisplay.utilities

import java.util.regex.Pattern

class Constant {

    companion object {
        val PASSWORD_PATTERN: Pattern =
            Pattern.compile(
                "^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                        //"(?=.*[a-zA-Z])" +      //any letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{7,}" +               //at least 4 characters
                        "$"
            );

        const val KEY_FIRST_NAME: String = "first_name"
        const val KEY_LAST_NAME: String = "last_name"
        const val KEY_EMAIL: String = "email"
        const val KEY_PASSWORD: String = "password"

        const val KEY_BUNDLE_SIGN_UP_INFO: String = "sign_up_info"

        const val DATABASE_NAME: String = "Users"

        const val BASE_URL: String = "https://milwaukee.dtndev.com/rest/default/V1/"

        const val IMAGE_LINK: String = "https://milwaukee.dtndev.com/pub/media/catalog/product/"

        const val NO_IMAGE_LINK: String = "https://upload.wikimedia.org/wikipedia/commons/6/65/No-Image-Placeholder.svg"

        const val SHARED_PREFERENCE_FILENAME = "com.example.toolsdisplay.SharedSecuredPreferences"

        const val AUTH_BEARER_KEY_NAME = "Bearer "

        const val SORT_ORDER = "ASC"

        const val ENTITY_ID = "entity_id"



    }
}