import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;
import org.junit.Before;
import org.junit.Test;
import ru.liga.App;
import ru.liga.songtask.domain.Note;
import ru.liga.songtasksolution.AnalyzeVoiceTrack;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnalyzeVoiceTrackTest {
    public static MidiFile midiFile;
    public static Tempo tempo;
    public static List<MidiTrack> tracks;
    public static List<Note> notes;
    public static MidiTrack trackWithWords;
    public static MidiTrack trackWithTextWords;


    @Before
    public void before() {
        try {
            String path = App.class.getClassLoader().getResource("Wrecking Ball.mid").getFile();
            path = path.replaceAll("%20", " ");
            midiFile = new MidiFile(new FileInputStream(path));
            tracks = midiFile.getTracks();
            tempo = (Tempo) tracks.get(0).getEvents().last();
            trackWithTextWords = AnalyzeVoiceTrack.trackWithTextWords(tracks);
            trackWithWords = AnalyzeVoiceTrack.trackWithWords(tracks, trackWithTextWords);
            notes = App.eventsToNotes(trackWithWords.getEvents());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void checkTrackWithTextWords() {
        MidiTrack midiTrack = AnalyzeVoiceTrack.trackWithTextWords(tracks);
        assertThat(midiTrack).isEqualTo(tracks.get(12));
    }

    @Test(expected = RuntimeException.class)
    public void checkNoFoundTrackWithTextWords() {
        List list = new ArrayList();
        AnalyzeVoiceTrack.trackWithTextWords(list);
    }

    @Test
    public void checkTrackWithWords() {
        MidiTrack trackWithTextWords = AnalyzeVoiceTrack.trackWithTextWords(tracks);
        MidiTrack trackWithWords = AnalyzeVoiceTrack.trackWithWords(tracks, trackWithTextWords);
        assertThat(trackWithWords).isEqualTo(tracks.get(2));
    }

    @Test(expected = RuntimeException.class)
    public void checkNoFoundTrackWithWords() {
        MidiTrack midiTrack = new MidiTrack();
        List midiTracks = Collections.EMPTY_LIST;
        AnalyzeVoiceTrack.trackWithWords(midiTracks, midiTrack);
    }


    @Test
    public void checkNoteDiapason() {
        int diapasonNote = AnalyzeVoiceTrack.noteDiapason(notes);
        assertThat(diapasonNote).isEqualTo(15);
    }

    @Test
    public void checkNoteRepeatCount() {
        HashMap<String, Integer> noteEntering = AnalyzeVoiceTrack.getNoteRepeat(notes);
        assertThat(noteEntering.get("A3")).isEqualTo(150);
    }

    @Test
    public void checkNoteRepeatCou2t() {
        HashMap<String, Integer> noteEntering = AnalyzeVoiceTrack.getNoteRepeat(notes);
        assertThat(noteEntering.get("A3")).isEqualTo(110);
    }


    @Test
    public void checkNoteDurationRepeatCount() {
        HashMap<Integer, Integer> noteDurationEntering = AnalyzeVoiceTrack.getDurationRepeat(notes, tempo.getBpm(), midiFile.getResolution());
        assertThat(noteDurationEntering.get(2875)).isEqualTo(4);
    }
}
