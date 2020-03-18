package ru.liga;


import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.songtask.domain.Note;
import ru.liga.songtask.domain.NoteSign;
import ru.liga.songtasksolution.AnalyzeVoiceTrack;
import ru.liga.songtasksolution.ChangingBaseTrack;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class App {

    private final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException {

        MidiFile midiFile = new MidiFile(new FileInputStream(args[0]));

        if (args.length == 2 && args[1].equals("analyze")) {
            logger.info("Анализируемый трек: " + args[0] + "...");
            List<MidiTrack> tracks = midiFile.getTracks();

            logger.info("Нахождение трека с текстом...");
            MidiTrack trackWithTextWords = AnalyzeVoiceTrack.trackWithTextWords(tracks);

            logger.info("Нахождение трека с голосом" +
                    "...");
            MidiTrack trackWithWords = AnalyzeVoiceTrack.trackWithWords(tracks, trackWithTextWords);
            List<Note> notes = eventsToNotes(trackWithWords.getEvents());
            Tempo last = (Tempo) midiFile.getTracks().get(0).getEvents().last();

            logger.info("Нахождение диапазона нот...");
            AnalyzeVoiceTrack.noteDiapason(notes);
            logger.info("Нахождение повторений длительностей нот...");
            AnalyzeVoiceTrack.getDurationRepeat(notes, last.getBpm(), midiFile.getResolution());
            logger.info("Нахождение повторений нот...");
            AnalyzeVoiceTrack.getNoteRepeat(notes);
        }

        if (args.length == 6 && args[1].equals("change")) {
            logger.info("Изменяемый трек: " + args[0]);
            logger.info("Величина транспонирования: " + args[3]);
            logger.info("Изменение темпа трека: " + args[5] + " процентов");

            logger.info("Транспонирование трека...");
            ChangingBaseTrack.transposAllTrack(midiFile.getTracks(), Integer.parseInt(args[3]));
            logger.info("Изменение темпа трека...");
            ChangingBaseTrack.changeTrackSpeed(midiFile, Integer.parseInt(args[5]));
            logger.info("Сохранение в новый файл...");
            MidiFile newMidiFile = new MidiFile(midiFile.getResolution(), midiFile.getTracks());
            String addingToFileName = args[2] + args[3] + args[4] + args[5];
            ChangingBaseTrack.saveToFile(newMidiFile, addingToFileName, args[0]);
        }
    }

    /**
     * @param events эвенты одного трека
     * @return список нот
     */
    public static List<Note> eventsToNotes(TreeSet<MidiEvent> events) {
        List<Note> vbNotes = new ArrayList<>();

        Queue<NoteOn> noteOnQueue = new LinkedBlockingQueue<>();
        for (MidiEvent event : events) {
            if (event instanceof NoteOn || event instanceof NoteOff) {
                if (isEndMarkerNote(event)) {
                    NoteSign noteSign = NoteSign.fromMidiNumber(extractNoteValue(event));
                    if (noteSign != NoteSign.NULL_VALUE) {
                        NoteOn noteOn = noteOnQueue.poll();
                        if (noteOn != null) {
                            long start = noteOn.getTick();
                            long end = event.getTick();
                            vbNotes.add(
                                    new Note(noteSign, start, end - start));
                        }
                    }
                } else {
                    noteOnQueue.offer((NoteOn) event);
                }
            }
        }
        return vbNotes;
    }

    private static Integer extractNoteValue(MidiEvent event) {
        if (event instanceof NoteOff) {
            return ((NoteOff) event).getNoteValue();
        } else if (event instanceof NoteOn) {
            return ((NoteOn) event).getNoteValue();
        } else {
            return null;
        }
    }

    private static boolean isEndMarkerNote(MidiEvent event) {
        if (event instanceof NoteOff) {
            return true;
        } else if (event instanceof NoteOn) {
            return ((NoteOn) event).getVelocity() == 0;
        } else {
            return false;
        }

    }


}

