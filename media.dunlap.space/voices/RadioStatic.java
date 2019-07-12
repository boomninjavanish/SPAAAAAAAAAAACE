package media.dunlap.space.voices;

/**************
** WARNING - this code automatically generated by Syntona.
** The real source is probably a Syntona patch.
** Do NOT edit this file unless you copy it to another directory and change the name.
** Otherwise it is likely to get clobbered the next time you
** export Java source code from Syntona.
**
** Syntona is available from: http://www.softsynth.com/syntona/
*/

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.ports.UnitInputPort;
import com.softsynth.shared.time.TimeStamp;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.PinkNoise;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.unitgen.Circuit;
import com.jsyn.unitgen.ImpulseOscillator;

public class RadioStatic extends Circuit implements UnitVoice {
    // Declare units and ports.
    PassThrough mFrequencyPassThrough;
    public UnitInputPort frequency;
    PassThrough mAmplitudePassThrough;
    public UnitInputPort amplitude;
    PassThrough mOutputPassThrough;
    public UnitOutputPort output;
    PassThrough mOutputRPassThrough;
    public UnitOutputPort outputR;
    SegmentedEnvelope mSegEnv;
    VariableRateMonoReader mMonoRdr;
    SquareOscillator mSquareOsc;
    PinkNoise mPinkNoise;
    ImpulseOscillator mImpulse;
    PinkNoise mPinkNoise2;

    // Declare inner classes for any child circuits.

    public RadioStatic() {
        // Create unit generators.
        add(mFrequencyPassThrough = new PassThrough());
        addPort(frequency = mFrequencyPassThrough.input, "frequency");
        add(mAmplitudePassThrough = new PassThrough());
        addPort(amplitude = mAmplitudePassThrough.input, "amplitude");
        add(mOutputPassThrough = new PassThrough());
        addPort( output = mOutputPassThrough.output, "output");
        add(mOutputRPassThrough = new PassThrough());
        addPort( outputR = mOutputRPassThrough.output, "outputR");
        double[] mSegEnvData = {
            3.1237317475986015, 1.0,
            2.3761443414079704, 1.0,
            5.774725396043537, 0.0,
        };
        mSegEnv = new SegmentedEnvelope( mSegEnvData );
        mSegEnv.setSustainBegin( 1 );
        mSegEnv.setSustainEnd( 1 );
        add(mMonoRdr = new VariableRateMonoReader());
        add(mSquareOsc = new SquareOscillator());
        add(mPinkNoise = new PinkNoise());
        add(mImpulse = new ImpulseOscillator());
        add(mPinkNoise2 = new PinkNoise());
        // Connect units and ports.
        mFrequencyPassThrough.output.connect(mImpulse.frequency);
        mFrequencyPassThrough.output.connect(mSquareOsc.frequency);
        mAmplitudePassThrough.output.connect(mMonoRdr.amplitude);
        mMonoRdr.output.connect(mPinkNoise2.amplitude);
        mMonoRdr.output.connect(mPinkNoise.amplitude);
        mSquareOsc.output.connect(mOutputPassThrough.input);
        mPinkNoise.output.connect(mImpulse.amplitude);
        mImpulse.output.connect(mOutputRPassThrough.input);
        mPinkNoise2.output.connect(mSquareOsc.amplitude);
        // Setup
        frequency.setup(40.0, 1068.325336, 8000.0);
        amplitude.setup(0.0, 1.0, 1.0);
        mMonoRdr.rate.set(1.0);
    }

    public void noteOn(double frequency, double amplitude, TimeStamp timeStamp) {
        this.frequency.set(frequency, timeStamp);
        this.amplitude.set(amplitude, timeStamp);
        mMonoRdr.dataQueue.queueOn( mSegEnv, timeStamp);
    }

    public void noteOff(TimeStamp timeStamp) {
        mMonoRdr.dataQueue.queueOff( mSegEnv, false, timeStamp);
    }
    
    public UnitOutputPort getOutput() {
        return output;
    }
}
