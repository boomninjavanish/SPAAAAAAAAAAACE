package media.dunlap.space;

import com.softsynth.jmsl.JMSLRandom;
import com.softsynth.jmsl.midi.MidiListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MidiResponder implements MidiListener{
    Main main = new Main("");
    
    // voice controller to be used to change voice
    private VoiceController voiceController;
    private CompositionController compController = new CompositionController();
    
    // note on and off detection
    private boolean isNoteOnIrLeft = false;
    private boolean isNoteOnIrRight = false;
    private boolean isNoteOnButtonLeft = false;
    private boolean isNoteOnButtonRight = false;
    
    // pitches that are sent from the Midi controller to be interpreted
    private final int IR_LEFT_PITCH = 60;
    private final int IR_RIGHT_PITCH = 61;
    private final int BUTTON_LEFT_PITCH = 62;
    private final int BUTTON_RIGHT_PITCH = 63;
    
    // left frequency to update 
    private int leftPitch = 60;
    
    // keep track of radio static mode
    private boolean radioStaticModeOn = false;
    

    public void handleNoteOn(double timeStamp, int channel, int pitch, int velocity) {
        
        // handle left infrared when actuation point has been reached
        if (pitch == IR_LEFT_PITCH) {
            if (isNoteOnButtonRight == false && radioStaticModeOn == false){
                voiceController.noteOn();
            } else {
                try {
                    // start red shift music shape
                    compController.startRedShiftMelody();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MidiResponder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
            System.out.println("InfraLeft note on.");
            isNoteOnIrLeft = true;
        }
        
        // handle right infrared when actuation point has been reached
        if (pitch == IR_RIGHT_PITCH) {
            isNoteOnIrRight = true;
            
        }
        
        // handel left button being pushed
        if (pitch == BUTTON_LEFT_PITCH){
            System.out.println("button L pressed");
            isNoteOnButtonLeft = true;
        }
        
        // handel right button being pushed
        if (pitch == BUTTON_RIGHT_PITCH){
            System.out.println("button R pressed");
            if (radioStaticModeOn == false) {
                //start the radio static music shape
                try {
                    compController.startRadioStaticMelody();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MidiResponder.class.getName()).log(Level.SEVERE, null, ex);
                }
                radioStaticModeOn = true;
                
            } else {
                try {
                    // stop the radio static music shape
                    compController.stopRadioStaticMelody();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MidiResponder.class.getName()).log(Level.SEVERE, null, ex);
                }
                radioStaticModeOn = false;
            }
            isNoteOnButtonRight = true;
        }
    }

    public void handleNoteOff(double timeStamp, int channel, int pitch, int velocity) {
        
        // handle left infrared when release point has been reached
        if (pitch == IR_LEFT_PITCH){
             if (isNoteOnButtonLeft == false){
                voiceController.noteOff();
            } else {
                try {
                compController.startSuperSawMelody();
            } catch (InterruptedException ex) {
                Logger.getLogger(MidiResponder.class.getName()).log(Level.SEVERE, null, ex);
            }
             }
            System.out.println("InfraLeft note off.");
            isNoteOnIrLeft = false;
        }
        
        // handle right infrared when release point has been reached
        if (pitch == IR_RIGHT_PITCH) {
            try {
                compController.startStarTwinkleMelody();
            } catch (InterruptedException ex) {
                Logger.getLogger(MidiResponder.class.getName()).log(Level.SEVERE, null, ex);
            }
            isNoteOnIrRight = false;
        }
        
        // handel left button being released
        if (pitch == BUTTON_LEFT_PITCH){
            System.out.println("button L released");
            isNoteOnButtonLeft = false;
        }
        
        // handel right button being released
        if (pitch == BUTTON_RIGHT_PITCH){
            System.out.println("button R released");
            isNoteOnButtonRight = false;
        }
       
    }

    public void handlePolyphonicAftertouch(double timeStamp, int channel, int pitch, int pressure) {
         
        // left infrared: send analog values while in between actuation and release points
        if (isNoteOnIrLeft == true && pitch == IR_LEFT_PITCH && radioStaticModeOn == false) {
            if (isNoteOnButtonLeft == false){
                System.out.println("InfraLeft pressure = " + pressure);
                
                // multiply pressure value into a low pass filterfrequency
                double lowPassFrequency = pressure * 85.3;
                
                // update with new low pass frequency
                voiceController.updateVoice(0.7, leftPitch, lowPassFrequency);
            } else {
                
                // update Super Saw Music Shape if right button is pressed
                JMSLRandom.setSeed(pressure); // make a seed from the pressure val
                double amp = JMSLRandom.choose(0.5, 1); // choose amp based on seed
                compController.addToSuperSawShape(0.25, pressure, amp, 1, 5000, 0.8);
            } 
        } else if (isNoteOnIrLeft == true && pitch == IR_LEFT_PITCH){
            // update red shift when in radio static mode
            compController.addToRedShiftShape(1, pressure, 0.9, 2);
        }
        
        // right infrared: send analog values while in between actuation and release points
        if (isNoteOnIrRight == true && pitch == IR_RIGHT_PITCH) {
            System.out.println("InfraRight pressure = " + pressure);
            
            // lower amplitude if radio static mode is going on
            double amp = 2;
            if (radioStaticModeOn == true){
                amp = 0.5;
            }
            
            // update Star Twinkle shape based on right IR value
            compController.addToStarTwinkleShape(1, pressure, amp, 2, 0.8, 0.001);
            leftPitch = pressure/2;
        }
    }

    public void handleControlChange(double timeStamp, int channel, int id, int value) {
        // do nothing
    }

    public void handleProgramChange(double timeStamp, int channel, int program) {
        // do nothing
    }

    public void handleChannelAftertouch(double timeStamp, int channel, int pressure) {
        // do nothing
    }

    public void handlePitchBend(double timeStamp, int channel, int lsb, int msb) {
        // do nothing
    }

    public void handleSysEx(double timeStamp, byte[] data) {
        // do nothing
    }
    
    // setup a voice
    public void setup(){
        // initialize voice controller
        voiceController = new VoiceController();
        
        // setup voice controller
        voiceController.start();
    }
    
    // stop a voice being controlled by this sensor
    public void stop(){
        voiceController.stop();
    }
}
