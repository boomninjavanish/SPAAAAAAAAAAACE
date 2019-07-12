package media.dunlap.space;

import com.softsynth.jmsl.DefaultMusicClock;
import com.softsynth.jmsl.EventScheduler;
import com.softsynth.jmsl.JMSL;
import com.softsynth.jmsl.JMSLMixerContainer;
import com.softsynth.jmsl.JMSLRandom;
import com.softsynth.jmsl.MusicShape;
import com.softsynth.jmsl.jsyn2.JSynMusicDevice;
import com.softsynth.jmsl.jsyn2.JSynUnitVoiceInstrument;
import com.softsynth.jmsl.view.MusicShapeEditor;
import media.dunlap.space.voices.*;

public class CompositionController {
    // make a mixer
    private JMSLMixerContainer mixer;
    
    // declare instruments to be controlled by music shapes
    private JSynUnitVoiceInstrument starTwinkleIns;
    private JSynUnitVoiceInstrument radioStaticIns;
    private JSynUnitVoiceInstrument redShiftIns;
    private JSynUnitVoiceInstrument superSawIns;
    
    // declare all of the music shapes
    private MusicShape starTwinkleMusicShape;
    private MusicShape radioStaticMusicShape;
    private MusicShape redShiftMusicShape;
    private MusicShape superSawMusicShape;
    
    // declare music shape editor
    private MusicShapeEditor musicShapeEditor;
    
    // setup the composition when the object is initialized
    public CompositionController(){
        System.out.println("initializing comp");
        setupJSML();
        startMusicDevices();
        startMixer();
        makeInstruments();
        makeMusicShapes();
    }
    
    private void setupJSML(){
        JMSL.clock = new DefaultMusicClock();
        JMSL.scheduler = new EventScheduler();
        JMSL.scheduler.start();
        JMSL.clock.setAdvance(0.1);
    }
    private void startMusicDevices(){
        JSynMusicDevice musicDevice = JSynMusicDevice.instance();
        musicDevice.open();
    }
    
    // start the mixer
    private void startMixer(){
        mixer = new JMSLMixerContainer();
        mixer.start();
    }
    
    // setup all of the Jsyn voices
    private void makeInstruments(){
        // Star Twinkle 8 voice polyphonic
        starTwinkleIns = new JSynUnitVoiceInstrument(8, StarTwinkle.class.getName());
	mixer.addInstrument(starTwinkleIns);
        
        // Radio Static 2 voice polyphonic
        radioStaticIns = new JSynUnitVoiceInstrument(2, RadioStatic.class.getName());
	mixer.addInstrument(radioStaticIns);
        
        // Red Shift 2 voice polyphonic
        redShiftIns = new JSynUnitVoiceInstrument(2, RedShift.class.getName());
	mixer.addInstrument(redShiftIns);
        
        // Super Saw monophonic
        superSawIns = new JSynUnitVoiceInstrument(1, SuperSaw.class.getName());
	mixer.addInstrument(superSawIns);
    }
    
    // setup all of the music shapes for our instruments
    private void makeMusicShapes(){
        
        // star twinkle
        starTwinkleMusicShape = new MusicShape(starTwinkleIns.getDimensionNameSpace());
        starTwinkleMusicShape.setInstrument(starTwinkleIns);
        starTwinkleMusicShape.setRepeats(1);
        //starTwinkleMusicShape.prefab();
        
        // radio static
        radioStaticMusicShape = new MusicShape(radioStaticIns.getDimensionNameSpace());
        radioStaticMusicShape.setInstrument(radioStaticIns);
        radioStaticMusicShape.setRepeats(4);
        //radioStaticMusicShape.prefab();
        
        // red shift
        redShiftMusicShape = new MusicShape(redShiftIns.getDimensionNameSpace());
        redShiftMusicShape.setInstrument(redShiftIns);
        redShiftMusicShape.setRepeats(2);
        //redShiftMusicShape.prefab();
        
        // super saw 
        superSawMusicShape = new MusicShape(superSawIns.getDimensionNameSpace());
        superSawMusicShape.setInstrument(superSawIns);
        superSawMusicShape.setRepeats(2);
        //superSawMusicShape.prefab();
        
    }
    
    // generate melody for Star Twinkle intrument
    public void generateStarTwinkleMelody(double[] notes){
        // generate a melody
        
        int numberOfNotes = notes.length;
        for (int i=0; i<numberOfNotes; i++){
            starTwinkleMusicShape.set(notes[i], i, 1);
        }
        
        // build the musicshape
        starTwinkleMusicShape.print();
                
    }
    
    public void startStarTwinkleMelody() throws InterruptedException{
        
        starTwinkleMusicShape.launch(JMSL.now());
    }
    
    public void startSuperSawMelody() throws InterruptedException{
        
        superSawMusicShape.launch(JMSL.now());
    }
    
    public void startRedShiftMelody() throws InterruptedException{
        
        redShiftMusicShape.launch(JMSL.now());
    }
    
    public void startRadioStaticMelody() throws InterruptedException{
               
        for (int i = 0; i < 5; i++ ){
            double dur = JMSLRandom.choose(9, 15); //dur
            double pitch = JMSLRandom.choose(77, 83);//pitch
            double amp = JMSLRandom.choose(0.9, 1);//amp
            double hold = dur * 1.4;//hold
            
            // load her up
            radioStaticMusicShape.add(dur, pitch, amp, hold);
        }
        radioStaticMusicShape.launch(JMSL.now());
    }
    
    public void stopRadioStaticMelody() throws InterruptedException{
        radioStaticMusicShape.finishAll();
    }
    
    public void addToStarTwinkleShape(double dur, double pitch, double amp, double hold, 
            double delayLeft, double delayRight){
        starTwinkleMusicShape.add(dur, pitch, amp, hold, delayLeft, delayRight);
    }
    
    public void addToSuperSawShape(double dur, double pitch, double amp, double hold, 
            double lowPassFreq, double qModMultiplier){
        superSawMusicShape.add(dur, pitch, amp, hold, lowPassFreq, qModMultiplier);
    }
    
    public void addToRedShiftShape(double dur, double pitch, double amp, double hold){
        redShiftMusicShape.add(dur, pitch, amp, hold);
    }
}
