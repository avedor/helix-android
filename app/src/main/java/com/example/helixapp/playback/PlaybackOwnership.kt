package com.example.helixapp.playback

import android.content.Context
import com.example.helixapp.HelixPrefs

/**
 * Tracks which Helix device currently owns the server-authoritative playback clock.
 *
 * Every transport action claims the calling device as the active renderer. When a
 * different device owns the clock, the local player must yield (pause locally) and
 * must not re-claim ownership with a transport POST. A blank active device id means
 * the server has no renderer yet, in which case any device may drive playback.
 */
object PlaybackOwnership {

    @Volatile
    private var activeDeviceId: String = ""

    /** Our stable Helix device id for this installation. */
    fun ownDeviceId(context: Context): String = HelixPrefs.getDeviceId(context)

    fun record(activeDeviceId: String) {
        this.activeDeviceId = activeDeviceId
    }

    fun isActiveDevice(context: Context): Boolean {
        val active = activeDeviceId
        return active.isNotBlank() && active == ownDeviceId(context)
    }

    /** We may render/drive the clock when the server has no active device yet or it is us. */
    fun isRenderCandidate(context: Context): Boolean {
        val active = activeDeviceId
        return active.isBlank() || active == ownDeviceId(context)
    }
}