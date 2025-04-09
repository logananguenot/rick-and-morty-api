package org.mathieu.cleanrmapi.koin

import org.koin.android.ext.koin.androidContext
import org.mathieu.cleanrmapi.audio.AudioPlayer
import org.koin.dsl.module
import org.mathieu.cleanrmapi.audio.AndroidAudioPlayer

val androidModule = module {
    single<AudioPlayer> { AndroidAudioPlayer(androidContext()) }
}