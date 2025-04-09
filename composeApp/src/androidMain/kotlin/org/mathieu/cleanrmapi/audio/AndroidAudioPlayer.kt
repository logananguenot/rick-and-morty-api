package org.mathieu.cleanrmapi.audio

import android.content.Context
import android.media.MediaPlayer
import org.mathieu.cleanrmapi.R

class AndroidAudioPlayer(private val context: Context): AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null

    override fun playSound() {
        if (mediaPlayer == null) {
           mediaPlayer = MediaPlayer.create(context, R.raw.sound)
        }
        mediaPlayer?.start()
    }
}