SPAAAAAAAAAAACE: A RADIO WAVE'S JOURNEY TO THE CENTER OF THE UNIVERSE
=====================================================================
A desire to create a standalone synthesizer that utilized software
synthesis and a unique gestural input method led to the development of
the airCV hardware platform.

![The airCV hardware that was utilized to perform this piece.](https://github.com/boomninjavanish/media/blob/master/SPAAAAAAAAAAACE/airCV.png) 

This device was realized during the semester and was completed just before this
project began to take shape. In fact, a symbiosis of the two ideas
formed as the class provided the tools necessary to create a custom
synthesizer that would run on standard hardware but allow for the
automation of a piece using algorithms. This, combined with the
flexibility of Java, led to experimentation and, finally, the creation
of SPAAAAAAAAAAACE. This piece was also the result of a deep admiration
of science fiction that focuses on living in the cold and lonely void.
The mystery of the unknown and the desire to explore led to many of the
ideas for this composition.

The realization of a radio wave as the subject of this composition came
about when I was experimenting with Jsyn unit generators in Syntona.
Once the main sound was chosen, others were created around the central
idea of a static-filled radio signal travelling through space to where
it all began.

Technical Description
=====================

Hardware
--------

The hardware consists of two infrared distance sensors
(Sharp GP2Y0A21YK) that are connected to a Teensy 3.2 microcontroller. The microcontroller has been programmed so
that it appears as a general MIDI controller device when plugged into
the USB port of a computer. The code on the microcontroller works based
on three events: an actuation point, a release point, and a measurement
of the distance between the release point and the sensor. 

![Demonstration on how the sensor works.](https://github.com/boomninjavanish/media/blob/master/SPAAAAAAAAAAACE/airSynthIRBeam-01.jpg) 

When the infrared distance sensor detects an
object reaching a certain distance towards it, the actuation point has
been reached and a “note ON” message is sent via MIDI. As the object
approaches the sensor, the values are read, converted to a value between
0-127, then transmitted as polyphonic aftertouch messages to the
computer. When the object retreats far enough away from the infrared
distance sensor, a release point is triggered and a “note OFF” message
is sent through MIDI. In addition to the sensors, the controller
contains two momentary switches. When pressed, these buttons send a MIDI
“note ON” message then a “note OFF” message upon release.

Initially, the goal was to make the hardware an all-in-one device that
contained the midi controller along with a Raspberry Pi computer
 that ran the Java software. This device, dubbed the
airCV, was created using a laser cut case that housed all of the
equipment discussed above. However, it was discovered during the
composition process that the Raspberry Pi was not powerful enough to
output all of the voices simultaneously. Therefore, the Raspberry Pi was
removed and the MIDI controller was plugged into a USB port on a Windows
laptop for the final performance.

Software
--------

The Java application consists of four parts: the main program, a MIDI
interpreter, a voice controller, and a composition controller. The MIDI
interpreter receives the MIDI input then acts upon the MIDI messages
that are received from the USB controller. It may trigger and control a
voice directly using the voice controller or direct aspects of the piece
using the composition controller.

### Jsyn Voices

SPAAAAAAAAAAACE utilizes four custom voices that were created in
Syntona: RadioStatic, RedShift, StarTwinkle, and SuperSaw. RadioStatic utilizes two pink noise generators that
modulate the amplitude of a square wave and an impulse unit generator.
The frequency of both the square wave and impulse unit generators can be
adjusted externally. This creates a sound that emulates a radio that is
attempting to hone in on a signal but never obtains a clear “lock” onto
the signal.

![Radio Static Jsyn/Syntona Patch](https://github.com/boomninjavanish/media/blob/master/SPAAAAAAAAAAACE/RadioStatic.jpg)

StarTwinkle mixes several sine
and square wave unit generators with an adjustable delay for each stereo
channel. With a quick attack and release time, it produces a slightly
“glitched” sound in the higher registers. 

![StarTwinkle Jsyn/Syntona Patch](https://github.com/boomninjavanish/media/blob/master/SPAAAAAAAAAAACE/StarTwinkle.jpg)

RedShift utilizes a couple of red noise unit generators and a
sine oscillator to modulate sine and saw waves. The resulting sound is a
slightly noisy voice with several “whoop whoop” sounds that seemingly
appear from nowhere. 

![RedShift Jsyn/Syntona Patch](https://github.com/boomninjavanish/media/blob/master/SPAAAAAAAAAAACE/RedShift.jpg)

The SuperSaw voice contains a series of saw oscillators that are placed in different, but
slightly out-of-tune, octaves. In addition, two sine waves are used to
modulate the Q control on two low-pass filters. A multiplier can be
adjusted externally to create more or less modulation. This voice has a
rich sound that is reminiscent of a fat analog synthesizer.

![SuperSaw Jsyn/Syntona Patch](https://github.com/boomninjavanish/media/blob/master/SPAAAAAAAAAAACE/SuperSaw.jpg)

### Voice Controller

The voice controller class allows for the direct manipulation of the
Jsyn voices. It starts a Jsyn synthesizer which connects the stereo
output ports of the voice to a line out. Methods for starting and
stopping notes are available publicly and is called from the MIDI
interpreter. An update note method allows for aspects of the voice to be
changed without triggering another envelope. The voice controller is
used for manipulation of the low-pass filter’s frequency in the SuperSaw
voice. In future iterations of this project, the voice controller will
allow the MIDI interpreter to alter more voices.

### Composition Controller

Several JMSL music shapes are created in the composition controller then
launched via the JMSL scheduler. The music shapes are altered via public
methods that receive input data from the MIDI interpreter then generate
or add to the music shapes. Each unit voice has its own music shape
which also has a distinct method for starting and updating the music
shapes. In addition, each music shape is setup with a different number
of repeats and level of polyphony based on the artistic qualities of the
voice.

### MIDI Interpreter

This Java class implements the MidiListener class from JMSL. The MIDI
Interpreter is started from the main class of the application and
initiates a listener to watch for incoming MIDI events using a JMSL
MidiParser class. Each infrared distance sensor and button has been
assigned a permanent MIDI note value. When a “note ON”, “note OFF”, or a
polyphonic aftertouch message is transmitted from the hardware, the MIDI
interpreter will act on those messages based on the MIDI note value.
Polyphonic aftertouch was chosen because, unlike most messages sent via
MIDI, it contains the note value along with the same message
[@midi1996complete pp. 4-7]. This allows the MIDI interpreter to
identify which sensor is sending the aftertouch message. Adding
aftertouch gives each sensor two dimensions of input to alter voices or
the composition: a note may be triggered then aspects of the sound of
the note can be altered based on the distance of the hand from the
sensor.

Performance Description
=======================

The right button on the controller triggers a mode in which the
RadioStatic voice’s envelope is started, then looped. The composition
controller contains a melody generation method that chooses long
durations between nine and fifteen seconds, MIDI pitches between seventy
seven and eighty three, and a hold duration that is one point four times
the chosen duration. These dimensions are added to the music shape via a
for-loop that runs five times.

While in radio static mode, the left infrared distance sensor creates
and launches a music shape that controls the RedShift voice. The
aftertouch pressure values are sent to the composition controller to add
to the music shape. The right infrared distance sensor starts and adds
to the StarTwinkle music shape. The pressure values also affect the
pitches.

When the right button is pressed again, the right infrared distance
sensor performs the same function as before, but at four times the
amplitude as before. The left sensor then controls the SuperSaw voice
via the voice controller: a “note ON” triggers the envelope loop, the
pressure affects the low-pass frequency, and a “note OFF” releases the
voice and completes the envelope. In addition, the pressure values from
the right infrared distance sensor also change the pitches of the
SuperSaw voice sending the pressure values at an octave lower. This
allows the SuperSaw drone to act as a pitch center for the StarTwinkle
melody while allowing changes in the center pitch on a whim.

Holding down the left button creates a SuperSaw music shape with notes
that last a quarter of a second. While this button is held, the pressure
values alter the pitches in the music shape while a random amplitude
value between point five and one is chosen.

Artistic Analysis
=================

The resulting hardware and software combination allowed for the creation
of a piece that communicated a radio wave’s journey to the center of the
universe. This idea initially came about when toying with the
RadioStatic voice: it sounded like a radio signal that was being
obfuscated by random bits of noise. Upon the realization that this was
going to be the main idea of the piece, the rest of the voices were
created to make a soundscape communicating this idea. The SuperSaw voice
was designed to invoke the vastness of space, while the StarTwinkle
voice was created to convey a sense of wonder. Mixing in the RedShift
voice created a mysterious chaotic sound.

Initially, there was a concern about the music shapes having linear and
chromatic melodies. However, moving the hand rapidly in random vertical
movements created some interesting lines. During testing of the piece, a
bug was discovered that caused the SuperSaw voice to break out of the
envelope and create rapid sharp attacks when hovering the hand around
the “note ON” threshold. This was utilized as another expression in the
piece to create a bit of tension.

Overall, using infrared distance sensors to control the voices and
composition allowed for a unique control scheme. In fact, during the
performance of the piece for Algorithmic Composition class, one audience
member described the use of this type of control as seeming like
performing magic. It will be fun to experiment more with this piece in
the future as there are many more dimensions to explore using the
infrared distance sensors.

Conclusion
==========

Experimenting with algorithmic composition using a non-traditional input
device created a rewarding performance that warrants further exploration
of the platform. The desire to create more compositions and to use more
infrared distance sensors in unique orientations will drive this
exploration further. However, it was disappointing that a more portable
platform could not be made in time for the performance. Perhaps, with
further research, a computer that is a similar size to the Raspberry Pi
but has stronger processing power can be found and used in the same
manner as a laptop. However, despite Java being able to run on a variety
of operating systems, it appears that Java’s is only supporting 64 bit,
x86 processors in the latest version of the Java Development Kit. 
Since ARM support has dried up, it may be wise to
choose a mini computer with this processor platform instead of the
Raspberry Pi. However, despite the performance issue, Java was flexible
in that development of the software was done on a Windows machine then
tested on the Raspberry Pi. Java’s ability to run anywhere allowed the
last-minute change to using the MIDI controller with a Windows laptop.
In addition to portablility, Java has excellent integrated development
environments. Eclipse and Netbeans were used during the creation of the
composition and were easy to use due to their helpful documentation and
excellent auto-completion tools.