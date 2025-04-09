package org.mathieu.cleanrmapi.audio
import java.io.File
import javax.sound.sampled.AudioSystem

class DektopAudioPlayer : AudioPlayer{
    override fun playSound() {
        val file = File("src/commonMain/resources/sound.ogg")
        val audioStream = AudioSystem.getAudioInputStream(file)
        val clip = AudioSystem.getClip()
        clip.open(audioStream)
        clip.start()
    }
}