package ru.liga.songtasksolution;

import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.meta.Lyrics;
import com.leff.midi.event.meta.TrackName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.App;
import ru.liga.songtask.domain.Note;
import ru.liga.songtask.util.SongUtils;

import java.util.*;
import java.util.stream.Collectors;

public class AnalyzeVoiceTrack {
    private final static Logger logger = LoggerFactory.getLogger(AnalyzeVoiceTrack.class);

    public static MidiTrack trackWithTextWords(List<MidiTrack> midiTracks) {

     // Ищем трек с названием Words, в котором слова лежат

        List<MidiTrack> midiTracks2 = midiTracks.stream()
                .filter(x -> x.getEvents().stream()
                        .anyMatch(midiEvent -> midiEvent instanceof TrackName && ((TrackName) midiEvent).getTrackName().equals("words")))
                .collect(Collectors.toList());

        // получаем либо пустой список либо состоящий из 1 элемента

        if (!midiTracks2.isEmpty()) {
            logger.info("Трек с текстовыми словами найден.");
            return midiTracks2.get(0);
        }

        logger.error("Трек с текстовыми словами не найден.");
        throw new RuntimeException("Трек с текстовыми словами не найден.");
    }

    public static MidiTrack trackWithWords(List<MidiTrack> midiTracks, MidiTrack trackWithTextWords) {

        // Ищем трек с голосом - сначала по Lyrics(в 2 из 3 треков быстро найдёт по ним)
        List<MidiTrack> midiTrackWithLyrics = midiTracks.stream()
                .filter(x -> x.getEvents().stream()
                        .anyMatch(midiEvent -> midiEvent instanceof Lyrics))
                .collect(Collectors.toList());

        // получаем либо пустой список либо состоящий из 1 элемента

        if (!midiTrackWithLyrics.isEmpty()) {
            logger.info("Трек с голосом найден.");
            return midiTrackWithLyrics.get(0);
        }

        List<Long> ticks = trackWithTextWords.getEvents().stream().map(MidiEvent::getTick).collect(Collectors.toList());

        // Сравнение тиков трека где слова, с тиками из других треков.
        // При полном соответствии(allMatch) трек найден
        List<MidiTrack> midiTrackByTick = new ArrayList<>();
        if (!ticks.isEmpty()) {
            midiTrackByTick = midiTracks.stream()
                    .filter(x -> !(App.eventsToNotes(x.getEvents())).isEmpty())
                    .filter(x -> App.eventsToNotes(x.getEvents())
                            .stream()
                            .allMatch(note -> ticks.contains(note.startTick())))
                    .collect(Collectors.toList());
        }

        // получаем либо пустой список либо состоящий из 1 элемента
        if (!midiTrackByTick.isEmpty()) {
            logger.info("Трек с голосом найден.");
            return midiTrackByTick.get(0);
        }

        logger.error("Трек с голосом не найден.");
        throw new RuntimeException("Трек с голосом не найден.");
    }

    public static int noteDiapason(List<Note> notes) {
        notes.sort(Comparator.comparing(note -> note.sign().getFrequencyHz()));
        String print = String.format("\nДиапазон\n" +
                        "\t  верхняя: %s\n" +
                        "\t  нижняя: %s\n" +
                        "\t  диапазон: %d\n",
                notes.get(0).sign().fullName(),
                notes.get(notes.size() - 1).sign().fullName(),
                notes.get(notes.size() - 1).sign().getMidi() - notes.get(0).sign().getMidi());
        logger.info(print);
        //диапазон - разница между значениями в отсортированном списке между первым и последним
        return notes.get(notes.size() - 1).sign().getMidi() - notes.get(0).sign().getMidi();
    }

    public static HashMap<Integer, Integer> getDurationRepeat(List<Note> notes, float bpm, int resolution) {
        HashMap<Integer, Integer> durationEntering = new HashMap<>();
        for (Note note : notes) {
            int tickToMs = SongUtils.tickToMs(bpm, resolution, note.durationTicks());
            if (durationEntering.containsKey(tickToMs)) {
                durationEntering.put(tickToMs, durationEntering.get(tickToMs) + 1);
            } else {
                durationEntering.put(tickToMs, 1);
            }
        }
        String print = "";
        for (Integer time : durationEntering.keySet()) {
            print += "\t  " + time + "мс: " + durationEntering.get(time) + "\n";
        }
        logger.info("\nКоличество нот по длительностям\n" + print);
        return durationEntering;
    }

    public static HashMap<String, Integer> getNoteRepeat(List<Note> notes) {
        HashMap<String, Integer> noteEntering = new HashMap<>();
        for (Note note : notes) {
            String noteName = note.sign().fullName();
            if (noteEntering.containsKey(noteName)) {
                noteEntering.put(noteName, noteEntering.get(noteName) + 1);
            } else {
                noteEntering.put(noteName, 1);
            }
        }
        String print = "";
        for (String noteName : noteEntering.keySet()) {
            print += "\t  " + noteName + ": " + noteEntering.get(noteName) + "\n";
        }
        logger.info("\nСписок нот с количеством вхождений\n" + print);
        return noteEntering;
    }
}
