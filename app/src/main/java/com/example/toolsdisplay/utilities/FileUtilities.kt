package com.example.toolsdisplay.utilities

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class FileUtilities {

    companion object {

        @JvmStatic
        fun readFile(inputStream: InputStream) : String {
            val sb : StringBuilder = StringBuilder()
            var strLine: String
            val reader: BufferedReader
            try {
                reader = BufferedReader(InputStreamReader(inputStream, Constant.SCHEME))
                strLine = reader.readLine()
                 while (strLine != null) {
                     sb.append(strLine)
                 }
            } catch (e: IOException) {
                   //ignore
            }
                return sb.toString()
        }

        @JvmStatic
        fun readFile2(inputStream: InputStream) : String? {
            var string: String? = ""
            try {
                val size: Int = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                buffer.toString(Charsets.UTF_8)
                string = String(buffer)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return string
        }
    }

}