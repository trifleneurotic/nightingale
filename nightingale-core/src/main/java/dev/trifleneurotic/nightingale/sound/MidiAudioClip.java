
package dev.trifleneurotic.nightingale.sound;

import javax.sound.midi.*;
import java.applet.AudioClip;
import java.io.InputStream;

/**
 * The Class MidiAudioClip. Implements the java.applet.AudioClip interface for
 * midi files in desktop-applications.
 */
public class MidiAudioClip implements AudioClip {

	private Sequencer sequencer = null;
	private boolean looped = false;
	
	/**
	 * Instantiates a new midi audio clip.
	 * 
	 * @param sound_stream the sound_stream
	 */
	public MidiAudioClip(InputStream sound_stream) {
		Sequence sequence = null;
		try {
			sequence = MidiSystem.getSequence(sound_stream);
			sequencer = MidiSystem.getSequencer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sequence != null && sequencer != null) {
			sequencer.addMetaEventListener(new MetaEventListener() {
				public void meta(MetaMessage event) {
					if (event.getType() == 47 && looped) {
						play();
					}
				}
			});
			try {
				sequencer.open();
				sequencer.setSequence(sequence);
			} catch (Exception e) {
				e.printStackTrace();
				sequencer = null;
				return;
			}
			if (! (sequencer instanceof Synthesizer)) {
				Synthesizer synt;
				try {
					synt = MidiSystem.getSynthesizer();
					synt.open();
					sequencer.getTransmitter().setReceiver(synt.getReceiver());
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
					sequencer = null;
					return;
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.applet.AudioClip#loop()
	 */
	public void loop() {
		sequencer.setMicrosecondPosition(0);
		if (sequencer != null) {
			looped = true;
			sequencer.start();
		}
	}

	/* (non-Javadoc)
	 * @see java.applet.AudioClip#play()
	 */
	public void play() {
		sequencer.setMicrosecondPosition(0);
		if (sequencer != null) {
			looped = false;
			sequencer.start();
		}
	}

	/* (non-Javadoc)
	 * @see java.applet.AudioClip#stop()
	 */
	public void stop() {
		if (sequencer != null) {
			looped = false;
			sequencer.stop();
		}
	}

}
