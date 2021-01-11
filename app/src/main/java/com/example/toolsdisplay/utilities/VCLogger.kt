package com.example.toolsdisplay.utilities

import android.util.Log
import timber.log.Timber

class VCLogger {

    companion object {

        @JvmStatic
        fun v(tag: String, msg: String) {
            Timber.tag(tag).v(msg)
        }

        @JvmStatic
        fun v(tag: String, msg: String, tr: Throwable) {
            Timber.tag(tag).v(tr, msg)
        }

        @JvmStatic
        fun d(tag: String, msg: String) {
            Timber.tag(tag).d(msg)
        }

        @JvmStatic
        fun d(tag: String, msg: String, tr: Throwable) {
            Timber.tag(tag).d(tr, msg)
        }

        @JvmStatic
        fun i(tag: String, msg: String) {
            Timber.tag(tag).i(msg)
        }

        @JvmStatic
        fun i(tag: String, msg: String, tr: Throwable) {
            Timber.tag(tag).i(tr, msg)
        }

        @JvmStatic
        fun w(tag: String, msg: String) {
            Timber.tag(tag).v(msg)
        }

        @JvmStatic
        fun w(tag: String, msg: String, tr: Throwable) {
            Timber.tag(tag).w(tr, msg)
        }

        @JvmStatic
        fun w(msg: String, tr: Throwable) {
            Timber.w(tr, msg)
        }

        @JvmStatic
        fun w(msg: String) {
            Timber.w(msg)
        }

        @JvmStatic
        fun w(tr: Throwable, tag: String) {
            Timber.tag(tag).w(tr)
        }

        @JvmStatic
        fun e(tag: String, msg: String) {
            Timber.tag(tag).e(msg)
        }

        @JvmStatic
        fun e(tag: String, msg: String, tr: Throwable) {
            Timber.tag(tag).e(tr, msg)
        }

        @JvmStatic
        fun wtf(tag: String, msg: String){
            Timber.tag(tag).wtf(msg)
        }

        @JvmStatic
        fun wtf(tag: String, msg: String, tr: Throwable){
            Timber.tag(tag).wtf(tr, msg)
        }

        @JvmStatic
        fun wtf(tag: String, tr: Throwable){
            Timber.tag(tag).wtf(tr)
        }

        @JvmStatic
        fun getStackTracingString(tr: Throwable): String {
            return Log.getStackTraceString(tr)
        }

    }
}