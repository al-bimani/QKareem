package qkareem.classes;

import java.util.ArrayList;

import org.json.JSONObject;

public class Reciter {
    public int id;
    public String name;
    public String Server;
    public String rewaya;
    public int count;
    public String letter;
    public ArrayList<Integer> surahsId = new ArrayList<Integer>();

    public Reciter(JSONObject reciterJson) {
        this.id = reciterJson.getInt("id");
        this.name = reciterJson.getString("name") + "";
        this.Server = reciterJson.getString("Server");
        this.rewaya = reciterJson.getString("rewaya");
        this.count = reciterJson.getInt("count");
        this.letter = reciterJson.getString("letter");

        for (Object surah : reciterJson.getJSONArray("suras")) {
            this.surahsId.add(Integer.parseInt(surah.toString()));
        }
    }

    public boolean hasSurah(int surahId) {
        for (int id : this.surahsId) {
            if (id == surahId) return true;
        }
        return false;
    }

    public String getSurahMp3Url(int surahId) {
        String surahName = "" + surahId;
        while (surahName.length() < 3) {
            surahName = "0" + surahName;
        }
        return this.Server + "/" + surahName + ".mp3";
    }
}