package qkareem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;

import qkareem.classes.Reciter;
import qkareem.classes.Surah;

public class QuranMP3 {

    public ArrayList<Surah> surahs = new ArrayList<Surah>();
    public ArrayList<Reciter> reciters = new ArrayList<Reciter>();

    public QuranMP3() {
        try {
            String source;

            source = new String(Files.readAllBytes(Paths.get("json/surahs.json")), "UTF-8");
            JSONArray surahsListJson = new JSONArray(source);

            source = new String(Files.readAllBytes(Paths.get("json/reciters.json")), "UTF-8");
            JSONArray recitersListJson = new JSONArray(source);

            for (Object surahJson : surahsListJson) {
                Surah surah = new Surah((JSONObject) surahJson);
                this.surahs.add(surah);
            }

            for (Object reciterJson : recitersListJson) {
                Reciter reciter = new Reciter((JSONObject) reciterJson);
                reciters.add(reciter);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final Reciter getReciter(String reciterName) {
        Reciter reciter = null;
        for (int i = 0; i < this.reciters.size(); i++) {
            reciter = this.reciters.get(i);
            if (reciter.name.strip().equals(reciterName)) {
                break;
            } else reciter = null;
        }
        return reciter;
    }

    public final Surah getReciterSurahById(Reciter reciter, int surahId) {
        Surah _surah = null;
        for (Surah surah : this.surahs) {
            if (surah.id == surahId) {
                if (reciter.hasSurah(surah.id))
                    _surah = surah;
                else
                    _surah = null;
                break;
            }
        }
        return _surah;
    }
}