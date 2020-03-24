package ru.liga.songtasksolution;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class ChangingBaseTrack {
    private final static Logger logger = LoggerFactory.getLogger(ChangingBaseTrack.class);

    public static List<MidiTrack> transposAllTrack(List<MidiTrack> midiTracks, int transposValue) {
        logger.debug("Метод транспонирования трека начал свою работу ...");
        for (int i = 0; i < midiTracks.size(); i++) {
            MidiTrack midiTrack = midiTracks.get(i);
            for (MidiEvent midiEvent : midiTrack.getEvents()) {
                if (midiEvent instanceof NoteOn) {
                    ((NoteOn) midiEvent).setNoteValue(((NoteOn) midiEvent).getNoteValue() + transposValue);
                } else if (midiEvent instanceof NoteOff) {
                    ((NoteOff) midiEvent).setNoteValue(((NoteOff) midiEvent).getNoteValue() + transposValue);
                }
            }
        }
        logger.info("Транспонирование трека завершено.");
        return midiTracks;
    }

    public static float changeTrackSpeed(MidiFile midiFile, int percent) {
        logger.debug("Метод изменения скорости трека начал свою работу...");
        float inhancePersent = 1 + (percent / 100f);
        Tempo tempo = (Tempo) midiFile.getTracks().get(0).getEvents().last();
        tempo.setBpm(tempo.getBpm() * inhancePersent);
        logger.info("Изменение темпа трека завершено.");
        return tempo.getBpm();
    }

    public static void saveToFile(MidiFile midiFile, String addingToFileName, String oldMidiFilePath) {
        logger.debug("Метод сохранения трека в новый файл начал свою работу...");
        File file = new File(oldMidiFilePath);
        String name = file.getName().replaceAll("\\.mid", "");
        String parent = file.getParent();
        String newFilePath = parent + File.separator + name + addingToFileName;
        File output = new File(newFilePath);
        try {
            midiFile.writeToFile(output);
            logger.info("Сохранение выполнено успешно.");
        } catch (IOException e) {
            logger.info("Не получилось выполнить сохранение в файл");
            e.printStackTrace();
        }
    }
}
