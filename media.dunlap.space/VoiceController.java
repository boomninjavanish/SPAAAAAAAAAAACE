package media.dunlap.space;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.midi.MidiConstants;
import com.jsyn.unitgen.LineOut;
import com.softsynth.shared.time.TimeStamp;
import media.dunlap.space.voices.*;

public class VoiceController {
    // synth for holding voice 
    final Synthesizer synthesizer = JSyn.createSynthesizer();
    
    // unitgen voice
    SuperSaw voice;
    
    // starting pitch for our voice; can be changed before starting
    public double initialPitch = 66;
    
    // start the engines
    public void start() {
        synthesizer.start();
        
        // Open our unitgen
        voice = new SuperSaw();
        synthesizer.add(voice);
        
        // patch the unitgen to the output
        LineOut out = new LineOut();
        synthesizer.add(out);
        voice.getOutput().connect(0, out.input, 0); // L
        voice.outputR.connect(0, out.input, 1); // R
        out.start();
    }
    
    // shhhhhhhhhh...stop
    public void stop(){
        synthesizer.stop();
    }
    
    // update the voice with new data
    public void updateVoice(double amp, double pitch, double lowPassFreq){
        // convert midi pitch to frequency
       double freq = MidiConstants.convertPitchToFrequency(pitch);
       
       voice.amplitude.set(amp);
       voice.frequency.set(freq);
       voice.lowPassFreq.set(lowPassFreq);
        
    }
    
    // turn note on
    public void noteOn(){
        TimeStamp timeStamp = new TimeStamp(0);
        voice.noteOn(440, 1.0, timeStamp);
        voice.lowPassFreq.set(10800);
    }
    
    // turn note off
    public void noteOff(){
        TimeStamp timeStamp = new TimeStamp(0);
        voice.noteOff(timeStamp);
    }
    
    
    
    
}
