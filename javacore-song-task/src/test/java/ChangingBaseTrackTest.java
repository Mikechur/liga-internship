import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;
import org.junit.Test;
import ru.liga.App;
import ru.liga.songtasksolution.ChangingBaseTrack;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangingBaseTrackTest {

    public static MidiFile midiFile;
    public static Tempo tempo;
    public static List<MidiTrack> tracks;

    static {
        try {
            String path = App.class.getClassLoader().getResource("Wrecking Ball.mid").getFile();
            path = path.replaceAll("%20", " ");
            midiFile = new MidiFile(new FileInputStream(path));
            tracks = midiFile.getTracks();
            tempo = (Tempo) tracks.get(0).getEvents().last();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkChangeTrackSpeed() {
        float oldBpmValue = tempo.getBpm();
        int percent = 50;

        float actual = ChangingBaseTrack.changeTrackSpeed(midiFile, percent);
        float expected = oldBpmValue * (100 + 50) / 100;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkTransposAllTrack() {
        TreeSet<MidiEvent> oldMidiEvents = tracks.get(6).getEvents();
        int transposValue = 2;

        // Сравнение значений у Нот типа NoteOn до транспонирования и после

        List<Integer> listOldValues = oldMidiEvents.stream()
                .filter(midiEvent -> midiEvent instanceof NoteOn)
                .map(midiEvent -> (NoteOn) midiEvent)
                .map(NoteOn::getNoteValue)
                .collect(Collectors.toList());

        ChangingBaseTrack.transposAllTrack(tracks, transposValue);

        List<Integer> listNewValues = oldMidiEvents.stream()
                .filter(midiEvent -> midiEvent instanceof NoteOn)
                .map(midiEvent -> (NoteOn) midiEvent)
                .map(NoteOn::getNoteValue)
                .collect(Collectors.toList());

        List<Integer> checkValues = listOldValues.stream()
                .map(value -> value + transposValue)
                .collect(Collectors.toList());

        assertThat(listNewValues).isEqualTo(checkValues);
    }


}
