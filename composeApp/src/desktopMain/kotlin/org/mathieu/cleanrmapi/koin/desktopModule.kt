package org.mathieu.cleanrmapi.koin

import org.koin.dsl.module
import org.mathieu.cleanrmapi.audio.AudioPlayer
import org.mathieu.cleanrmapi.audio.DektopAudioPlayer

val desktopModule = module {
    single<AudioPlayer> { DektopAudioPlayer() }
}